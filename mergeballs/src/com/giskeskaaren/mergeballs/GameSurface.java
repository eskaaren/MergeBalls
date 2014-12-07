package com.giskeskaaren.mergeballs;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 6:14 PM
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
private static final String TAG = "GameSurface";


    Paint paint = new Paint();

    private GameThread gameThread;
    private SurfaceHolder holder;
    private Context context;
    private Bundle savedState;

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
         this.context = context;
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
            gameThread.setState(GameThread.STATE_PAUSED);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "Starting gameThread");
        gameThread.setState(GameThread.STATE_RUNNING);

    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
        gameThread.setSurfaceSize(width, height);
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        gameThread.setState(GameThread.STATE_PAUSED);
    }

    public GameThread getThread() {
        return gameThread;
    }

}