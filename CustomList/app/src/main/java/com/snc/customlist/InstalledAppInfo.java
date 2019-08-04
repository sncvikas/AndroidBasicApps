package com.snc.customlist;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.widget.ImageView;

class InstalledAppInfo {
    private Drawable mIcon;
    private String mName;
    private String mVersion;
    private String mPackageName;

    public InstalledAppInfo(Drawable icon, String name, String version, String packageName) {
        this.mIcon = icon;
        this.mName = (name==null || name.isEmpty()) ? packageName : name;
        this.mVersion = version;
        this.mPackageName = packageName;
    }

    public String getName() {
        return mName;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public Drawable getIcon(){
        return mIcon;
    }
}
