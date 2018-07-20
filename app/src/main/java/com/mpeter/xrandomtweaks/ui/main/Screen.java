package com.mpeter.xrandomtweaks.ui.main;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.mpeter.xrandomtweaks.SnackbarAction;

public interface Screen {
    void showToast(String text, @IntRange(from = 0, to = 1) int length);
    void showSnack(String text, @IntRange(from = 0) int duration);
    void showSnack(String text, @IntRange(from = 0) int duration, @Nullable SnackbarAction action);
}
