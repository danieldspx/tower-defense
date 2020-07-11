package com.arco.towerdefense.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ground {
    private Texture grassTxt;
    private Texture dirtTxt;

    private int groundWeight;
    private int groundHeight;

    private int groundSize;
    private int gridBlockSize;
    private int scale;

    public Ground(int groundWeight, int groundHeight, int groundSize, int gridBlockSize) {
        grassTxt = new Texture("grasstop.png");
        dirtTxt = new Texture("dirt.png");

        this.groundWeight = groundWeight;
        this.groundHeight = groundHeight;

        this.groundSize = groundSize;
        this.gridBlockSize = gridBlockSize;
        this.scale = groundSize*gridBlockSize;
    }

    public int getGridWidth() {
        return groundWeight / scale;
    }

    public int getGridHeight() {
        return groundHeight / scale;
    }

    public int getScale() {
        return scale;
    }

    public void draw(SpriteBatch batch) {
        paint(batch);
    }

    private void paint(SpriteBatch batch) {
        for(int x = 0; x <= this.getGridWidth(); x++) {
            for (int y = 0; y <= this.getGridHeight(); y++) {
                drawGridBlock(x, y, grassTxt, batch);
            }
        }
    }

    private void drawGridBlock(int x, int y, Texture texture, SpriteBatch batch) {
        for (int i = 0; i < gridBlockSize; i++) {
            int realX = x*scale;

            realX += i*groundSize;

            for (int j = 0; j < gridBlockSize; j++) {
                int realY = y*scale;
                realY += j*groundSize;
                batch.draw(texture, realX, realY);
            }
        }
    }

    public void dispose() {
        grassTxt.dispose();
        dirtTxt.dispose();
    }
}
