package com.giskeskaaren.mergeballs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 5:29 PM
 */
public class GameActivity extends Activity {
    private static final String TAG = "GameActivity";

    private GameSurface gameSurface;
    private GameThread gameThread;
    private Handler handler;
    private HiScoreAdder hiScoreAdder;
    private boolean sound;
    private int level, difficulty;
    private String name;
    private Bundle saved;
    private Level savedLevel;
    private Typeface tf;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i(TAG, "GameActivity onCreate!!!");
        saved = savedInstanceState;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gameactivity);

        tf = Typeface.createFromAsset(getAssets(),"data/fonts/Welbut__.ttf");

        Bundle extras = getIntent().getExtras();
        sound = extras.getBoolean("sound");
        level = extras.getInt("level");
        name = extras.getString("name");
        difficulty = extras.getInt("difficulty");
        hiScoreAdder = new HiScoreAdder(name, getApplicationContext());

        final DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        gameSurface = (GameSurface) findViewById(R.id.gameSurface);
        gameThread = gameSurface.getGameThread();
        gameThread.setSurfaceSize(dm);
        gameThread.setSound(sound);
        gameThread.setLevel(level);
        gameThread.setDifficulty(difficulty);
        Log.e(TAG, "gtread");
        gameThread.setState(GameThread.STATE_READY);
        gameThread.start();


        handler = new Handler();
        handler.post(new Runnable() {

            // boolean lock = false;
            double scoreTime = 0;
            double last = 10000;
            public void run() {


                if (gameThread.getRun() || gameThread.getPause()) {
                    scoreTime = gameThread.getLevel().getScoreTime();


                    if (last != scoreTime) {
                        // lock = true;
                        if (scoreTime > 0 && gameThread.getLevel().getBalls().size() == 5) {
                            level = gameThread.getLevel().getLevelNum();
                            difficulty = gameThread.getDifficulty();

                            hiScoreAdder.addScore(scoreTime, "DB_" + difficulty + "_" + level);

                            new Thread(new Runnable() {
                                public void run() {
                                    HttpClient client = new DefaultHttpClient();
                                    HttpPost post = new HttpPost("http://www.giskeskaaren.com/mergeballs/dbinsert.php");

                                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                                    pairs.add(new BasicNameValuePair("name", name));
                                    pairs.add(new BasicNameValuePair("score", String.valueOf(scoreTime)));
                                    pairs.add(new BasicNameValuePair("level", String.valueOf(level)));
                                    pairs.add(new BasicNameValuePair("difficulty", String.valueOf(difficulty)));
                                    try {
                                        post.setEntity(new UrlEncodedFormEntity(pairs));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        HttpResponse response =
                                                client.execute(post);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            last = scoreTime;
                        }
                    }
                }
                if (gameThread.getQuit()) {
                    finish();
                }

                handler.postDelayed(this, 150);
            }
        });


        gameSurface.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                //Log.i(TAG, "Touch... ");
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    gameThread.clearPoints();
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

                    Point point = new Point();
                    //Log.w(TAG, "point: " + point);
                    point.x = event.getX();
                    point.y = event.getY();
                    gameThread.updatePoint(point);
                    return true;
                }
                return false;
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            gameThread.setState(GameThread.STATE_PAUSED);
            TextView tv = new TextView(gameSurface.getContext());
            tv.setText("Quit game?");
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tf);

            AlertDialog.Builder builder = new AlertDialog.Builder(gameSurface.getContext());
            builder.setCustomTitle(tv);
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    gameThread.setState(GameThread.STATE_RUNNING);
                }

            });
            builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    gameThread.setState(GameThread.STATE_STOPPED);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    finish();
                }

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setTypeface(tf);
            Button b2 = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            b2.setTypeface(tf);

        }
        return false;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "destroying");

        gameThread.setState(GameThread.STATE_STOPPED);
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pausemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pause) {
            if (gameThread.state == GameThread.STATE_PAUSED) {
                gameThread.setState(GameThread.STATE_RUNNING);

            }
            else gameThread.setState(GameThread.STATE_PAUSED);
        }

        return super.onOptionsItemSelected(item);
    }

}


