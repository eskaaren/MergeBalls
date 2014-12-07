package com.giskeskaaren.mergeballs;

import android.graphics.PointF;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 10:21 PM
 */
public class Point extends PointF {

    float x, y;

    public Point() {}

    public Point(float px, float py) {
        this.x = px;
        this.y = py;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
