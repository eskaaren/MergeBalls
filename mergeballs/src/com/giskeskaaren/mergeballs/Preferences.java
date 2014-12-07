package com.giskeskaaren.mergeballs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.TextView;

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
        Typeface tf = Typeface.createFromAsset(getAssets(), "data/fonts/Welbut__.ttf");
        TextView tv = (TextView) findViewById(android.R.id.title);
        tv.setTypeface(tf);
    }

}