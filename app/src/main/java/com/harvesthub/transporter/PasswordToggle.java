package com.harvesthub.transporter;

import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Toggles the visibility of a password field and swaps the accompanying eye icon.
 */
final class PasswordToggle {

    private PasswordToggle() {
    }

    static void toggle(EditText field, ImageButton button) {
        boolean hidden = field.getTransformationMethod() instanceof PasswordTransformationMethod;
        if (hidden) {
            field.setTransformationMethod(null);
            button.setImageResource(R.drawable.ic_eye_off);
        } else {
            field.setTransformationMethod(new PasswordTransformationMethod());
            button.setImageResource(R.drawable.ic_eye);
        }
        // Keep the cursor at the end of the text after toggling.
        field.setSelection(field.getText().length());
    }
}
