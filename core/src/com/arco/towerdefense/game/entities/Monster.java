package com.arco.towerdefense.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Monster {
    private int speed;
    private int health;
    private int x;
    private int y;

    Texture txt;

    public Monster(int x, int y) {
        randomSelect(x, y);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Texture getTxt() {
        return txt;
    }

    public void setTxt(Texture txt) {
        this.txt = txt;
    }

    public void randomSelect(int x, int y) {
        Random gen = new Random();

        switch(gen.nextInt(5)) {
            case 0: createMonster(10, 100, x, y, "virus/Blue_Virus.png"); //blue
                break;

            case 1: createMonster(20, 200, x, y, "virus/Green_Virus.png"); //green
                break;

            case 2: createMonster(30, 300, x,  y, "virus/Pink_Virus.png"); //pink
                break;

            case 3: createMonster(40, 400, x, y, "virus/Red_Virus.png"); //red
                break;

            case 4: createMonster(50, 500, x, y, "virus/Yellow_Virus.png"); //yellow
                break;
        }
    }

    public void createMonster(int speed, int health, int x, int y, String txt) {
        this.speed = speed;
        this.health = health;
        this.x = x;
        this.y = y;
        this.txt = new Texture(txt);
    }

    public void draw(SpriteBatch batch, int scale) {
        batch.draw(txt, x, y, scale, scale);
    }
}
