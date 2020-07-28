package com.arco.towerdefense.game.utils;

import com.arco.towerdefense.game.GameSingleton;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Utils {
    private static Vector2 utilVector = new Vector2();
    private static Rectangle utilRectangle = new Rectangle();

    public static boolean isCursorInside(float x, float y, float width, float height) {
        utilRectangle.setX(x);
        utilRectangle.setY(y);
        utilRectangle.setWidth(width);
        utilRectangle.setHeight(height);
        return utilRectangle.contains(GameSingleton.getInstance().getCursorLocation());
    }

    public static boolean isCursorInside(Sprite sprite) {
        return isCursorInside(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public static boolean isInsideRectangle(Rectangle rect, float x, float y) {
        utilVector.x = x;
        utilVector.y = y;
        return rect.contains(utilVector);
    }

    public static float getScreenCenterX() {
        return Consts.V_WIDTH / 2;
    }

    public static float getScreenCenterY() {
        return Consts.V_HEIGHT / 2;
    }

    public static boolean isInsideScreen(float x, float y) {
        utilRectangle.setX(0);
        utilRectangle.setY(0);
        utilRectangle.setWidth(Consts.V_WIDTH);
        utilRectangle.setHeight(Consts.V_HEIGHT);

        return isInsideRectangle(utilRectangle, x, y);
    }

    public static Vector2 returnNextV2FromList(ArrayList<Vector2> array, Vector2 current) {
        int next = array.indexOf(current) + 1;

        if(array.get(next) == null)
            return null;

        return array.get(next);
    }

    public static Vector2 returnFirstV2FromList(ArrayList<Vector2> array) {
        return array.get(0);
    }

    public static Vector2 returnLastV2FromList(ArrayList<Vector2> array) {
        int last = array.size() -1;

        return array.get(last);
    }
}
