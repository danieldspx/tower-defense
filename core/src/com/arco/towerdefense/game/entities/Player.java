package com.arco.towerdefense.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private Texture txt;
    private int x;
    private int y;
    private int width;
    private int height;

    public Player(int x, int y, int width, int height) {
        txt = new Texture("dirt.png");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update() {

    }

    public void draw(SpriteBatch batch) {
        batch.draw(txt, x, y, width, height);
    }

    public void dispose() {
        txt.dispose();
    }
}
