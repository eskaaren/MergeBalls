package com.giskeskaaren.backup;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.giskeskaaren.mergeballs.R;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/21/12
 * Time: 12:25 AM
 */
public class Preferences extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}