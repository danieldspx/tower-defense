package com.arco.towerdefense.game.entities;

import com.arco.towerdefense.game.GameSingleton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class EnemyEntity extends Entity {

    private enum direction{DOWN, UP, LEFT, RIGHT}
    private int id;
    private float speed;
    private Vector2 nextCheckPoint;
    private direction dir;
    public boolean alive;
    private UUID targetID;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private int width;
    private int height;

    public EnemyEntity(int id, float speed, String txt, UUID targetID, Animation<TextureRegion> animation) {
        super(GameSingleton.getInstance().getTexture(txt), 0, 0);
        this.id = id;
        this.speed = speed;
        this.alive = true;
        this.targetID = targetID;
        this.animation = animation;
        this.stateTime = 0f;
    }


    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public void update(float delta) {
        if(dir == direction.DOWN)
            y -= delta * speed;

        if(dir == direction.UP)
            y += delta * speed;

        if(dir == direction.LEFT)
            x -= delta * speed;

        if(dir == direction.RIGHT)
            x += delta * speed;
    }

    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.draw(currentFrame, getScaledX(), getScaledY(), width, height);
        // Enemy Height and Width is equal to scale then
    }

    public Rectangle getEnemyRect() {
        return new Rectangle(getScaledX(), getScaledY(), getWidth(), getHeight());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAlive() { return alive; }

    public Vector2 getNextCheckPoint() {
        return nextCheckPoint;
    }

    public void setNextCheckPoint(Vector2 nextCheckPoint) {
        this.nextCheckPoint = nextCheckPoint;
    }

    public void selectDirection() {
        if (nextCheckPoint != null) {
            if (y > nextCheckPoint.y) {
                //baixo
                dir = direction.DOWN;
            }
            if (y < nextCheckPoint.y) {
                //cima
                dir = direction.UP;
            }
            if (x > nextCheckPoint.x) {
                //esquerda
                dir = direction.LEFT;
            }
            if (x < nextCheckPoint.x) {
                //direita
                dir = direction.RIGHT;
            }
        }
    }

    public boolean isCheckPoint() {
        if(x > nextCheckPoint.x - 0.1 && x < nextCheckPoint.x + 0.1 && y > nextCheckPoint.y - 0.1 && y < nextCheckPoint.y + 0.1) {
            x = nextCheckPoint.x;
            y = nextCheckPoint.y;
            return true;
        }

        return false;
    }

    public UUID getTargetID() {
        return targetID;
    }

    public void setTargetID(UUID targetID) {
        this.targetID = targetID;
    }
}
