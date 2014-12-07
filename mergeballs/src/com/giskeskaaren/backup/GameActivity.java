package com.giskeskaaren.backup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import com.giskeskaaren.androidgamedev.lab4.R;
import com.giskeskaaren.mergeballs.R;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i(TAG, "GameActivity onCreate!!!");
        saved = savedInstanceState;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gameactivity);

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
        gameThread.setState(GameThread.STATE_READY);
        gameThread.start();


        handler = new Handler();
        handler.post(new Runnable() {

            boolean lock = false;
            double scoreTime = 0;
            double last = 10000;
            public void run() {

                if (!gameThread.getLock()) {
                    lock = false;
                }
                if (gameThread.getRun()) {
                    scoreTime = gameThread.getLevel().getScoreTime();


                    if (last != scoreTime) {
                        lock = true;
                        if (scoreTime > 0 && gameThread.getLevel().getBalls().size() == 5) {
                            level = gameThread.getLevel().getLevelNum();
                            difficulty = gameThread.getDifficulty();

                            hiScoreAdder.addScore(scoreTime, "DB_" + difficulty + "_" + level);
                            //System.out.println("ADDING SCORE!!!!!");
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

                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            gameThread.setState(GameThread.STATE_PAUSED);
            AlertDialog alertDialog = new AlertDialog.Builder(gameSurface.getContext()).create();
            alertDialog.setTitle("Quit game?");
            alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    gameThread.setState(GameThread.STATE_RUNNING);
                }

            });
            alertDialog.setButton2("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    gameThread.setState(GameThread.STATE_STOPPED);
                    finish();
                }

            });
            alertDialog.setIcon(R.drawable.icon);
            alertDialog.show();
        }
        return false;

    }




//    @Override
//    protected void onPause() {
//        super.onPause();
//        //gameThread.gamePause();
//        Bundle b = new Bundle();
//        b.putSerializable("laststate", gameThread.getLevel());
//        b.putString("test", "test1234");
//
//        onSaveInstanceState(b);
//        //Log.i(TAG, "Saving state");
//
////        Level l = (Level)b.getSerializable("laststate");
////        if (l == null) {
////            Log.i(TAG, "Saving state error in serialize level");
////        }
//
//    }
//
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable("laststate", gameThread.getLevel());
//        outState.putString("test", "test12345");
//        super.onSaveInstanceState(outState);
//        //gameThread.saveState(outState);
//        //Log.w(this.getClass().getName(), "SIS called");
//    }



    //long last = System.currentTimeMillis();
//    public boolean onTouch(View view, final MotionEvent event) {
//        // long now = System.currentTimeMillis();
//        //if (now-last > 2) {
//        Log.i(TAG, "Touch... ");
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            gameThread.clearPoints();
//
//         return true;
//        }
//        else if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//            Point point = new Point();
//            point.x = event.getX();
//            point.y = event.getY();
//            gameThread.updatePoint(point);
//           return true;
//            // Log.d(TAG, "point: " + point);
//        }
//        // }
//        // last = now;
//        // }
//        return false;
//    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        //onResume();
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.w(this.getClass().getName(), "onPause!!!!!!!");
// //       savedLevel = gameThread.getLevel();
//        //gameSurface.getThread().gamePause();
//        //gameSurface.getThread().stop();
//    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        gameThread.saveState(outState);
//
//        Log.w(this.getClass().getName(), "onSaveInstanceState!!!");
//    }

//    public void onResume() {
//        super.onResume();
//        Log.w(this.getClass().getName(), "onResume!!!!!!!");
//       if (saved == null) {
//            gameThread.setState(GameThread.STATE_READY);
//            Log.i(this.getClass().getName(), "SIS is null");
//        }
//        else {
//            gameSurface.setRestoreData(saved);
//
//            //gameThread.restoreState(savedInstanceState);
//            //Log.i(this.getClass().getName(), "SIS is nonnull!!!!!!!!!!!!!!!!!!!!!!!!");
//        }
//   }

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

//    public void onRestoreInstanceState(Bundle in) {
//        onResume();
//    }

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

