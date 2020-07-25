package com.arco.towerdefense.game.entities;

import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.arco.towerdefense.game.utils.Consts.V_WIDTH;

public class BulletEntity extends Entity {
    private float speed;
    private int damage;
    private boolean shouldRemove;
    private EnemyEntity target;

    public BulletEntity(String txt, float x, float y, float speed, int damage, EnemyEntity target) {
        super(new Texture(txt), x, y);
        this.speed = speed;
        this.damage = damage;
        this.shouldRemove = false;
        this.target = target;
    }

    public void update(float delta, float scale) {
        x += delta * speed;

        if(!Utils.isInsideScreen(x*scale, y*scale)) {
            shouldRemove = true;
        }
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void draw(SpriteBatch batch, int scale) {
        batch.draw(txt, x*scale, y*scale, scale, scale);
    }

    public void dispose() {
        this.getTexture().dispose();
    }
}
