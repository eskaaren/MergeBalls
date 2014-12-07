package com.giskeskaaren.backup;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.giskeskaaren.androidgamedev.lab4.R;
import com.giskeskaaren.mergeballs.R;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/15/12
 * Time: 8:49 PM
 */
public class HiScoreActivity extends ListActivity implements View.OnClickListener {
    private String[] hiscores;// = new String[] {"1. ", "2. ", "3. "};
    private SQLiteAdapter mySQLiteAdapter;
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
        //dbname += (difficulty+level);
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
//        hiscores = new String[] {};
//        String level = "DB_" + Integer.toString(extras.getInt("level"));
//
//        //Create the DB if needed
//        mySQLiteAdapter = new SQLiteAdapter(this, level);
//        mySQLiteAdapter.openToWrite();
//        //mySQLiteAdapter.deleteAll();  //Uncomment to clear DB
//        if (mySQLiteAdapter.queueAll().equals("")) {
//            mySQLiteAdapter.insert("1. --- 10000");
//            mySQLiteAdapter.insert("2. --- 10000");
//            mySQLiteAdapter.insert("3. --- 10000");
//            mySQLiteAdapter.insert("4. --- 10000");
//            mySQLiteAdapter.insert("5. --- 10000");
//        }
//        mySQLiteAdapter.close();
//
//        //Read from DB
//        mySQLiteAdapter = new SQLiteAdapter(this, level);
//        mySQLiteAdapter.openToRead();
//        String contentRead = mySQLiteAdapter.queueAll();
//        hiscores = contentRead.split("\n");
//        mySQLiteAdapter.close();
//
//        //Check against last score and replace if it should in on the list.
//
//        if (extras != null) {
////            String lastscore = extras.getString("LastScore");
////            String[] tmp, tmp2;// = new String[]{"","",""};
//
//            double lastscore = extras.getDouble("score");
//            String [] tmp, tmp2;// = new String[]{"","",""};
//
//            for (int i = 0; i < hiscores.length; ++i) {
//                tmp = hiscores[i].split(" ");
//
//                if (Double.parseDouble(tmp[2]) > lastscore) {
//
//                    for (int j = hiscores.length-1; j > i; --j) {
//                        tmp2 = hiscores[j-1].split(" ");
//                        hiscores[j] = j+1+". "+tmp2[1]+" "+tmp2[2];
//                    }
//                    tmp[2] = Double.toString(lastscore);
//                    hiscores[i] = i+1+". "+tmp[1]+" "+tmp[2];
//                    i = hiscores.length;
//                }
//            }
//        }
//
//        mySQLiteAdapter.openToWrite();
//        mySQLiteAdapter.deleteAll();
//        for (int i = 0; i < hiscores.length; ++i) {
//            mySQLiteAdapter.insert(hiscores[i]);
//        }
//        mySQLiteAdapter.close();

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
            //startActivity(new Intent(this, GameActivity.class));
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


    }
}
