package com.mpeter.xrandomtweaks.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.SnackbarAction;
import com.mpeter.xrandomtweaks.ui.main.home.HomeFragment;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Screen, HomeFragment.OnFragmentInteractionListener {
    FragmentManager fragmentManager;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "", 0);
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment, HomeFragment.getInstance())
                .commit();
    }

    @Override
    public void showToast(@NonNull String text, int length) {
        if (text.isEmpty()) {
            Timber.w("Text is empty");
            return;
        }

        Toast.makeText(this, text, length).show();
    }

    @Override
    public void showSnack(@NonNull String text, int duration) {
        showSnack(text, duration, null);
    }

    @Override
    public void showSnack(@NonNull String text, int duration, SnackbarAction action) {
        if (text.isEmpty()) {
            Timber.w("Text is empty");
            return;
        }

        snackbar.setText(text).setDuration(duration);
        if (action != null) snackbar.setAction(action.getText(), action.getAction());
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, HomeFragment.getInstance())
                .commit();
    }
}
