package com.giskeskaaren.mergeballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Creator: Eivin Giske Skaaren
 * Credits for algorithms in comments
 * Email: eskaaren@yahoo.no
 * Date: 5/6/12
 * Time: 10:03 PM
 */
public class Line
{
    /**
     * Points where line start and stop.
     */
    public final Point PointA;
    public final Point PointB;


   // Credits: http://stackoverflow.com/questions/385305/efficient-maths-algorithm-to-calculate-intersections

    public final float a;
    public final float b;
    public final float xm;
    public final float ym;

    /**
     * Create a line from two points.
     * @param pointa
     * @param pointb
     */
    public Line(Point pointa, Point pointb)
    {
        PointA = pointa;
        PointB = pointb;

        a = (pointb.y - pointa.y);
        b = (pointa.x - pointb.x);

        xm = (pointa.x + pointb.x) / 2;
        ym = (pointa.y + pointb.y) / 2;
    }

    /**
     * Calculate intersection of two lines.
     *
     * @param otherLine	Other line to intersect with.
     * @return	intersection point, or null if no intersection is found (or other line is a part of this line).
     */
    public Point getIntersection(Line otherLine)
    {
        float result1 = calculateEquation(otherLine.PointA);
        float result2 = calculateEquation(otherLine.PointB);

        // Both same side?
        if (result1 < 0 && result2 < 0)
            return null;
        if (result1 > 0 && result2 > 0)
            return null;

        result1 = otherLine.calculateEquation(PointA);
        result2 = otherLine.calculateEquation(PointB);

        // Both same side?
        if (result1 < 0 && result2 < 0)
            return null;
        if (result1 > 0 && result2 > 0)
            return null;

        // Calculate intersecion.
        // Credits:
        // http://www.gamedev.net/page/resources/_/technical/math-and-physics/fast-2d-line-intersection-algorithm-r423

        float x0 = PointA.getX();
        float y0 = PointA.getY();
        float x1 = PointB.getX();
        float y1 = PointB.getY();

        float x2 = otherLine.PointA.getX();
        float y2 = otherLine.PointA.getY();
        float x3 = otherLine.PointB.getX();
        float y3 = otherLine.PointB.getY();

        float a1, b1, c1, // constants of linear equations
                a2, b2, c2, det_inv, // the inverse of the determinant of the
                // coefficient matrix
                m1, m2; // the slopes of each line

        // compute slopes, note the cludge for infinity, however, this will
        // be close enough

        if ((x1 - x0) != 0)
            m1 = (y1 - y0) / (x1 - x0);
        else
            m1 = (float) 1e+10; // close enough to infinity

        if ((x3 - x2) != 0)
            m2 = (y3 - y2) / (x3 - x2);
        else
            m2 = (float) 1e+10; // close enough to infinity

        // compute constants
        a1 = m1;
        a2 = m2;
        b1 = -1;
        b2 = -1;
        c1 = (y0 - m1 * x0);
        c2 = (y2 - m2 * x2);

        // compute the inverse of the determinate
        det_inv = 1 / (a1 * b2 - a2 * b1);

        // PEK-note: This could happen if a line is a sub part of another line. Example:
        // line1: (0, 0); (4,4), line2: (2,2); (3,3)
        // Then we have several intersections... treat this a no intersection at all.
        if( Float.isInfinite(det_inv) )
            return null;

        // use Kramers rule to compute xi and yi
        float px = ((b1 * c2 - b2 * c1) * det_inv);
        float py = ((a2 * c1 - a1 * c2) * det_inv);

        return new Point(px, py);
    }

    /**
     * Compute equation for a point on the nice line formula.
     *
     * @param point
     * @return
     */
    private float calculateEquation(Point point)
    {
        return a * (point.x - xm) + b * (point.y - ym);
    }


    public void draw(Canvas c, Paint p) {
        p.setColor(Color.WHITE);
        p.setStrokeWidth(5);
        c.drawLine(PointA.getX(), PointA.getY(), PointB.getX(), PointB.getY(), p);
    }
}
