package com.harvesthub.transporter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.harvesthub.transporter.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buildTermsLabel();
        buildSignInFooter();

        binding.tabLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
        binding.tvSignInLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
        binding.btnTogglePassword.setOnClickListener(v ->
                PasswordToggle.toggle(binding.etPassword, binding.btnTogglePassword));
        binding.btnCreateAccount.setOnClickListener(v -> attemptCreateAccount());

        // Reveal the green check icon once a plausible full name is entered.
        binding.etFullName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean looksValid = s != null && s.toString().trim().length() >= 3;
                binding.etFullName.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, looksValid ? R.drawable.ic_check_circle_green : 0, 0);
            }
        });
    }

    /** Terms checkbox label with the linked phrases underlined and bolded. */
    private void buildTermsLabel() {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("I agree to the ");
        int termsStart = sb.length();
        sb.append("HarvestHub Transporter Terms");
        int termsEnd = sb.length();
        sb.append(", 5% Platform Dispatch Commission & ");
        int cargoStart = sb.length();
        sb.append("Cargo Liability Policy");
        int cargoEnd = sb.length();
        sb.append(".");

        int linkColor = ContextCompat.getColor(this, R.color.text_primary);
        for (int[] range : new int[][]{{termsStart, termsEnd}, {cargoStart, cargoEnd}}) {
            sb.setSpan(new UnderlineSpan(), range[0], range[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new StyleSpan(Typeface.BOLD), range[0], range[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        binding.cbTerms.setText(sb);
    }

    /** "Already registered? Sign In" footer with the trailing link picked out. */
    private void buildSignInFooter() {
        int green = ContextCompat.getColor(this, R.color.deep_green_text);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("Already registered? ");
        int linkStart = sb.length();
        sb.append("Sign In");
        sb.setSpan(new StyleSpan(Typeface.BOLD), linkStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new android.text.style.ForegroundColorSpan(green),
                linkStart, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvSignInLink.setText(sb);
    }

    private void attemptCreateAccount() {
        String name = binding.etFullName.getText().toString().trim();
        String mobile = binding.etMobile.getText().toString().trim();
        String nic = binding.etNic.getText().toString().trim();
        String license = binding.etLicense.getText().toString().trim();
        String password = binding.etPassword.getText().toString();

        if (name.length() < 3) {
            fail(binding.etFullName, "Enter the full name as shown on the NIC");
            return;
        }

        String digits = mobile.replaceAll("\\D", "");
        if (digits.startsWith("0")) {
            digits = digits.substring(1); // tolerate leading 0 (077 …)
        }
        if (digits.length() != 9) {
            fail(binding.etMobile, "Enter a valid 9-digit mobile number (e.g. 77 123 4567)");
            return;
        }

        // Modern NICs are 12 digits; legacy NICs are 9 digits + V/X.
        if (!nic.matches("^([0-9]{12}|[0-9]{9}[VvXx])$")) {
            fail(binding.etNic, "Enter a valid NIC (12 digits, or 9 digits + V/X)");
            return;
        }

        if (!license.matches("^[A-Za-z0-9]{5,12}$")) {
            fail(binding.etLicense, "Enter a valid driving license number (e.g. B1234567)");
            return;
        }

        boolean hasLetter = password.matches(".*[A-Za-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        if (password.length() < 8 || !hasLetter || !hasDigit) {
            fail(binding.etPassword, "Use at least 8 characters with letters and numbers");
            return;
        }

        if (!binding.cbTerms.isChecked()) {
            Toast.makeText(this,
                    "Please agree to the HarvestHub Transporter Terms and Cargo Liability Policy to continue.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String firstName = name.split("\\s+")[0];
        Toast.makeText(this,
                "Welcome, " + firstName + "! Your transporter account request has been received.",
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void fail(android.widget.EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }
}
