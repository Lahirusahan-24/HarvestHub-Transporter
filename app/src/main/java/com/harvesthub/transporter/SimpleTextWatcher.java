package com.harvesthub.transporter;

import android.text.Editable;
import android.text.TextWatcher;

/** TextWatcher with no-op before/after hooks for convenience. */
abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // no-op
    }

    @Override
    public void afterTextChanged(Editable s) {
        // no-op
    }
}
