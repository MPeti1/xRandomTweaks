package com.mpeter.xrandomtweaks.ui.main.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
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
import com.mpeter.xrandomtweaks.utils.picasso.PackageIconRequestHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ModuleRecyclerViewAdapter extends RecyclerView.Adapter<ModuleRecyclerViewAdapter.ViewHolder> {
    private OnRecyclerViewItemClickListener mListener;
    private ArrayList<ApplicationInfo> mApps;
    private Picasso mPicasso;
    private Context mContext;
    private SharedPreferences mEnabledPackages;

    interface OnRecyclerViewItemClickListener<T> {
        T onListItemClicked(String packageName);
    }

    ModuleRecyclerViewAdapter(ArrayList<ApplicationInfo> apps, OnRecyclerViewItemClickListener listener, Context context) {
        super();

        this.mApps = apps;
        this.mListener = listener;

        mEnabledPackages = context.getSharedPreferences(App.ENABLED_PACKAGES_PREF_FILE, Context.MODE_PRIVATE);
        mContext = context;

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.addRequestHandler(new PackageIconRequestHandler(context));

        mPicasso = builder.build();
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

        holder.itemView.setTag(applicationInfo.packageName);

        mPicasso.load(PackageIconRequestHandler.getUri(applicationInfo.packageName)).into(holder.icon);

        boolean enabled = mEnabledPackages.getBoolean(applicationInfo.packageName, false);
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
            String tag = (String) itemView.getTag();

            mEnabledPackages.edit().putBoolean(tag, isChecked).apply();
        }
    }
}
