package com.giskeskaaren.mergeballs;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/15/12
 * Time: 8:49 PM
 */
public class HiScoreActivity extends ListActivity implements View.OnClickListener {
    private String[] hiscores;
    private HiScoreAdder hiScoreAdder;
    private int level = 1;
    private int difficultyInt = 0;
    private String dbname = "DB_";
    private TextView hiScoreTitle;
    private String name, difficulty, title;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hiscore);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        difficultyInt = extras.getInt("difficulty");

        if (difficultyInt == 0) {
            difficulty = "Easy";
        }
        else if (difficultyInt == 1) {
            difficulty = "Medium";
        }
        else if (difficultyInt == 2) {
            difficulty = "Hard";
        }
        setTitle();
        hiScoreTitle = (TextView) findViewById(R.id.hiscoretitle);
        hiScoreTitle.setText(title);

        Button b1 = (Button) findViewById(R.id.prevlevelhibutton);
        Button b2 = (Button) findViewById(R.id.nextlevelhibutton);
        Button b3 = (Button) findViewById(R.id.exitbuttonhi);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

        hiScoreAdder = new HiScoreAdder(name, this);

        hiscores = hiScoreAdder.getHiscores(dbname+difficultyInt+"_"+level);

        load();
    }

    private void setTitle() {
        title = "Level " + level + "    " + difficulty;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.exitbuttonhi) {

            finish();

        }
        else if (view.getId() == R.id.prevlevelhibutton) {
            if (level > 1) {
                --level;
                hiscores = hiScoreAdder.getHiscores(dbname+difficultyInt+"_"+level);
                setTitle();
                hiScoreTitle.setText(title);
                load();
            }
        }
        else if (view.getId() == R.id.nextlevelhibutton) {
            if (level < 20) {
                ++level;
                hiscores = hiScoreAdder.getHiscores(dbname+difficultyInt+"_"+level);
                setTitle();
                hiScoreTitle.setText(title);
                load();
            }
        }
    }

    public void load() {
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hiscores));
        //setListAdapter(new ArrayAdapter<String>(this, R.xml.mylist, hiscores));
    }
}
