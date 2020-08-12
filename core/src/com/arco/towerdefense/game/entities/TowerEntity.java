package com.arco.towerdefense.game.entities;

import com.arco.towerdefense.game.GameSingleton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;


public class TowerEntity extends Entity {
    private int damage;
    private float timeSinceLastShoot;
    private float firingSpeed;
    private ArrayList<BulletEntity> bullets;
    private int id;
    private float range;
    private Vector2 centerTower;
    private int price;
    private int upgradeTowerId;
    private int upgradeTowerPrice;
    private String bulletAtlasPath;
    private boolean shouldRotateBullet;

    private float PERCENTAGE_LOSS_ON_SELL = 0.2f;

    public TowerEntity(String texturePath, int gridX, int gridY) {
        super(new Sprite(GameSingleton.getInstance().getTexture(texturePath)), gridX, gridY);
        super.setSpriteSizeToScale();

        this.bullets = new ArrayList<>();

        centerTower = new Vector2();

    }

    public void setPrice(int price) { this.price = price; }

    public void setRange(float range) {
        this.range = range * scale;
    }

    public void setUpgradeTowerId(int upgradeTowerId) {
        this.upgradeTowerId = upgradeTowerId;
    }

    public int getUpgradeTowerId() {
        return upgradeTowerId;
    }

    public int getUpgradeTowerPrice() {
        return upgradeTowerPrice;
    }

    public void setUpgradeTowerPrice(int upgradeTowerPrice) {
        this.upgradeTowerPrice = upgradeTowerPrice;
    }

    public boolean isUpgradable() {
        return this.upgradeTowerId != -1;
    }

    public int getSellPrice() {
        return (int) (this.price * (1 - this.PERCENTAGE_LOSS_ON_SELL));
    }

    public Vector2 getCenterTower() {
        centerTower.x = getScaledX() + getWidth()/2;
        centerTower.y = getScaledY() + getHeight()/2;

        return centerTower;
    }

    public Circle getCircleRange() {
        return new Circle(getCenterTower(), range);
    }

    private void shoot(EnemyEntity enemyTarget) {
        timeSinceLastShoot = 0;
        bullets.add(new BulletEntity(bulletAtlasPath, shouldRotateBullet, gridX, gridY, 10, damage, enemyTarget));

    }

    public void update(float delta, ArrayList<EnemyEntity> enemies) {
        timeSinceLastShoot += delta;
        if (canShoot()) {
            EnemyEntity enemyTarget = this.getEnemyInRange(enemies);
            if (enemyTarget != null) {
                shoot(enemyTarget);
            }
        }

        updateBullets(delta);
    }

    private boolean canShoot() {
        return timeSinceLastShoot >= firingSpeed;
    }

    public void updateBullets(float delta) {
        ArrayList<BulletEntity> bulletsToRemove = new ArrayList<>();

        for (BulletEntity bullet : bullets) {

        }
        Iterator<BulletEntity> it = bullets.iterator();
        while (it.hasNext()) {
            BulletEntity bullet = it.next();
            bullet.update(delta);
            if (bullet.shouldRemove()) {
                it.remove();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);

        for (BulletEntity bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public void setTexture(String texturePath) {
        // If this throw an error means that we have not loaded the texture in our AssetManager.
        this.sprite.setTexture(GameSingleton.getInstance().getTexture(texturePath));
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setFiringSpeed(float firingSpeed) {
        this.firingSpeed = firingSpeed;
        this.timeSinceLastShoot = firingSpeed; // So that the first shot is immediate
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBulletAtlasPath(String bulletAtlasPath) {
        this.bulletAtlasPath = bulletAtlasPath;
    }

    public void setShouldRotateBullet(boolean shouldRotateBullet) {
        this.shouldRotateBullet = shouldRotateBullet;
    }

    public EnemyEntity getEnemyInRange(ArrayList<EnemyEntity> enemies) {
        for (EnemyEntity enemy: enemies) {
            if (Intersector.overlaps(this.getCircleRange(), enemy.getEntityRect())) {
                return enemy;
            }
        }

        return null; // We could not find any
    }

    public int getPrice() {
        return price;
    }
}
