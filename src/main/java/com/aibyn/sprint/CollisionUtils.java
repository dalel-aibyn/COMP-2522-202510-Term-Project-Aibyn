package com.aibyn.sprint;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public final class CollisionUtils {

    public static float distance(final float x1, final float y1, final float x2, final float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float[] findClosestPointOnRectangle(final Circle circle, final Rectangle rect) {
        float closestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        float closestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));
        return new float[] { closestX, closestY };
    }

    public static boolean circleRectangleCollision(final Circle circle, final Rectangle rect) {
        float[] closest = findClosestPointOnRectangle(circle, rect);
        float closestX = closest[0];
        float closestY = closest[1];

        float distanceX = circle.x - closestX;
        float distanceY = circle.y - closestY;
        float distanceSquared = distanceX * distanceX + distanceY * distanceY;

        return distanceSquared < (circle.radius * circle.radius);
    }
}