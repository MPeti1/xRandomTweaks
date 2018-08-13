package com.mpeter.xrandomtweaks.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.mpeter.xrandomtweaks.R;

public class IntNumberEditTextPreference extends Preference implements TextWatcher {
    EditText cornerRoundsET;
    Resources r;

    public IntNumberEditTextPreference(Context context) {
        super(context);
        setWidgetLayoutResource(R.layout.edittext_pref_widget);
        r = getContext().getResources();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false);
        cornerRoundsET = holder.itemView.findViewById(R.id.current_dip);

        cornerRoundsET.setText(String.valueOf(getSharedPreferences().getInt(getKey(), r.getInteger(R.integer.gboard_custom_round_corner_dip_def))));
        cornerRoundsET.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && cornerRoundsET.getText().toString().isEmpty())
                cornerRoundsET.setText(r.getInteger(R.integer.gboard_custom_round_corner_dip_def));
        });

        cornerRoundsET.addTextChangedListener(this);
    }

    @Override
    public void onDetached() {
        cornerRoundsET.removeTextChangedListener(this);
        super.onDetached();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().isEmpty())
            getSharedPreferences().edit().putInt(getKey(), Integer.parseInt(editable.toString())).apply();
    }
}
