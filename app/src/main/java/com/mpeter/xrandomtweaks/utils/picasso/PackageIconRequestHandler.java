package com.mpeter.xrandomtweaks.utils.picasso;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

import timber.log.Timber;

public class PackageIconRequestHandler extends RequestHandler {
    public static final String SCHEME_APP_ICON = "app-icon";

    private PackageManager packageManager;

    public PackageIconRequestHandler(Context context) {
        super();
        packageManager = context.getPackageManager();
    }

    public static Uri getUri(String packageName){
        return Uri.fromParts(SCHEME_APP_ICON, packageName, null);
    }

    @Override
    public boolean canHandleRequest(Request data) {
        return SCHEME_APP_ICON.equals(data.uri.getScheme());
    }

    @Nullable
    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        String packageName = request.uri.getSchemeSpecificPart();

        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).applicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Package not found: %s", packageName);
            return null;
        }

        Drawable iconDrawable = applicationInfo.loadIcon(packageManager);
        Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();

        return new Result(iconBitmap, Picasso.LoadedFrom.DISK);
    }
}
