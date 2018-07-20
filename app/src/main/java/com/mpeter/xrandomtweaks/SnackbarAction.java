package com.mpeter.xrandomtweaks;

import android.view.View;

public class SnackbarAction {
    private View.OnClickListener mAction;
    private String mText = "DEFAULT";

    public SnackbarAction() {
    }

    public SnackbarAction(View.OnClickListener action, String text) {
        mAction = action;
        mText = text;
    }

    public View.OnClickListener getAction() {
        return mAction;
    }

    public void setAction(View.OnClickListener mAction) {
        this.mAction = mAction;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}
