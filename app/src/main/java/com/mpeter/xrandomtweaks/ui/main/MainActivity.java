package com.mpeter.xrandomtweaks.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.widget.Toast;

import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.SnackbarAction;
import com.mpeter.xrandomtweaks.ui.main.home.HomeFragment;
import com.mpeter.xrandomtweaks.ui.main.hookprefs.HookPreferenceFragment;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import java.util.Objects;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Screen, HomeFragment.OnFragmentInteractionListener {
    public static final String LOG_TAG = XposedModule.getLogtag(MainActivity.class);
    public static final String FRAGMENT_HOME = "HomeFragment";
    public static final String FRAGMENT_HOOKSETTINGS = "HookPreferenceFragment";

    ArrayMap<String, Fragment> fragmentArrayMap = new ArrayMap<>();
    FragmentManager fragmentManager;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "", 0);
        fragmentManager = getSupportFragmentManager();

        loadFragment(FRAGMENT_HOME);
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
        if (Objects.equals(fragmentManager.findFragmentById(R.id.main_fragment).getTag(), FRAGMENT_HOME))
            super.onBackPressed();
        else loadFragment(FRAGMENT_HOME);
    }

    private void loadFragment(String TAG){
        if (!fragmentArrayMap.containsKey(TAG)){
            switch (TAG){
                case FRAGMENT_HOME:
                    fragmentArrayMap.put(TAG, HomeFragment.getInstance());
                    break;

                case FRAGMENT_HOOKSETTINGS:
                    fragmentArrayMap.put(TAG, HookPreferenceFragment.getInstance());
                    break;

                default:
                    Timber.tag(LOG_TAG).wtf("Unknown fragment tag: %s", TAG);;
            }
        }

        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragmentArrayMap.get(TAG), TAG)
                .commit();
    }
}
