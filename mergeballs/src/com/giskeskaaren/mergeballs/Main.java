package com.giskeskaaren.mergeballs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 5:28 PM
 */
public class Main extends Activity implements View.OnClickListener {
    private Button startGameButton, hiScoreButton, exitButton, onlineHiScoreButton, preferenceButton, aboutButton;
    private SharedPreferences settings;
    private boolean sound;
    private String name;
    private int level, difficulty;
    private Dialog aboutDialog;
    private Typeface tf;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        aboutDialog = new Dialog(Main.this);

        tf = Typeface.createFromAsset(getAssets(),"data/fonts/Welbut__.ttf");
        TextView tv = (TextView) this.findViewById(android.R.id.title);
        tv.setTypeface(tf);

        startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);
        startGameButton.setTypeface(tf);
        hiScoreButton = (Button) findViewById(R.id.hiscoreButton);
        hiScoreButton.setOnClickListener(this);
        hiScoreButton.setTypeface(tf);
        onlineHiScoreButton = (Button) findViewById(R.id.onlinehiscoreButton);
        onlineHiScoreButton.setOnClickListener(this);
        onlineHiScoreButton.setTypeface(tf);
        preferenceButton = (Button) findViewById(R.id.preferenceButton);
        preferenceButton.setOnClickListener(this);
        preferenceButton.setTypeface(tf);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(this);
        aboutButton.setTypeface(tf);
        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.setTypeface(tf);
    }

    public void onResume() {
        super.onResume();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        sound = settings.getBoolean("sound", false);
        name = settings.getString("name", "---");
        level = Integer.parseInt(settings.getString("level", "1"));
        difficulty = Integer.parseInt(settings.getString("difficulty", "0"));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.startGameButton) {
            final Intent i = new Intent(this, GameActivity.class);
            i.putExtra("sound", sound);
            i.putExtra("name", name);
            i.putExtra("level", level);
            i.putExtra("difficulty", difficulty);
            startActivity(i);
        }
        else if (view.getId() == R.id.hiscoreButton) {
            Intent i = new Intent(this, HiScoreActivity.class);
            i.putExtra("name", name);
            i.putExtra("difficulty", difficulty);
//            i.putExtra("level", level);
            startActivity(i);
        }
        else if (view.getId() == R.id.onlinehiscoreButton) {

            Intent i = new Intent(this, OnlineHiScoreActivity.class);
            startActivity(i);
        }
        else if (view.getId() == R.id.preferenceButton) {
            Intent intent = new Intent(this, Preferences.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.aboutButton) {
            aboutDialog.setContentView(R.layout.about);
            Button b = (Button) aboutDialog.findViewById(R.id.aboutOkButton);
            b.setOnClickListener(this);
            b.setTypeface(tf);
            TextView tv = (TextView) aboutDialog.findViewById(R.id.aboutText);
            tv.setTypeface(tf);
            TextView tv1 = (TextView) aboutDialog.findViewById(R.id.aboutTitle);
            tv1.setTypeface(tf);
            aboutDialog.show();
        }
        else if (view.getId() == R.id.exitButton) {
            finish();
        }
        else if (view.getId() == R.id.aboutOkButton) {
            aboutDialog.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.prefs) {
            Intent intent = new Intent(this, Preferences.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}

