package com.giskeskaaren.mergeballs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;
import java.util.Random;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 8:07 PM
 */
public class Ball {

    //    private Context mContext;
    private GameThread mThread;


    private float mX;      // X position
    private float mY;      // Y position

    private double mSpeed;
    private double mAngle;

    private boolean mHit;
    private boolean cHit;

    private static final String TAG = "Ball";

    private float radius;
    private int color;

    private Random rgen = new Random();
    private Point centrePoint;


    public Ball(float radius, float x, float y, int color, Context context, GameThread gameThread, double speed) {
        this.radius = radius;
        this.mX = x;
        this.mY = y;
        this.centrePoint = new Point(x,y);
        this.color = color;
        this.mThread =  gameThread;

        mAngle = rgen.nextInt(360);
        mSpeed = speed - radius/2;
        if (mSpeed < 5) {
            mSpeed = 5.0;
        }
    }

    public double getmX() {
        return mX;
    }

    public double getmY() {
        return mY;
    }

    private boolean updated = false;
    private Double randomChangeTimer = 0.0;
    public void update(Double elapsed) {
        randomChangeTimer += elapsed;

        if (updated) {

            // calculate step in pixels relative to how much time has passed since last move
            double diff = (mSpeed / 1000.0 * elapsed);

            // calculate and apply x, y vectors according to angle
            double radians = mAngle * (Math.PI / 180);

            mX += diff * Math.sin(radians);
            mY += diff * Math.cos(radians);

            // Check for side collisions
            if (mX > mThread.canvasWidth && !mHit) {
                mAngle = -mAngle;
                mX = mThread.canvasWidth;

                mHit = true;
            }
            else if (mX < 0 && !mHit) {
                mAngle = -mAngle;
                mX = 0;

                mHit = true;
            }
            else if (mY > mThread.canvasHeight && !mHit) {
                radians = Math.PI - radians;

                mAngle = radians * (180 / Math.PI);
                mY = mThread.canvasHeight;

                mHit = true;

            }
            else if (mY < 0 && !mHit) {
                radians = Math.PI - radians;

                mAngle = radians * (180 / Math.PI);
                mY = 0;
                mHit = true;
            }
            else if (mHit) {
                mHit = false;
            }

            if (mHit) {
                randomChangeTimer = 0.0;
            }


            //Check for collisions with other balls
            Level level = mThread.getLevel();
            List<Ball> balls = level.getBalls();
            cHit = false;
            for (Ball c : balls) {

                if (!(mX == c.getmX() && mY == c.getmY() )) {
                    if (isCollision(new Point((float)mX, (float)mY), radius, new Point((float)c.getmX(), (float)c.getmY()), c.getRadius()) && !mHit && !cHit) {

                        if (Math.abs(mX-c.getmX()) <= radius+c.radius && Math.abs(mY-c.getmY()) < (radius+c.radius)/2) {
                            mAngle = -mAngle;

                            if (mX < c.getmX()) {
                                --mX;
                            }
                            else {
                                ++mX;
                            }

                            if (mY < c.getmY()) {
                                --mY;
                            }
                            else {
                                ++mY;
                            }

                        }
                        else if (Math.abs(mY-c.getmY()) <= radius+c.radius && Math.abs(mX-c.getmX()) < (radius+c.radius)/2) {
                            radians = Math.PI - radians;
                            mAngle = radians * (180 / Math.PI);

                            if (mX < c.getmX()) {
                                --mX;
                            }
                            else {
                                ++mX;
                            }

                            if (mY < c.getmY()) {
                                --mY;
                            }
                            else {
                                ++mY;
                            }
                        }
                        else if (Math.abs(mX-c.getmX()) <= radius+c.radius) {
                            mAngle = -mAngle;
                            if (mX < c.getmX()) {
                                --mX;
                            }
                            else {
                                ++mX;
                            }

                            if (mY < c.getmY()) {
                                --mY;
                            }
                            else {
                                ++mY;
                            }
                        }
                        else if (Math.abs(mY-c.getmY()) <= radius+c.radius) {
                            radians = Math.PI - radians;
                            mAngle = radians * (180 / Math.PI);
                            if (mX < c.getmX()) {
                                --mX;
                            }
                            else {
                                ++mX;
                            }

                            if (mY < c.getmY()) {
                                --mY;
                            }
                            else {
                                ++mY;
                            }
                        }

                        cHit = true;
                        c.cHit = true;
                    }
                    else if (cHit) {
                        cHit = false;
                    }

                    if (cHit) {
                        randomChangeTimer = 0.0;
                    }
                }
            }

            //Do some random movement
            if (randomChangeTimer > 1500.0 && !mHit & !cHit) {
                boolean b = rgen.nextBoolean();
                if (mAngle > 0) {
                    if(b) {
                        mAngle += 40;
                    }
                    else mAngle -= 40;
                    if (mAngle > 360) {
                        mAngle = 10;
                    }
                }

                randomChangeTimer = 0.0;
            }
        }
        centrePoint.setX(mX);
        centrePoint.setY(mY);
        updated = true;
    }

    private static boolean isCollision(Point p1, float r1, Point p2, double r2)
    {
        final double a = r1 + r2;
        final double dx = p1.x - p2.x;
        final double dy = p1.y - p2.y;
        return a * a > (dx * dx + dy * dy);
    }



    public void draw(Canvas c, Paint p) {

        p.setColor(color);
        //Log.d(TAG, );
        c.drawCircle(mX, mY, radius, p);
    }

    public int getColor() {
        return color;
    }

    public float getRadius() {
        return radius;
    }

    public Object getCentrePoint() {
        return centrePoint;
    }
}
