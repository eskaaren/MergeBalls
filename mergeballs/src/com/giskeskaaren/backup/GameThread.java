package com.giskeskaaren.backup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 6:18 PM
 */
public class GameThread extends Thread {
    private static final String TAG = "GameThread";

    // Game state constants
    public static final int STATE_READY = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_PAUSED = 3;
    public static final int STATE_STOPPED = 4;

    public int state = 1;

    public int canvasWidth;        // Screen width
    public int canvasHeight;       // Screen height

    private double touchX; // Touch X position
    private double touchY; // Touch Y position

    private final SurfaceHolder surfaceHolder;
    private GameSurface gameSurface;
    private Context context;
    private Line line;
    private boolean run = false;
    private boolean firstrun = false;
    private Level level;

    private Point prevPoint;
    private long beginTime;
    private int framesSkipped;
    private long timeDiff;
    private int sleepTime;


    // desired fps
    private final static int 	MAX_FPS = 50;
    // maximum number of frames to be skipped
    private final static int	MAX_FRAME_SKIPS = 5;
    // the frame period
    private final static int	FRAME_PERIOD = 1000 / MAX_FPS;
    private Level resumeLevel = null;
    private boolean sound;
    private int numLevel;
    private int difficulty;

    public GameThread(SurfaceHolder surfaceHolder, Context context, GameSurface gameSurface) {

        //this.canvasHeight = gameSurface.getHeight();
        //this.canvasWidth = gameSurface.getWidth();
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        this.gameSurface = gameSurface;

//        this.level = new Level(surfaceHolder, gameSurface, this, context);
//        level.setSound(sound);
//        level.load(numLevel, difficulty);

        //setState(STATE_STOPPED);
    }

    public void setRun(boolean b) {
        run = b;
        firstrun = true;
    }

    //private long paused = 0;
    public void run() {
        Canvas c;


            //Log.w(TAG, "Starting run()");
        this.level = new Level(surfaceHolder, gameSurface, this, context);
        level.setSound(sound);
        level.load(numLevel, difficulty);

        while (state == STATE_READY) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException ignore) {
            }
        }
        //level.load(1, 2);

        //state = STATE_RUNNING;

        //level.load(1);
//        if (resumeLevel == null) {
//        this.level = new Level(surfaceHolder, gameSurface, this, context);
//            level.load(1);
//            Log.i(TAG, "Level null");
//        }
//        else {
//            level = resumeLevel;
//        }

        sleepTime = 0;


        while (state != STATE_STOPPED) {
            while (state == STATE_PAUSED) {
                long pausestart = System.currentTimeMillis();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignore) {
                }

            long paused = System.currentTimeMillis() - pausestart;
                level.updateStartTime(paused);
            }
            c = null;

            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {


                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;	// resetting the frames skipped
                    if (state == STATE_RUNNING) {
                    // update game state
                    update();
                    }
//                    else {
//                        level.updateStartTime();
//                    }
                    // render state to the screen
                    // draws the canvas on the panel
                    draw(c);

                    // calculate how long did the cycle take
                    timeDiff = System.currentTimeMillis() - beginTime;
                    // calculate sleep time
                    sleepTime = (int)(FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        // if sleepTime > 0 we're OK
                        try {
                            // send the thread to sleep for a short period
                            // very useful for battery saving
                            //Thread.sleep(sleepTime);
                            surfaceHolder.wait(sleepTime);
                        } catch (InterruptedException e) {}
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        // we need to catch up
                        // update without rendering
                        if (state == STATE_RUNNING) {
                        update();
                        }
//                        else {
//                            level.updateStartTime();
//                        }
                        // add frame period to check if in next frame
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private long mLastTime;
    private void update() {
        long now = System.currentTimeMillis();

        if (mLastTime > now) return;

        double elapsed = now - mLastTime;
        if (elapsed > 1000) elapsed = 100; //Game has been paused
        // Log.d(TAG, Double.toString(elapsed));

        level.update(elapsed);

        mLastTime = now;
    }

    public void draw(Canvas canvas) {
        //synchronized (surfaceHolder) {
        canvas.drawColor(Color.BLACK);

        level.draw(canvas);

        //}
    }

    public void setSurfaceSize(DisplayMetrics dm) {
        //synchronized (surfaceHolder) {
            this.canvasHeight = dm.heightPixels;
            this.canvasWidth = dm.widthPixels;
            //Log.d(TAG, canvasHeight + " " + canvasWidth);
        //}
    }

    public void setSurfaceSize(int width, int height) {
        this.canvasHeight = height;
        this.canvasWidth = width;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setTouchPos(double x, double y) {
        //synchronized (surfaceHolder) {
            touchX = x;
            touchY = y;

            Point point = new Point();
            point.x = (float)x;
            point.y = (float)y;
        //}
    }

    public void clearPoints() {
        synchronized (surfaceHolder) {
            level.clearLine();
        }
        prevPoint = null;
    }

    //List<Line> lines = new ArrayList<Line>();
    public void updatePoint(Point point) {
        //if (state != STATE_RUNNING) setState(STATE_RUNNING);
        synchronized (surfaceHolder) {

            if (point != prevPoint && prevPoint != null) {
                double distance = Math.sqrt((point.getX()-prevPoint.getX())*(point.getX()-prevPoint.getX()) + (point.getY()-prevPoint.getY())*(point.getY()-prevPoint.getY()));

                if (distance > 1) {
                    level.addLine(new Line(prevPoint, point), point);
                    level.addPolyPoint(point);
                    prevPoint = point;
                    //Log.w(TAG, "Added line " + prevPoint + " " + point);
                }
            }
            else {
                level.addPolyPoint(point);
                prevPoint = point;
            }

        }
    }


    public Level getLevel() {
        return level;
    }

//    public Bundle saveState(Bundle data) {
//        synchronized (surfaceHolder) {
//            if (data != null) {
//
//                data = level.saveState(data);
//
//            }
//        }
//        return data;
//    }
//
//    public synchronized void restoreState(Bundle savedState) {
//        synchronized (surfaceHolder) {
//           // setState(STATE_RUNNING);
//
//            level.restoreState(savedState);
//        }
//    }

//    public void gamePause() {
//        //To change body of created methods use File | Settings | File Templates.
//    }

//    public void setLevel(Level laststate) {
//        resumeLevel = laststate;
//        Log.i(TAG, "Setting level");
//        if (resumeLevel == null) {
//            Log.i(TAG, "Error resumeLevel null");
//        }
//    }



    public boolean getRun() {
        return run;
    }
    private boolean quit = false;
    public void quit() {
        quit = true;
    }

    public boolean getQuit() {
        return quit;
    }

    private boolean lock = false;
    public void setLock() {
        lock = true;
    }

    public void unLock() {
        lock = false;
    }

    public boolean getLock() {
        return lock;
    }

    public void setLevel(int i) {
          numLevel = i;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }


    public void setState(int s) {
        state = s;
    }

    public Bundle saveState(Bundle data) {
        synchronized (surfaceHolder) {
            if (data != null) {
                Log.i(TAG, "In saveState");
                level.saveState(data);

            }
        }
        return data;
    }
//
    public void restoreState(Bundle savedInstanceState) {
        synchronized (surfaceHolder) {
            setState(STATE_RUNNING);
            //level = new Level(surfaceHolder, gameSurface, this, context);
            //level.load(1,1);
            //mBall.restoreState(savedState);
            //level = (Level) savedInstanceState.getSerializable("level");
            level.restoreState(savedInstanceState);
        }
    }
//
//
//    public void gameStart() {
//        synchronized (surfaceHolder) {
//            //ball.init();
//
//            mLastTime = System.currentTimeMillis();
//        }
//        setState(STATE_RUNNING);
//    }
//
//
    public void gamePause() {
        synchronized (surfaceHolder) {
            if (state == STATE_RUNNING) setState(STATE_PAUSED);
        }
    }
//
//
//    public void gameResume() {
//        Log.i(TAG, "Resuming game!");
//        synchronized (surfaceHolder) {
//            mLastTime = System.currentTimeMillis() + 100;
//        }
//        setState(STATE_RUNNING);
//    }

    public void setResumeLevel(Level savedLevel) {
        level = savedLevel;
    }
}
