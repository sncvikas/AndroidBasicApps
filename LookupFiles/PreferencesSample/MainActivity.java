package com.sunincha.preferencessample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PreferenceSample";
    private static Context mContext;
    static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        //getFragmentManager to startPreference fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPrefFragment()).commit();


    }

    public static void showToast(String message) {
        Log.i(TAG, message);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static class MyPrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        private SwitchPreference prefShowNotifications;
        private EditTextPreference prefUserName;
        private MultiSelectListPreference prefUserInterests;
        private ListPreference prefNotificationFreq;
        private CheckBoxPreference prefShowAllNews;


        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
        }


        public MyPrefFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //load the fragment from xml
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onStart() {
            super.onStart();

            prefUserName = (EditTextPreference) findPreference("user_name");
            prefUserInterests = (MultiSelectListPreference) findPreference("user_interest");
            prefShowAllNews = (CheckBoxPreference) findPreference("show_all");
            prefShowNotifications = (SwitchPreference) findPreference("notifications");
            prefNotificationFreq = (ListPreference) findPreference("notifications_count");


            prefUserName.setOnPreferenceChangeListener(this);
            prefUserInterests.setOnPreferenceChangeListener(this);
            prefShowAllNews.setOnPreferenceChangeListener(this);
            prefNotificationFreq.setOnPreferenceChangeListener(this);
            prefShowNotifications.setOnPreferenceChangeListener(this);


            final Set<String> defaultInterests = new HashSet<>();
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String currentUserName = prefs.getString("user_name", null);
            boolean showAllInterests = prefs.getBoolean("show_all", false);
            boolean showNotifs = prefs.getBoolean("notifications", false);
            String notificationsCount = prefs.getString("notifications_count", "All");
            Set<String> interests = prefUserInterests.getValues();


            Log.i(TAG, "--------- Current preferences ---------");
            Log.i(TAG, "User Name: " + (currentUserName != null ? currentUserName : ""));
            prefUserName.setSummary((currentUserName != null ? currentUserName : ""));
            Log.i(TAG, "Show All Interests? " + showAllInterests);
            Log.i(TAG, "Show notifications? " + showNotifs);

            Log.i(TAG, "Notifications Count " + notificationsCount);
            prefNotificationFreq.setSummary(notificationsCount.equals("0")?"All":notificationsCount);

            Log.i(TAG, "Interests");
            StringBuilder interestValues = new StringBuilder();
            for (String a : interests) {
                Log.i(TAG, a);
                interestValues.append(getResources().getStringArray(R.array.interests_array)[Integer.parseInt(a)]).append(", ");
            }
            Log.i(TAG, "--------- Current preferences ---------");
            prefUserInterests.setSummary(interestValues);

        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {

            if (preference instanceof CheckBoxPreference) {
                if (preference.getKey().equals("show_all")) {
                    boolean newValue = (Boolean) o;
                    if (newValue)
                        showToast("Selected to show all interests");
                    else
                        showToast("Not showing all interests");

                }
            } else if (preference instanceof SwitchPreference) {
                if (preference.getKey().equals("notifications")) {
                    boolean newValue = (Boolean) o;
                    if (newValue)
                        showToast("Selected to show notifications");
                    else
                        showToast("Not showing notifications");
                }
            } else if (preference instanceof EditTextPreference) {
                String newValue = o.toString();
                showToast("UserName changed to " + newValue);
                preference.setSummary(o.toString());
            } else if (preference instanceof ListPreference) {
                ListPreference notifFreq = (ListPreference) preference;
                int index = notifFreq.findIndexOfValue(o.toString());
                String val = getResources().getStringArray(R.array.notification_frequency_values)[index];
                if (val.equals("0"))
                    val = "All";
                notifFreq.setSummary(val);
                showToast("Notification frequency changed to " + val);
            } else if (preference instanceof MultiSelectListPreference) {

            }
            return true;
        }
    }
}