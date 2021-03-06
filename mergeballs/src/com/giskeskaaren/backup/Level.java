package com.giskeskaaren.backup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import com.giskeskaaren.androidgamedev.lab4.R;
import com.giskeskaaren.mergeballs.R;

import java.util.*;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 8:23 PM
 */
public class Level {
    private List<Ball> balls;
    private List<Line> lines = new ArrayList<Line>();
    private GameThread gameThread;
    private Context context;
    private SurfaceHolder surfaceHolder;
    private GameSurface gameSurface;
    private static final float radius = 10;
    private int width, height;
    private Random r, r2;
    private Vector<Point> pointVec = new Vector<Point>();
    private ArrayList<Point> mPoints = new ArrayList<Point>();
    private Polygon polygon;
    private  float[] xArr, yArr;
    private MediaPlayer mp;
    private int difficulty = 0;



    private HashMap<Integer,int[][]> levelMap = new HashMap();
    private long startTime;
    private int level = 1;
    public boolean sound;
    private HiScoreAdder hiScoreAdder;
    private String name;

    public Level (SurfaceHolder s, GameSurface gs, GameThread gameThread, Context context) {
        this.gameThread = gameThread;
        this.context = context;
        this.surfaceHolder = s;
        this.gameSurface = gs;
        lines = new ArrayList<Line>();
        pointVec = new Vector<Point>();
        mPoints = new ArrayList<Point>();
//        lines.clear();
//        mPoints.clear();

        r = new Random();
        r2 = new Random();
        r.setSeed(System.currentTimeMillis());
        r2.setSeed(System.currentTimeMillis());

        width = gameThread.getCanvasWidth()-10;
        height = gameThread.getCanvasHeight()-10;

        name = gameThread.getName();
        hiScoreAdder = new HiScoreAdder(name, context);

        // load(1);
    }

    private double speed = 30;
    public void load(int level, int difficulty) {

        this.difficulty = difficulty;

        balls = new LinkedList<Ball>();
        Point p;

        int numBalls = level;
        int factor = 2;

        if (level > 5) {
            numBalls = 1+(level/2);
            speed = 40;
        }
        else if (level > 10) {
            numBalls = 2+(level/3);
            speed = 50;
        }
        else if (level > 15) {
            numBalls = 3+(level/4);
            speed = 60;
        }

        speed += (difficulty*10);

        for (int i = 0; i < numBalls*factor; ++i) {
            p = uniquePoint();
            balls.add(new Ball(radius, p.getX(), p.getY(), Color.RED, context, gameThread, speed));
            p = uniquePoint();
            balls.add(new Ball(radius, p.getX(), p.getY(), Color.BLUE, context, gameThread, speed));
            p = uniquePoint();
            balls.add(new Ball(radius, p.getX(), p.getY(), Color.GREEN, context, gameThread, speed));
            p = uniquePoint();
            balls.add(new Ball(radius, p.getX(), p.getY(), Color.YELLOW, context, gameThread, speed));
            p = uniquePoint();
            balls.add(new Ball(radius, p.getX(), p.getY(), Color.MAGENTA, context, gameThread, speed));
            // Log.d("Level", r.nextFloat() * width + " " + r.nextFloat() * height);

        }
        scoreTime = 0;
        startTime = System.currentTimeMillis();
    }

    public void setStartTime(long time) {
        startTime = time;
    }

    public Point uniquePoint() {
        boolean unique = false;
        //Point p = null;
        float x = r.nextFloat() * width;
        float y = r.nextFloat() * height;
        Point p = new Point(x,y);
        if (pointVec.isEmpty()) {
            unique = true;
        }

        while (!unique && !pointVec.isEmpty()) {
            unique = true;
            for (Point p1 : pointVec) {
                if (Math.abs(x - p1.getX()) < (radius * 2) && Math.abs(y - p1.getY()) < (radius * 2)) {
                    unique = false;
                }

            }
            if (!unique) {
                x = r.nextFloat() * width;
                y = r.nextFloat() * height;
                p = new Point(x,y);
            }

        }
        pointVec.add(p);
        return p;
    }

    public void update(double elapsed) {
        for (Ball ball : balls) {
            ball.update(elapsed);
        }
        long now = System.currentTimeMillis();
        scoreTime = (now-startTime)/1000.00;

    }

    private double scoreTime = 0;

    public void draw(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStrokeWidth(2);
        p.setTextSize(20);
        c.drawText(Double.toString(scoreTime), 20, 20, p);

        for (Ball ball : balls) {
            ball.draw(c, p);
        }
        for (Line line : lines) {
            line.draw(c, p);
            // Log.d("Level", "Drawing line");

        }

    }

    public void addLine(Line line, Point point) {

        boolean b = true;
        for (int i = 0; i < lines.size()-1; ++i) {
            if(line.getIntersection(lines.get(i)) != null) {

                if(!mPoints.isEmpty() && !lines.isEmpty()) {
                    for (int j = 0; mPoints.get(0) != lines.get(i).PointA; ++j) {
                        if(mPoints.size() > 1) {
                            mPoints.remove(0);
                        }

//                    Log.w("Level", "Rmoved polypoint");
                    }
                    createPoly();
                    checkBalls();
                    clearLine();

                    b = false;
                }
            }
        }
        if (b) {
            //Log.w("Level", "Added line");

            lines.add(line);
        }

    }

    private void createPoly() {
        int size = mPoints.size();
        xArr = new float[size];
        yArr = new float[size];
        for (int i = 0; i < size; ++i) {
            xArr[i] = mPoints.get(i).getX();
            yArr[i] = mPoints.get(i).getY();

        }
        polygon = new Polygon(xArr, yArr, size);
        mPoints.clear();
    }

    private int red = 0, blue = 0, green = 0, yellow = 0, magenta = 0;
    private Vector<Ball> inside = new Vector<Ball>();
    private void checkBalls() {
        //Log.i("Level", "checkBalls()!");

        boolean r = false, b = false, g = false, y = false, m = false;
        inside.clear();
        for (int i = 0; i < balls.size(); ++i) {
            Point p = (Point) balls.get(i).getCentrePoint();

            if (polygon.contains(p.getX(), p.getY())) {
                int c = balls.get(i).getColor();

                // System.out.println("checkC: " + inside.size());

                if (checkColors(c)) {
                    inside.add(balls.get(i));
                    switch (c) {
                        case Color.RED:
                            ++red;
                            //System.out.println("red: " + c);
                            break;
                        case Color.BLUE:
                            ++blue;
                            //System.out.println("blue: " + c);
                            break;
                        case Color.GREEN:
                            ++green;
                            //System.out.println("green: " + c);
                            break;
                        case Color.YELLOW:
                            ++yellow;
                            //System.out.println("yellow: " + c);
                            break;
                        case Color.MAGENTA:
                            ++magenta;
                            //System.out.println("magenta: " + c);
                            break;
                        default:
                            //System.out.println("Error color: " + c);
                            break;
                    }
                }
                else {
                    i = balls.size();
                    //return;
                }
            }
        }

        int count = 0;
        if (red != 0) {
            r = true;
            ++count;
        }
        if (blue != 0) {
            b = true;
            ++count;
        }
        if (green != 0) {
            g = true;
            ++count;
        }
        if (yellow != 0) {
            y = true;
            ++count;
        }
        if (magenta != 0) {
            m = true;
            ++count;
        }

        if (count == 1) {
            if (inside.size() >= 2) {
                int color = 0;
                if (r) color = Color.RED;
                else if (b) color = Color.BLUE;
                else if (g) color = Color.GREEN;
                else if (y) color = Color.YELLOW;
                else if (m) color = Color.MAGENTA;

                //System.out.println("Before merge: " + inside.size() + "Color: " + color);
                mergeBalls(inside, color);
            }
        }
        inside.clear();
        //mPoints.clear();
        r = false; b = false; g = false; y = false; m = false;
        red = 0; blue = 0; green = 0; yellow = 0; magenta = 0;
    }

    private void mergeBalls(Vector BallVec, int c) {

        Ball ci = (Ball) BallVec.get(0);
        float x0 = (float) ci.getmX();
        float y0 = (float) ci.getmY();
        float size = 0;

        for (int j = 0; j < BallVec.size(); ++j) {
            Ball k = (Ball) BallVec.get(j);
            size += k.getRadius();

            balls.remove(k);

            // System.out.println("mergeC: " + i.get(j));
            //System.out.println("mergeC: " + balls.get(1).getColor());
        }
        if (sound) {
            playSound();
        }
        balls.add(new Ball(size, x0, y0, c, context, gameThread, speed));
        if (balls.size() == 5) {
            endLevel();
        }
    }

    private double endtime = 0;
    private void endLevel() {
        endtime = scoreTime;

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Level " + level + " cleared!");
        alertDialog.setMessage("Time: " + endtime + "\nProceed to next level or quit?");
        alertDialog.setButton("Next", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //clearLine();
                //balls.clear();
                ++level;
                load(level, difficulty);
                gameThread.unLock();

            }

        });
        alertDialog.setButton2("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                gameThread.quit();
            }

        });
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }

    public void clearLine() {
        lines.clear();
        mPoints.clear();
        //polygon.clearPolygon();
    }

    public boolean checkColors(int c) {
        switch (c) {
            case Color.RED:
                //Log.i("Level: ", "Red!");
                if (blue != 0 || green != 0 || yellow != 0 || magenta != 0) {
                    return false;
                }
                break;
            case Color.BLUE:
                //Log.i("Level: ", "Blue!");
                if (red != 0 || green != 0 || yellow != 0 || magenta != 0) {
                    return false;
                }
                break;
            case Color.GREEN:
                //Log.i("Level: ", "Green!");
                if (red != 0 || blue != 0 || yellow != 0 || magenta != 0) {
                    return false;
                }
                break;
            case Color.YELLOW:
                //Log.i("Level: ", "Yellow!");
                if (red != 0 || blue != 0 || green != 0 || magenta != 0) {
                    return false;
                }
                break;
            case Color.MAGENTA:
                //Log.i("Level: ", "Magenta!");
                if (red != 0 || blue != 0 || green != 0 || yellow != 0) {
                    return false;
                }
                break;
            default:
                //Log.i("Level: ", "Illegal color!!");
                return false;
        }
        return true;
    }

    public void playSound() {
        mp = MediaPlayer.create(context, R.raw.merge);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }
        });
        mp.start();
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void addPolyPoint(Point point) {
        mPoints.add(point);
    }

    public int getLevelNum() {
        return level;
    }




//    public Bundle saveState(Bundle data) {
//        if (data != null) {
//            data.pu
//        }
//        return data;
//    }
//


    public double getScoreTime() {
        return endtime;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }


    public void updateStartTime(long pause) {
        startTime += pause;//(System.currentTimeMillis() - startTime);
    }

    public Bundle saveState(Bundle data) {
        lines.clear();
        pointVec.clear();

        if (data != null) {
//            for (Ball b : balls) {
//                 b.saveState(data);
//            }

            //data.putParcelableArrayList("Balls", (ArrayList<? extends Parcelable>) balls);
            //data.putSerializable("Lines", (Serializable) lines);
            //data.putSerializable("Polygon", polygon);
            //data.putSerializable("Points", pointVec);
            data.putLong("StartTime", startTime);
            data.putDouble("ScoreTime)", scoreTime);
            Log.e("Level", "Save state complete");
        }
        return data;
    }

    public void restoreState(Bundle savedState) {
//            for (Ball b : balls) {
//                b.restoreState(savedState);
//            }
        //balls = savedState.getParcelableArrayList("Balls");
        //lines = (List<Line>) savedState.getSerializable("Lines");
        //  polygon = (Polygon) savedState.getSerializable("Polygon");
        //pointVec = (Vector<Point>) savedState.getSerializable("Points");
        startTime = savedState.getLong("StartTime");
        scoreTime = savedState.getDouble("ScoreTime");
    }
}
