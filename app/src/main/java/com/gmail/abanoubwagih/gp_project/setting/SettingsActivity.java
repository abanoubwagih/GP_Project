package com.gmail.abanoubwagih.gp_project.setting;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.gmail.abanoubwagih.gp_project.LaunchActivity;
import com.gmail.abanoubwagih.gp_project.R;
import com.gmail.abanoubwagih.gp_project.UserHandler.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    public static String city;
    public static String address;
    public static String change_password;
    public static String name;
    public static String turnOnGPS;


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof SwitchPreference) {


                if (preference.getKey().equalsIgnoreCase(turnOnGPS)) {
                    boolean gpsValue = (boolean) value;
                    if (gpsValue) {
                        turnGPSOn();
                    } else {
                        turnGPSOff();
                    }

                    preference.setSummary(stringValue);
                }
            } else {
                if (preference.getKey().equalsIgnoreCase(city)) {
                    if (TextUtils.isEmpty(stringValue)) {
                        SharedPreferences sharedPreferences = LaunchActivity.context.getSharedPreferences(LaunchActivity.context.getString(R.string.sharedPreferences), MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString(LaunchActivity.context.getString(R.string.usrObject), "");
                        User user = gson.fromJson(json, User.class);
                        if (user != null) {

                            stringValue = user.getCity();
                            ((EditTextPreference) preference).setText(stringValue);
                            preference.setSummary(stringValue);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.context);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(city, stringValue);
                            editor.apply();

                            return true;

                        }
                    }

                    updateUserProfile("city", stringValue);
                    preference.setSummary(stringValue);


                } else if (preference.getKey().equalsIgnoreCase(address)) {
                    if (TextUtils.isEmpty(stringValue)) {
                        SharedPreferences sharedPreferences = LaunchActivity.context.getSharedPreferences(LaunchActivity.context.getString(R.string.sharedPreferences), MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString(LaunchActivity.context.getString(R.string.usrObject), "");
                        User user = gson.fromJson(json, User.class);
                        if (user != null) {
                            stringValue = user.getAddress();
                            ((EditTextPreference) preference).setText(stringValue);
                            preference.setSummary(stringValue);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.context);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(address, stringValue);
                            editor.apply();

                            return true;

                        }
                    }

                    updateUserProfile("address", stringValue);
                    preference.setSummary(stringValue);

                } else if (preference.getKey().equalsIgnoreCase(change_password)) {


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = "SOME-SECURE-PASSWORD";
                    if (!TextUtils.isEmpty(stringValue)) {
                        newPassword = stringValue;
                    }

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("settingChangePassword", "User password updated.");
                                    } else {
                                        Log.d("settingChangePassword", "User password failed.");

                                    }
                                }
                            });
                    String email;
                    SharedPreferences sharedPreferences = null;
                    try {
                        sharedPreferences = LaunchActivity.context.getSharedPreferences(LaunchActivity.context.getString(R.string.sharedPreferences), MODE_PRIVATE);
                        email = sharedPreferences.getString(LaunchActivity.context.getString(R.string.loginName), "abanoubwagih@gmail.com");
                    } catch (Exception e) {
                        e.printStackTrace();
                        email = "abanoubwagih@gmail.com";
                    }
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, stringValue);

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("change password", "User re-authenticated.");
                                }
                            });

                } else if (preference.getKey().equalsIgnoreCase(name)) {

                    if (TextUtils.isEmpty(stringValue)) {
                        SharedPreferences sharedPreferences = LaunchActivity.context.getSharedPreferences(LaunchActivity.context.getString(R.string.sharedPreferences), MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString(LaunchActivity.context.getString(R.string.usrObject), "");
                        User user = gson.fromJson(json, User.class);
                        if (user != null) {
                            stringValue = user.getName();
                            ((EditTextPreference) preference).setText(stringValue);
                            preference.setSummary(stringValue);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LaunchActivity.context);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(name, stringValue);
                            editor.apply();

                            return true;

                        }
                    }
                    updateUserProfile("name", stringValue);
                    preference.setSummary(stringValue);

                } else {

                    preference.setSummary(stringValue);
                }
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public static void updateUserProfile(String vield, String value) {
        try {
            SharedPreferences sharedPreferences = LaunchActivity.context.getSharedPreferences(LaunchActivity.context.getString(R.string.sharedPreferences), MODE_PRIVATE);
            String name = sharedPreferences.getString(LaunchActivity.context.getString(R.string.loginName), "abanoubwagih");
            String usedName2 = name != "abanoubwagih" ?
                    (name.split("@")[0].contains(".") ? name.split("@")[0].split(".")[0] : name.split("@")[0])
                    : "abanoubwagih";
            if (usedName2 != null) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(usedName2).child(vield).setValue(value);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        LaunchActivity.context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(LaunchActivity.context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            LaunchActivity.context.sendBroadcast(poke);


        }
    }

    // automatic turn off the gps
    public static void turnGPSOff() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        LaunchActivity.context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(LaunchActivity.context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            LaunchActivity.context.sendBroadcast(poke);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        city = getString(R.string.setting_city);
        name = getString(R.string.setting_customer_name);
        address = getString(R.string.setting_address);
        turnOnGPS = getString(R.string.setting_gps_switch);
        change_password = getString(R.string.setting_change_password);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_gps_switch)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_customer_name)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_address)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_city)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_change_password)));

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
