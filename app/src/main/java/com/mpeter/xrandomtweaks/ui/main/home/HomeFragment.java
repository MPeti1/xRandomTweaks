package com.mpeter.xrandomtweaks.ui.main.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.UpdateReceiver;
import com.mpeter.xrandomtweaks.ui.main.hookprefs.HookPreferenceFragment;
import com.mpeter.xrandomtweaks.utils.ToastUtils;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import java.util.ArrayList;

import timber.log.Timber;

public class HomeFragment extends Fragment implements ModuleRecyclerViewAdapter.OnRecyclerViewItemClickListener {
    public static final String LOG_TAG = XposedModule.getLogtag(HomeFragment.class);
    public static final String TAG = "HomeFragment";

    //Xposed state CardView
    TextView moduleState;
    TextView xposedVersionLabel;
    TextView xposedVersion;
    Button fixModule;
    Switch preloadDisabledHooks;
    RadioGroup themeRadioGroup;

    //Tweaks CardView
    RecyclerView recyclerView;
    TextView tweakCount;
    ArrayList<String> apps = new ArrayList<>();
    ModuleRecyclerViewAdapter tweakAdapter;
    LinearLayoutManager layoutManager;

    SharedPreferences xSettings;
    SharedPreferences sharedPrefs;
    Resources r;
    Snackbar snackbar;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
    }

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();

        Fade enterAnim = new Fade();
        enterAnim.setStartDelay(300);
        enterAnim.setDuration(300);

        Fade exitAnim = new Fade();
        exitAnim.setDuration(300);

        homeFragment.setEnterTransition(enterAnim);
        homeFragment.setExitTransition(exitAnim);

        return homeFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xSettings = getContext().getSharedPreferences(App.XSETTINGS_PREF_FILE, Context.MODE_PRIVATE);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        r = getResources();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        moduleState = view.findViewById(R.id.textview_module_state);
        xposedVersionLabel = view.findViewById(R.id.textview_xposed_version_label);
        xposedVersion = view.findViewById(R.id.textview_xposed_version);
        fixModule = view.findViewById(R.id.button_enable_module);
        preloadDisabledHooks = view.findViewById(R.id.switch_preload_disabled_hooks);
        tweakCount = view.findViewById(R.id.textview_tweak_count);
        themeRadioGroup = view.findViewById(R.id.radiogroup_themeselector);

        snackbar = Snackbar.make(view.findViewById(R.id.mainCoordinatorLayout), "", 0);

        recyclerView = view.findViewById(R.id.modules_recyclerview);

        boolean enabled = XposedModule.isModuleEnabled();

        if (enabled){
            moduleState.setText(R.string.module_state_enabled);
            moduleState.setTextColor(ContextCompat.getColor(getActivity(), R.color.material_green500));
            fixModule.setVisibility(View.GONE);
        } else if (UpdateReceiver.isUpdatedSinceBoot()){
            moduleState.setText(R.string.module_state_updated);
            moduleState.setTextColor(ContextCompat.getColor(getActivity(), R.color.material_yellow500));
            fixModule.setText(r.getString(R.string.fixmodule_reboot));

            fixModule.setOnClickListener(view1 -> ToastUtils.makeToast(getContext(), "Reboot action needed", 200));
        } else {
            moduleState.setText(R.string.module_state_disabled);
            moduleState.setTextColor(ContextCompat.getColor(getActivity(), R.color.material_red500));
            fixModule.setText(r.getString(R.string.fixmodule_enable));

            fixModule.setOnClickListener(v -> startActivity(
                    new Intent().setComponent(
                            new ComponentName(
                                    "de.robv.android.xposed.installer",
                                    "de.robv.android.xposed.installer.WelcomeActivity")))
            );
        }

        preloadDisabledHooks.setChecked(xSettings.getBoolean(
                r.getString(R.string.preload_disabled_hooks),
                r.getBoolean(R.bool.preload_disabled_hooks_def))
        );

        preloadDisabledHooks.setOnCheckedChangeListener((compoundButton, enabled1)
                -> xSettings.edit()
                .putBoolean(r.getString(R.string.preload_disabled_hooks), enabled1)
                .apply()
        );

        String themeName = sharedPrefs.getString(r.getString(R.string.app_theme_key), r.getString(R.string.app_theme_light));
        if (themeName.equals(r.getString(R.string.app_theme_light)))
            themeRadioGroup.check(R.id.radiobutton_themeselector_light);
        else if (themeName.equals(r.getString(R.string.app_theme_dark)))
            themeRadioGroup.check(R.id.radiobutton_themeselector_dark);
        else Timber.tag(LOG_TAG).wtf("invalid theme name: %s", themeName);

        themeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radiobutton_themeselector_light:
                        sharedPrefs.edit()
                                .putString(r.getString(R.string.app_theme_key), r.getString(R.string.app_theme_light))
                                .apply();
                        displaySnackbarThemeChanged();
                        break;

                    case R.id.radiobutton_themeselector_dark:
                        sharedPrefs.edit()
                                .putString(r.getString(R.string.app_theme_key), r.getString(R.string.app_theme_dark))
                                .apply();
                        displaySnackbarThemeChanged();
                        break;

                    default:
                        Timber.tag(LOG_TAG).wtf("unknown radiobutton id: %d", i);
                }
            }
        });

        setupRecyclerView();
        setupTweakCount();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClicked(String packageName) {
        Bundle bundle = new Bundle();
        bundle.putString(HookPreferenceFragment.EXTRA_PACKAGE_NAME, packageName);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, HookPreferenceFragment.instantiate(getActivity(), HookPreferenceFragment.class.getCanonicalName(), bundle))
                .commit();
    }

    public interface OnFragmentInteractionListener {
    }

    private void setupRecyclerView(){
        ArrayList<String> apps = SupportedPackages.getPackages();
        ArrayList<ApplicationInfo> appInfos = new ArrayList<>();

        PackageManager packageManager = getContext().getPackageManager();

        for (int i = 0; i < apps.size(); i++) {
            try {
                appInfos.add(packageManager.getPackageInfo(apps.get(i), PackageManager.GET_META_DATA).applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                Timber.tag(LOG_TAG).e(e, "Package not found: %s", apps.get(i));
            }
        }

        tweakAdapter = new ModuleRecyclerViewAdapter(appInfos, this, getContext());
        tweakAdapter.setHasStableIds(true);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(tweakAdapter);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
    }

    private void setupTweakCount(){
        tweakAdapter.getEnabledAppsCounter().observe(this, integer -> {
            int allTweaksCount = tweakAdapter.getItemCount();
            int enabledTweaksCount = integer;

            tweakCount.setText(getString(R.string.tweak_count, enabledTweaksCount, allTweaksCount));
        });
    }

    private void displaySnackbarThemeChanged(){
        snackbar.setText("Theme changed, apply changes?");
        snackbar.setAction("Yes", view -> getActivity().recreate());
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }
}
