package com.giskeskaaren.backup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.giskeskaaren.mergeballs.R;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 5:28 PM
 */
public class Main extends Activity implements View.OnClickListener {
    private Button startGameButton, hiScoreButton;
    private SharedPreferences settings;
    private boolean sound;
    private String name;
    private int level, difficulty;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);
        hiScoreButton = (Button) findViewById(R.id.hiscoreButton);
        hiScoreButton.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        sound = settings.getBoolean("sound", false);
        name = settings.getString("name", "---");
        level = Integer.parseInt(settings.getString("level", "1"));
        difficulty = Integer.parseInt(settings.getString("difficulty", "1"));
    }

    public void onClick(View view) {
        if (view.getId() == R.id.startGameButton) {
            final Intent i = new Intent(this, GameActivity.class);
            i.putExtra("sound", sound);
            i.putExtra("name", name);
            i.putExtra("level", level);
            i.putExtra("difficulty", difficulty);

            Toast t = Toast.makeText(getApplicationContext(), "Touch the screen to start!", Toast.LENGTH_LONG);
            t.show();

            startActivity(i);
        }
        else if (view.getId() == R.id.hiscoreButton) {
            Intent i = new Intent(this, HiScoreActivity.class);
            i.putExtra("name", name);
            i.putExtra("difficulty", difficulty);
//            i.putExtra("level", level);
            startActivity(i);
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

