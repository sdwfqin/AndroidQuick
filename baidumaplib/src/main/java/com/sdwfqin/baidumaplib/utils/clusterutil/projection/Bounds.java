
package com.sdwfqin.baidumaplib.utils.clusterutil.projection;

/**
 * Represents an area in the cartesian plane.
 */
public class Bounds {
    public final double minX;
    public final double minY;

    public final double maxX;
    public final double maxY;

    public final double midX;
    public final double midY;

    public Bounds(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        midX = (minX + maxX) / 2;
        midY = (minY + maxY) / 2;
    }

    public boolean contains(double x, double y) {
        return minX <= x && x <= maxX && minY <= y && y <= maxY;
    }

    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    public boolean contains(Bounds bounds) {
        return bounds.minX >= minX && bounds.maxX <= maxX && bounds.minY >= minY && bounds.maxY <= maxY;
    }

    public boolean intersects(double minX, double maxX, double minY, double maxY) {
        return minX < this.maxX && this.minX < maxX && minY < this.maxY && this.minY < maxY;
    }

    public boolean intersects(Bounds bounds) {
        return intersects(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY);
    }

}