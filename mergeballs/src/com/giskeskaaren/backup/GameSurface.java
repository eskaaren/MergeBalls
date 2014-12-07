package com.giskeskaaren.backup;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.Serializable;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 6:14 PM
 */
//public class GameSurface extends View implements View.OnTouchListener {
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback, Serializable {
private static final String TAG = "GameSurface";

   // List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    private GameThread gameThread;
    private SurfaceHolder holder;
    private Context context;
    private Bundle savedState;

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
         this.context = context;
        //getHolder().addCallback(this);
         holder = getHolder();
        holder.addCallback(this);
        gameThread = new GameThread(holder, context, GameSurface.this);
        setFocusableInTouchMode(true);



    }

    public GameThread getGameThread() {
        return gameThread;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            //gameThread.gamePause();
            gameThread.setState(GameThread.STATE_PAUSED);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.w(TAG, "Starting gameThread");
       // gameThread.setRun(true);
        gameThread.setState(GameThread.STATE_RUNNING);
        //gameThread.start();
//        if (gameThread.state != GameThread.STATE_READY) {
//            gameThread = new GameThread(holder, context, GameSurface.this);
//            gameThread.state = GameThread.STATE_READY;
//            gameThread.start();
//            gameThread.restoreState(savedState);
//
//
//        }
//        else {
//            gameThread.start();
//        }
       // try {
          //}
//        catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
        gameThread.setSurfaceSize(width, height);
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;


        //gameThread.setRun(false);
        gameThread.setState(GameThread.STATE_PAUSED);
//        while (retry) {
//            try {
//                Log.e(TAG, "Surface destroyed");
//                //gameThread.setState(GameThread.STATE_STOPPED);
//                gameThread.join();
//                ((Activity)getContext()).finish();
//                retry = false;
//            } catch (Exception e) {
//                //Try again...
//            }
//        }
//        Log.e(TAG, "Surface destroyed done");
    }

//    public void setLevel(Level laststate) {
//        if (laststate == null) {
//            Log.i(TAG, "Error last state null");
//        }
//        gameThread.setLevel(laststate);
//    }

    public GameThread getThread() {
        return gameThread;
    }

    public void setRestoreData(Bundle savedInstanceState) {
        savedState = savedInstanceState;
    }
}