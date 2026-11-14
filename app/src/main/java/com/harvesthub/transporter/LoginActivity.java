package com.harvesthub.transporter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.harvesthub.transporter.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS = "harvesthub_prefs";
    private static final String KEY_STAY_SIGNED_IN = "stay_signed_in";

    private ActivityLoginBinding binding;
    private boolean emailMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buildHeadline();
        buildTermsFooter();
        restoreStaySignedIn();

        binding.tvUseEmail.setOnClickListener(v -> toggleEmailMode());
        binding.tvForgotPassword.setOnClickListener(v -> onForgotPassword());
        binding.tabRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
        binding.btnTogglePassword.setOnClickListener(v ->
                PasswordToggle.toggle(binding.etPassword, binding.btnTogglePassword));
        binding.btnSignIn.setOnClickListener(v -> attemptSignIn());
    }

    /** Headline with "Local Harvest" picked out in brand green. */
    private void buildHeadline() {
        int dark = ContextCompat.getColor(this, R.color.text_primary);
        int green = ContextCompat.getColor(this, R.color.accent_green);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("Empowering ");
        int greenStart = sb.length();
        sb.append("Local Harvest");
        int greenEnd = sb.length();
        sb.append("\nLogistics");

        sb.setSpan(new ForegroundColorSpan(dark), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(green), greenStart, greenEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvHeadline.setText(sb);
    }

    /** Footer legal line with the linked portion underlined. */
    private void buildTermsFooter() {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("By continuing you agree to HarvestHub's ");
        int linkStart = sb.length();
        sb.append("Carrier Terms & Fair Transit Standards");
        int linkEnd = sb.length();

        int linkColor = ContextCompat.getColor(this, R.color.text_primary);
        sb.setSpan(new UnderlineSpan(), linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.BOLD), linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(linkColor), linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvTermsFooter.setText(sb);
    }

    private void restoreStaySignedIn() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        binding.cbStaySignedIn.setChecked(prefs.getBoolean(KEY_STAY_SIGNED_IN, false));
        binding.cbStaySignedIn.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_STAY_SIGNED_IN, isChecked).apply());
    }

    /** Switches the primary identifier field between mobile number and email. */
    private void toggleEmailMode() {
        emailMode = !emailMode;

        if (emailMode) {
            binding.tvMobileLabel.setText("Email Address");
            binding.tvUseEmail.setText("Use Mobile instead");
            binding.etMobile.setHint("name@example.com");
            binding.etMobile.setInputType(
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_email, 0);
        } else {
            binding.tvMobileLabel.setText("Mobile Number");
            binding.tvUseEmail.setText("Use Email instead");
            binding.etMobile.setHint("e.g. 077 123 4567");
            binding.etMobile.setInputType(InputType.TYPE_CLASS_PHONE);
            binding.etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_smartphone, 0);
        }

        binding.etMobile.setText("");
        binding.etMobile.setError(null);
        binding.etMobile.requestFocus();
    }

    private void onForgotPassword() {
        String id = binding.etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this,
                    "Enter your mobile number or email first, then tap Forgot Password.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Password reset link sent to " + id,
                Toast.LENGTH_LONG).show();
    }

    private void attemptSignIn() {
        String id = binding.etMobile.getText().toString().trim();
        String pin = binding.etPassword.getText().toString();

        if (emailMode) {
            if (TextUtils.isEmpty(id) || !Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                binding.etMobile.setError("Enter a valid email address");
                binding.etMobile.requestFocus();
                return;
            }
        } else {
            String digits = id.replaceAll("\\D", "");
            if (digits.length() < 9 || digits.length() > 11) {
                binding.etMobile.setError("Enter a valid mobile number");
                binding.etMobile.requestFocus();
                return;
            }
        }

        if (pin.length() < 6) {
            binding.etPassword.setError("PIN or password must be at least 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        Toast.makeText(this, "Welcome back! Opening your delivery manifests…",
                Toast.LENGTH_LONG).show();
    }
}
