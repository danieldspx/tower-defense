package com.arco.towerdefense.game.entities;

import com.arco.towerdefense.game.GameSingleton;
import com.arco.towerdefense.game.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BulletEntity extends Entity {
    private float duration;
    private int damage;
    private boolean shouldRemove;
    private EnemyEntity target;
    private float stateTime;
    private boolean hasAnimatedSpawn;
    private boolean hasHitTarget;
    private boolean shouldRotateBullet;
    private Vector2 originPos;
    private Vector2 originPosGrid;
    private boolean completeMovement;

    private Animation<TextureAtlas.AtlasRegion> spawnAnimations;
    private Animation<TextureAtlas.AtlasRegion> shotAnimations;
    private Animation<TextureAtlas.AtlasRegion> hitAnimations;


    public BulletEntity(String animationAtlasPath, boolean shouldRotateBullet,float x, float y, float speed, int damage, EnemyEntity target) {
        super(new Sprite(), x, y);

        super.setSpriteSizeToScale();

        this.originPos = new Vector2(getScaledX(), getScaledY());
        this.originPosGrid = new Vector2(x, y);

        initAnimationVariables(animationAtlasPath);

        this.stateTime = 0f;
        this.duration = 1 / speed;
        this.damage = damage;
        this.shouldRotateBullet = shouldRotateBullet;
        this.shouldRemove = false;
        this.hasAnimatedSpawn = false;
        this.hasHitTarget = false;
        this.target = target;
    }

    private void initAnimationVariables(String animationAtlasPath) {
        TextureAtlas animationAtlas = GameSingleton.getInstance().getTextureAtlas(animationAtlasPath);

        Array<TextureAtlas.AtlasRegion> spawnRegions = animationAtlas.findRegions("spawn");
        Array<TextureAtlas.AtlasRegion> shotRegions = animationAtlas.findRegions("shot");
        Array<TextureAtlas.AtlasRegion> hitRegions = animationAtlas.findRegions("hit");

        this.spawnAnimations = spawnRegions.isEmpty() ? null : new Animation<>(0.2f / spawnRegions.size, spawnRegions);

        this.shotAnimations = shotRegions.isEmpty() ? null : new Animation<>(0.01f / shotRegions.size, shotRegions);

        this.hitAnimations = hitRegions.isEmpty() ? null : new Animation<>(0.3f/ hitRegions.size, hitRegions);
    }

    public void update(float delta) {
        stateTime += delta;

        adjustPositions();

        if (isHittingTarget() && !hasHitTarget) {
            performTargetHit();
        }

        if(!Utils.isInsideScreen(getScaledX(), getScaledY())) {
            shouldRemove = true;
        }
    }

    private void performTargetHit() {
        this.hasHitTarget = true;
        target.performHit(this.damage);
    }

    public boolean isHittingTarget() {
        return Intersector.overlaps(super.getEntityRect(), target.getEntityRect());
    }

    private void adjustPositions() {
        if (completeMovement) return;

        completeMovement = stateTime >= duration;

        float percent = completeMovement ? 1 : stateTime / duration;

        x = originPosGrid.x + (target.getX() - originPosGrid.x) * percent;
        y = originPosGrid.y + (target.getY() - originPosGrid.y) * percent;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    private void drawSpawnBullet(SpriteBatch batch) {
        if (hasAnimatedSpawn || spawnAnimations == null || spawnAnimations.isAnimationFinished(stateTime)) {
            hasAnimatedSpawn = true;
            return;
        }

        // We do not use the super.sprite because we can have spawn-animation and shot-animation at the same time, so we need
        // another one
        Sprite spawnBulletFrame = new Sprite(spawnAnimations.getKeyFrame(stateTime, false));

        spawnBulletFrame.setSize(super.scale, super.scale);
        spawnBulletFrame.setPosition(originPos.x, originPos.y);

        spawnBulletFrame.draw(batch);
    }

    private void drawBulletOrHit(SpriteBatch batch) {
        Sprite currentFrame;

        if (shotAnimations != null && !hasHitTarget) {
            currentFrame = new Sprite(shotAnimations.getKeyFrame(stateTime, true));
        } else if (hitAnimations != null && !hitAnimations.isAnimationFinished(stateTime)) {
            currentFrame = new Sprite(hitAnimations.getKeyFrame(stateTime, false));
        } else {
            shouldRemove = true;
            return;
        }

        if (shouldRotateBullet) adjustSpriteRotation(currentFrame);

        this.sprite = currentFrame;
        super.setSpriteSizeToScale();

        super.draw(batch); // This call will adjust the sprite position
    }

    private void adjustSpriteRotation(Sprite sprite) {
        if (sprite == null) return;

        // Here we kinda translate the coordinate system to the originPos so the angle is related to it
        Vector2 u = new Vector2(target.getScaledX() - originPos.x, target.getScaledY() - originPos.y);

        float degrees = u.angle();

        sprite.setRotation(degrees);
    }

    public void draw(SpriteBatch batch) {
        drawSpawnBullet(batch);
        drawBulletOrHit(batch);
    }
}
