
package com.arco.towerdefense.game.controllers;

import com.arco.towerdefense.game.drawer.GroundDrawer;
import com.arco.towerdefense.game.entities.TowerEntity;
import com.arco.towerdefense.game.entities.Wave;
import com.arco.towerdefense.game.entities.WaveManager;
import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.path.Path;
import com.arco.towerdefense.game.utils.Utils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class GroundController extends InputAdapter {
    private GroundDrawer groundDrawer;
    private ArrayList<TowerEntity> towers;
    private Path path;

    private Rectangle viewRectangle;

    private boolean hasSelectedTower;
    private TowerEntity towerEntityHolder;

    private WaveManager waveManager;
    private Wave wave;

    public GroundController(SpriteBatch batch,  int gridBlockSize, int viewWidth, int viewHeight) {

        path = new Path();
        path.setCheckPoints();

        viewRectangle = new Rectangle(0, 0, viewWidth, viewHeight);
        groundDrawer = new GroundDrawer(batch, gridBlockSize, viewRectangle, path.getLanes());

        towers = new ArrayList<>();

        waveManager = new WaveManager(path.getCheckPoints());

        towerEntityHolder = null;
        hasSelectedTower = false;
    }

    private boolean existsTowerAt(float x, float y) {
        for (TowerEntity tower: towers) {
            if (tower.getX() == x && tower.getY() == y) return true;
        }

        return false;
    }

    private void addTower(int x, int y) {
        if (towerEntityHolder == null) return;

        towerEntityHolder.setX(x);
        towerEntityHolder.setY(y);

        towers.add(towerEntityHolder);

        groundDrawer.removeScheduleOfGroundSelection();
        hasSelectedTower = false;
        towerEntityHolder = null;
    }

    //update call in game screen (call all the update methods to run the game)
    public void update(float delta) {
        groundDrawer.drawGround();
        updateTowers(delta);
        waveManager.update(delta);
        groundDrawer.drawTowers(towers);
        groundDrawer.drawEnemies(waveManager.getEnemiesList());
        groundDrawer.drawScheduledItems();
        drawSelectedTowerUnderCursor();
    }

    //update tower and bullets movements
    private void updateTowers(float delta) {
        for(TowerEntity tower : towers) {
            tower.update(delta);
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Consts.V_HEIGHT - screenY;
        if (Utils.isInsideRectangle(viewRectangle, screenX, screenY) && button == Input.Buttons.LEFT) {
            int gridX = screenX / groundDrawer.getScale();
            int gridY = screenY / groundDrawer.getScale();

            if (!existsTowerAt(gridX, gridY)) {
                addTower(gridX, gridY);
            }

            return true;
        }

        return false; // Meaning that we have not handled the touch
    }

    public void setHasSelectedTower(boolean hasSelectedTower) {
        this.hasSelectedTower = hasSelectedTower;
    }

    public void setTowerEntityHolder(TowerEntity towerEntity) {
        this.towerEntityHolder = towerEntity;

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        screenY = Consts.V_HEIGHT - screenY;

        if (Utils.isInsideRectangle(viewRectangle, screenX, screenY) && hasSelectedTower) {
            int gridX = screenX / groundDrawer.getScale();
            int gridY = screenY / groundDrawer.getScale();

            groundDrawer.setPositionSelectedTower(screenX, screenY);
            groundDrawer.scheduleDrawGroundSelectionAt(gridX , gridY);

            return true;
        }

        return false;
    }


    public void setSelectedTowerSprite(Texture texture) {
        groundDrawer.setSelectedTowerSprite(texture);
    }

    private void drawSelectedTowerUnderCursor() {
        if (hasSelectedTower) {
            groundDrawer.drawSelectedTowerUnderCursor();
        }
    }

    public void dispose() {
        this.groundDrawer.dispose();
    }
}