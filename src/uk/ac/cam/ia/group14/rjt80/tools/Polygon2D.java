package uk.ac.cam.ia.group14.rjt80.tools;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Polygon2D {
    private List<Point> ps;
    private Point centre;
    private int scale;

    public Polygon2D(List<Point> ps, Point centre, int scale) {
        sharedConstructor(ps, centre,scale);
    }

    public Polygon2D(List<Point> ps, Point centre) {
        sharedConstructor(ps, centre, 1);
    }

    public Polygon2D(List<Point> ps, int scale) {
        sharedConstructor(ps,new Point(0, 0), scale);
    }

    public Polygon2D(List<Point> ps) {
        sharedConstructor(ps, new Point(0, 0),1);
    }

    public Polygon2D(String pointsFile) throws IOException { loadPointsFromFile(pointsFile); }

    private void loadPointsFromFile(String filename) throws IOException {
        List<Point> sPs = new LinkedList<>();

        FileReader f = new FileReader(filename);
        BufferedReader b = new BufferedReader(f);
        String line;

        //File first line is scale
        int sScale = Integer.parseInt(b.readLine());

        //File second line is centre coordinates
        line = b.readLine();
        Point sCentre = new Point(Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1]));

        //All other lines are polygon edge coordinates
        while ((line = b.readLine()) != null) {
            String[] coords = line.split(",");
            for (int i = 0; i < coords.length / 2; ++i) {
                Point p = new Point(Integer.parseInt(coords[2 * i]), Integer.parseInt(coords[2 * i + 1]));
                sPs.add(p);
            }
        }

        sharedConstructor(sPs, sCentre, sScale);
    }

    private void sharedConstructor(List<Point> ps, Point centre, int scale) {
        this.ps = new ArrayList<>(ps);
        this.centre = new Point(centre);
        this.scale = scale;
    }

    public List<Point> getPs() {
        return new ArrayList<>(ps);
    }

    public void setPs(List<Point> ps) {
        this.ps = new ArrayList<>(ps);
    }

    public Point getCentre() {
        return new Point(centre);
    }

    public void setCentre(Point centre) {
        this.centre = new Point(centre);
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * @param delta : absolute change to centre
     *
     * Allows all shapes to be adjusted to fit onto a new screen
     */
    public void adjustCentre(Point delta) {this.centre = new Point(this.centre.x + delta.x, this.centre.y + delta.y);}

    /**
     * @param delta absolute change to scale factor
     *
     * Allows all shapes to be adjusted to fit onto a new screen
     */
    public void adjustScale(int delta) {this.scale += delta;}

    //

    /**
     * @param x x-coordinate
     * @return adjusted coordinate
     *
     * adjusts coordinates at use time to prevent cumulative rounding errors (esp if this was extended to floating point)
     */
    private int adjustedXCoordinate(int x) { return x * scale + centre.x; }

    /**
     * @param y y-coordinate
     * @return adjusted coordinate
     *
     * adjusts coordinates at use time to prevent cumulative rounding errors (esp if this was extended to floating point)
     */
    private int adjustedYCoordinate(int y) {
        return y * scale + centre.y;
    }

    /**
     * @return Path of polygon outline
     * Required to draw the polygon on to a swing component using graphics
     */
    public Shape drawShape() {
        GeneralPath path = new GeneralPath();
        //iterates over all points on the polygon adding them to an outline path
        for (int i = 0; i < ps.size(); i++) {
            Point2D.Double p = new Point2D.Double(adjustedXCoordinate(ps.get(i).x), adjustedYCoordinate(ps.get(i).y));
            if (i == 0) {
                path.moveTo(p.getX(), p.getY());
            } else {
                path.lineTo(p.getX(), p.getY());
            }
        }
        path.closePath();
        return path;
    }

    /**
     *
     * @param xy a direction vector
     * @return a vector normal to xy
     */
    private Point normal(Point xy) {
        return new Point(-xy.y, xy.x);
    }

    /**
     *
     * @param v1 first vector
     * @param v2 second vector
     * @return v1 - v2
     */
    private Point subtract(Point v1, Point v2) {
        return new Point(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     *
     * @param v1 first vector
     * @param v2 second vector
     * @return v1 dot v2
     */
    private int dot(Point v1, Point v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    /***
     *
     * @param x integer to find the sign of
     * @return it's sign as +1, 0, -1
     */
    private int sign(int x) {
        return Integer.signum(x);
    }

    /**
     *
     * @param u1 first vector first end coordinates
     * @param u2 first vector second end coordinates
     * @param v1 second vector first end coordinates
     * @param v2 second vector second end coordinates
     * @return whether u intersects v
     *
     */
    private boolean intersect(Point u1, Point u2, Point v1, Point v2) {
        boolean intsct = false;

        Point n = normal(subtract(u1, u2));
        int s1 = sign(dot(n, subtract(v1,u2)));
        int s2 = sign(dot(n, subtract(v2,u2)));

        if (s1 == 0 || s2 == 0 || s1 != s2) {
            n = normal(subtract(v1, v2));
            s1 = sign(dot(n, subtract(u1, v2)));
            s2 = sign(dot(n, subtract(u2, v2)));
            if (s1 == 0 || s2 == 0 || s1 != s2) {
                intsct = true;
            }
        }

        return intsct;
    }

    /**
     * @param mousePosition position of the mouse
     * @return mouse position in polygon or not
     * uses ray tracing algorithm to detect whether the user clicked within the shape
     */
    public boolean withinPolygon(Point mousePosition) {
        int intersections = 0;
        Point origin = new Point(0, 0);
        for (int i = 0; i < ps.size(); ++i) {
            int j = (i + 1) % ps.size();
            if (intersect(origin, mousePosition, new Point(adjustedXCoordinate(ps.get(i).x),adjustedYCoordinate(ps.get(i).y)), new Point(adjustedXCoordinate(ps.get(j).x),adjustedYCoordinate(ps.get(j).y)))) {
                ++intersections;
            }
        }
        return intersections % 2 == 1;
    }
}