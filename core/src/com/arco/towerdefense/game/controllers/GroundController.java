
package com.arco.towerdefense.game.controllers;

import com.arco.towerdefense.game.GameSingleton;
import com.arco.towerdefense.game.drawer.GroundDrawer;
import com.arco.towerdefense.game.entities.TowerEntity;
import com.arco.towerdefense.game.factories.TowerFactory;
import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.path.Lane;
import com.arco.towerdefense.game.utils.Utils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.Iterator;

public class GroundController extends InputAdapter {
    private GroundDrawer groundDrawer;
    private ArrayList<TowerEntity> towers;
    private ArrayList<Lane> lanes;
    private Stage stage;
    private Table towerMenu;

    private Rectangle viewRectangle;

    private boolean hasSelectedBuyTower;
    private TowerEntity towerEntityHolder;
    private LevelController levelController;
    private TowerEntity selectedTower;
    private TowerFactory towerFactory;

    private Label upgradeLabel;
    private float upgradeLabelOriginX;
    private Label sellLabel;

    public GroundController(SpriteBatch batch,  int gridBlockSize, int viewWidth, int viewHeight, LevelController levelController, OrthographicCamera camera) {
        this.levelController = levelController;
        this.stage = new Stage(new StretchViewport(Consts.V_WIDTH, Consts.V_HEIGHT, camera), batch);

        setUpLanes();
        initLabels();
        initTowerMenu();

        viewRectangle = new Rectangle(0, 0, viewWidth, viewHeight);
        groundDrawer = new GroundDrawer(batch, gridBlockSize, viewRectangle, lanes);

        towers = new ArrayList<>();
        towerFactory = new TowerFactory();

        towerEntityHolder = null;
        hasSelectedBuyTower = false;
    }



    private void initLabels() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        upgradeLabel = new Label("", labelStyle);
        sellLabel = new Label("", labelStyle);
    }

    private void initTowerMenu() {
        TextureAtlas hudAtlas = GameSingleton.getInstance().getTextureAtlas("hud/pack.atlas");
        float btnSize = 32;




        ImageButton upgradeBtn = new ImageButton(new TextureRegionDrawable(hudAtlas.findRegion("arrow_up2")));
        upgradeBtn.setSize(btnSize, btnSize);
        upgradeBtn.setPosition(0, 0, Align.bottomLeft);
        upgradeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeSelectedTower();
            }
        });

        ImageButton sellBtn = new ImageButton(new TextureRegionDrawable(hudAtlas.findRegion("sell_coin")));
        sellBtn.setSize(btnSize, btnSize);
        sellBtn.setPosition(upgradeBtn.getX() + upgradeBtn.getWidth() + 40, 0, Align.bottomLeft); // 64 is the padding
        sellBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sellSelectedTower(event);
            }
        });

        upgradeLabel.setPosition(upgradeBtn.getX(), upgradeBtn.getY() - upgradeLabel.getHeight() - 20); // 20 is the padding
        upgradeLabelOriginX = upgradeBtn.getX();
        sellLabel.setPosition(sellBtn.getX(), sellBtn.getY() - sellLabel.getHeight() - 20);

        towerMenu = new Table();
        towerMenu.setFillParent(false);
        towerMenu.setWidth(sellBtn.getX() + sellBtn.getWidth());
        towerMenu.setHeight(btnSize);
        towerMenu.addActor(upgradeBtn);
        towerMenu.addActor(upgradeLabel);
        towerMenu.addActor(sellBtn);
        towerMenu.addActor(sellLabel);
        towerMenu.setVisible(false);
        stage.addActor(towerMenu);
    }

    public int getCurrentWaveId() {
        return levelController.getWaveID();
    }

    public int getTotalWaves() {
        return levelController.getTotalWaves();
    }

    public boolean getLevelCompleted() {
        return levelController.completed;
    }

    private boolean removeTowerFromStageAndArray(TowerEntity towerRemove) {
        Iterator<TowerEntity> it = towers.iterator();
        while(it.hasNext()) {
            TowerEntity tower = it.next();
            if (tower.getGridX() == towerRemove.getGridX() && tower.getGridY() == towerRemove.getGridY()) {
                it.remove();
                return tower.remove();
            }
        }

        return false;
    }

    private void sellSelectedTower(InputEvent event) {
        if (selectedTower == null) return;

        if (selectedTower.remove()) {
            GameSingleton.getInstance().increaseMoneyBy(selectedTower.getSellPrice());

            removeTowerFromStageAndArray(selectedTower);

            resetTowerSelection();
        }
    }

    private void upgradeSelectedTower() {
        if (selectedTower == null) return;

        TowerEntity upgradeTower = towerFactory.createById(selectedTower.getUpgradeTowerId());

        if (upgradeTower != null && GameSingleton.getInstance().decreaseMoneyBy(upgradeTower.getPrice())) {
            this.towerEntityHolder = upgradeTower;
            removeTowerFromStageAndArray(selectedTower);
            addTower((int) selectedTower.getGridX(), (int) selectedTower.getGridY());
        }

        resetTowerSelection();
    }

    private void resetTowerSelection() {
        selectedTower = null;
        towerMenu.setVisible(false);
    }

    private TowerEntity getTowerAt(int x, int y) {
        for (TowerEntity tower: towers) {
            if (tower.getGridX() == x && tower.getGridY() == y) return tower;
        }

        return null;
    }

    private boolean performTowerSelectionAt(int gridX, int gridY) {
        if (hasSelectedBuyTower) return false;

        this.selectedTower = getTowerAt(gridX, gridY);

        if (selectedTower == null) return false;

        sellLabel.setText("R$ "+String.format("%01d", selectedTower.getSellPrice()));
        if (selectedTower.isUpgradable()) {
            upgradeLabel.setX(upgradeLabelOriginX);
            upgradeLabel.setText("R$ "+String.format("%01d", selectedTower.getUpgradeTowerPrice()));
        } else {
            upgradeLabel.setText("Indisponivel");
            upgradeLabel.setX(upgradeLabel.getX() - upgradeLabel.getPrefWidth()/3);
        }


        Vector2 center = selectedTower.getCenterTower();
        towerMenu.setPosition(center.x - (towerMenu.getWidth() / 2), center.y - (towerMenu.getHeight() / 2), Align.bottomLeft);
        towerMenu.setVisible(true);

        return true;
    }

    private void addTower(int gridX, int gridY) {
        if (towerEntityHolder == null) return;

        towerEntityHolder.setGridX(gridX);
        towerEntityHolder.setGridY(gridY);

        towerEntityHolder.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                performTowerSelectionAt(
                        Utils.realToGrid(event.getStageX()),
                        Utils.realToGrid(event.getStageY())
                );
            }
        });

        stage.addActor(towerEntityHolder);
        towers.add(towerEntityHolder);
        towerMenu.setZIndex(towers.size());

        groundDrawer.removeScheduleOfGroundSelection();
        hasSelectedBuyTower = false;
        towerEntityHolder = null;
    }

    private boolean isPointInsideLane(int gridX, int gridY) {
        ArrayList<Vector2> checkpoints = this.levelController.getCheckPoints();
        Iterator<Vector2> it = checkpoints.iterator();

        Vector2 point = new Vector2(gridX, gridY);

        if (!it.hasNext()) return false;

        Vector2 p1 = it.next();

        while (it.hasNext()) {
            Vector2 p2 = it.next();
            if (p1.x <= point.x && point.x <= p2.x && p1.y <= point.y && point.y <= p2.y) {
                return true;
            }
            p1 = p2;
        }

        return false;
    }

    // Update call in game screen (call all the update methods to run the game)
    public void update(float delta) {
        updateTowers(delta);
        levelController.update(delta);
        stage.act(delta);
        groundDrawer.drawGround();
        groundDrawer.drawTowerRange(selectedTower);
//        groundDrawer.drawTowers(towers);
        // Stage.draw() needs a ended batch. So e end ours and then start again
        stage.getBatch().end();
        stage.draw();
        stage.getBatch().begin();
        groundDrawer.drawEnemies(levelController.getCurrentWave().getEnemiesInGame());
        groundDrawer.drawScheduledItems();
        drawSelectedTowerUnderCursor();
    }
    //update tower and bullets movements
    private void updateTowers(float delta) {
        for(TowerEntity tower : towers) {
            tower.update(delta, levelController.getCurrentWave().getEnemiesInGame());
        }
    }

    public void selfIncludeToMultiplexer(InputMultiplexer multiplexer) {
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Consts.V_HEIGHT - screenY;

        if (Utils.isInsideRectangle(viewRectangle, screenX, screenY) && button == Input.Buttons.LEFT) {
            int gridX = Utils.realToGrid(screenX);
            int gridY = Utils.realToGrid(screenY);

            if (getTowerAt(gridX, gridY) == null && !this.isPointInsideLane(gridX, gridY)) {
                if (selectedTower != null) resetTowerSelection();

                addTower(gridX, gridY);
            }

            return true;
        }

        return false; // Meaning that we have not handled the touch
    }

    public void setHasSelectedBuyTower(boolean hasSelectedBuyTower) {
        this.hasSelectedBuyTower = hasSelectedBuyTower;

        if (hasSelectedBuyTower) {
            resetTowerSelection();
        }
    }

    public void setTowerEntityHolder(TowerEntity towerEntity) {
        this.towerEntityHolder = towerEntity;
        groundDrawer.setSelectedTowerEntity(towerEntity);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        screenY = Consts.V_HEIGHT - screenY;

        if (Utils.isInsideRectangle(viewRectangle, screenX, screenY) && hasSelectedBuyTower) {
            int gridX = screenX / groundDrawer.getScale();
            int gridY = screenY / groundDrawer.getScale();

            groundDrawer.setPositionSelectedTower(screenX, screenY);
            groundDrawer.scheduleDrawGroundSelectionAt(gridX , gridY);
            groundDrawer.setCanPlaceTower(!this.isPointInsideLane(gridX, gridY));

            return true;
        }

        return false;
    }

    public void setSelectedTowerSprite(Texture texture) {
        groundDrawer.setSelectedTowerSprite(texture);
    }

    private void drawSelectedTowerUnderCursor() {
        if (hasSelectedBuyTower) {
            groundDrawer.drawSelectedTowerUnderCursor();
        }
    }

    private void setUpLanes() {
        lanes = new ArrayList<>();

        for (int i = 0; i < levelController.getCheckPoints().size() - 1; i++) {
            Vector2 checkPoint = levelController.getCheckPoints().get(i);
            Vector2 nextPoint = levelController.getCheckPoints().get(i+1);

            lanes.add(new Lane((int) checkPoint.x, (int) checkPoint.y, (int) nextPoint.x, (int) nextPoint.y));
        }
    }

    public void dispose() {
        this.groundDrawer.dispose();
    }
}
