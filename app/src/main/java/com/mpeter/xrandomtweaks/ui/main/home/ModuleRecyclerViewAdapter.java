package com.mpeter.xrandomtweaks.ui.main.home;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpeter.xrandomtweaks.App;
import com.mpeter.xrandomtweaks.R;
import com.mpeter.xrandomtweaks.xposed.SupportedPackages;
import com.mpeter.xrandomtweaks.xposed.XposedModule;

import java.util.ArrayList;

import timber.log.Timber;

public class ModuleRecyclerViewAdapter extends RecyclerView.Adapter<ModuleRecyclerViewAdapter.ViewHolder> {
    public static final String LOG_TAG = XposedModule.getLogtag(ModuleRecyclerViewAdapter.class);
    private OnRecyclerViewItemClickListener mListener;
    private ArrayList<ApplicationInfo> mApps;
    private Context mContext;
    private SharedPreferences mXSettings;
    private final MutableLiveData<Integer> enabledAppsCounter = new MutableLiveData<>();

    interface OnRecyclerViewItemClickListener {
        void onListItemClicked(String packageName);
    }

    ModuleRecyclerViewAdapter(ArrayList<ApplicationInfo> apps, OnRecyclerViewItemClickListener listener, Context context) {
        super();

        this.mApps = apps;
        this.mListener = listener;

        mXSettings = context.getSharedPreferences(App.XSETTINGS_PREF_FILE, Context.MODE_PRIVATE);
        mContext = context;

        enabledAppsCounter.setValue(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweak_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo applicationInfo = mApps.get(position);
        holder.icon.setImageDrawable(null);
        holder.appName.setText(mContext.getPackageManager().getApplicationLabel(applicationInfo));
        holder.packageName.setText(applicationInfo.packageName);
        holder.icon.setImageDrawable(applicationInfo.loadIcon(mContext.getPackageManager()));
        holder.itemView.setTag(applicationInfo.packageName);

        Resources r = mContext.getResources();

        String prefString = null;
        SupportedPackages.Package pkg = SupportedPackages.Package.forString(applicationInfo.packageName);

        assert pkg != null;
        switch (pkg) {
            case PACKAGE_MIUI_HOME:
                prefString = r.getString(R.string.miuihome_hooks_enabled);
                break;
            case PACKAGE_FB_MESSENGER:
                prefString = r.getString(R.string.messenger_hooks_enabled);
                break;
            case PACKAGE_EGGINC:
                prefString = r.getString(R.string.egginc_hooks_enabled);
                break;
            case PACKAGE_AIMP:
                prefString = r.getString(R.string.aimp_hooks_enabled);
                break;
            case PACKAGE_GBOARD:
                prefString = r.getString(R.string.gboard_hooks_enabled);
                break;

            default:
                Timber.tag(LOG_TAG).e("Invalid Package: getPackageName: %s, toString: %s, name: %s", pkg.getPackageName(), pkg.toString(), pkg.name());
        }

        boolean enabled = mXSettings.getBoolean(prefString, false);
        holder.toggle.setChecked(enabled);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView icon;
        TextView appName;
        TextView packageName;
        CheckBox toggle;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            appName = itemView.findViewById(R.id.app_name);
            packageName = itemView.findViewById(R.id.package_name);
            toggle = itemView.findViewById(R.id.checkbox_enable_tweak);

            itemView.setOnClickListener(this);
            toggle.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onListItemClicked((String) v.getTag());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View itemView = (View) buttonView.getParent().getParent();
            int change;
            if (isChecked) change = 1;
            else change = -1;

            enabledAppsCounter.setValue(enabledAppsCounter.getValue() + change);

            SupportedPackages.Package pkg = SupportedPackages.Package.forString((String) itemView.getTag());

            SharedPreferences.Editor editor = mXSettings.edit();
            Resources r = mContext.getResources();

            assert pkg != null;
            switch (pkg) {
                case PACKAGE_MIUI_HOME:
                    editor.putBoolean(r.getString(R.string.miuihome_hooks_enabled), isChecked);
                    break;
                case PACKAGE_FB_MESSENGER:
                    editor.putBoolean(r.getString(R.string.messenger_hooks_enabled), isChecked);
                    break;
                case PACKAGE_EGGINC:
                    editor.putBoolean(r.getString(R.string.egginc_hooks_enabled), isChecked);
                    break;
                case PACKAGE_AIMP:
                    editor.putBoolean(r.getString(R.string.aimp_hooks_enabled), isChecked);
                    break;
                case PACKAGE_GBOARD:
                    editor.putBoolean(r.getString(R.string.gboard_hooks_enabled), isChecked);
                    break;
                default:
                    Timber.tag(LOG_TAG).e("Invalid Package: getPackageName: %s, toString: %s, name: %s", pkg.getPackageName(), pkg.toString(), pkg.name());
                    break;
            }

            editor.apply();
        }
    }

    public MutableLiveData<Integer> getEnabledAppsCounter() {
        return enabledAppsCounter;
    }
}
