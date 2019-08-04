package com.snc.customlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    public static final String TAG = "CustomListAdapter";
    private static int mNumberOfApps;
    private ArrayList<InstalledAppInfo> mInstalledApps;
    LayoutInflater mLayoutInflater;
    InfoIconClickListener mInfoIconClickListener;

    public CustomListAdapter(Context ctx, ArrayList<InstalledAppInfo> appList, InfoIconClickListener listener) {
        super();
        mInstalledApps = new ArrayList<>();
        mInstalledApps.addAll(appList);
        mNumberOfApps = appList.size();
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInfoIconClickListener = listener;
    }

    @Override
    public int getCount() {
        Utils.infoLog(TAG,"getCount");
        return mNumberOfApps;
    }

    @Override
    public Object getItem(int i) {
        Utils.infoLog(TAG,"getItem:"+String.valueOf(i));
        return mInstalledApps.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            Utils.infoLog(TAG,"getView : null");
            view = mLayoutInflater.inflate(R.layout.custom_list_layout, null);
            ;
            viewHolder = new ViewHolder(view);


            view.setTag(viewHolder);

        } else {
            Utils.infoLog(TAG,"getView : not null");
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInfoIconClickListener.onInfoIconClicked(i);
            }
        });

        viewHolder.icon.setImageDrawable(mInstalledApps.get(i).getIcon());
        viewHolder.appName.setText(mInstalledApps.get(i).getPackageName().trim());
        return view;
    }

    public class ViewHolder {
        ImageView icon;
        TextView appName;
        ImageView infoIcon;

        ViewHolder(View view) {
            icon = view.findViewById(R.id.img_app_icon);
            appName = view.findViewById(R.id.txt_app_name);
            infoIcon = view.findViewById(R.id.img_btn_app_info);

            icon.setFocusable(false);
            icon.setFocusableInTouchMode(false);
            infoIcon.setFocusable(false);
            infoIcon.setFocusableInTouchMode(false);

        }

    }

    public interface InfoIconClickListener {
        void onInfoIconClicked(int i);
    }
}
