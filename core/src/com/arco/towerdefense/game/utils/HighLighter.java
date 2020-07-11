package com.arco.towerdefense.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class HighLighter {
    private Texture texture;
    private TextureRegion region;
    private ShapeDrawer shapeDrawer;

    private int x;
    private int y;
    private int width;
    private int height;

    public HighLighter() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        texture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        region = new TextureRegion(texture, 0, 0, 1, 1);
    }

    public void setShapeDrawer(SpriteBatch batch) {
        shapeDrawer = new ShapeDrawer(batch, region);
    }

    public void draw() {
        shapeDrawer.setColor(Color.RED);
        shapeDrawer.rectangle(x, y, width, height);
    }

    public void update(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void dispose() {
        texture.dispose();
    }
}
