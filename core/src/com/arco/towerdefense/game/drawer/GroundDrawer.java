package com.arco.towerdefense.game.drawer;

import com.arco.towerdefense.game.GameSingleton;
import com.arco.towerdefense.game.entities.EnemyEntity;
import com.arco.towerdefense.game.entities.TowerEntity;
import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.path.Lane;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroundDrawer{
    private  SpriteBatch batch;
    private Texture grassImg;
    private Texture laneImg;

    private int groundSize;
    private int gridBlockSize;
    private int scale;

    private Rectangle viewRectangle;
    private Texture textureShapeDrawer;
    private TextureRegion regionShapeDrawer;
    private ShapeDrawer shapeDrawer;

    private enum QueueKey { DRAW_GROUND_SELECTION };
    private Map<QueueKey, Vector2> scheduledDrawingPositions;

    private ArrayList<Lane> lanes;

    public GroundDrawer(SpriteBatch batch, int gridBlockSize, Rectangle viewRectangle, ArrayList<Lane> lanes) {
        this.batch = batch;
        grassImg = GameSingleton.getInstance().getTexture(Consts.GROUND_GRASS);
        laneImg = GameSingleton.getInstance().getTexture(Consts.GROUND_DIRT);

        groundSize = grassImg.getHeight();
        this.gridBlockSize = gridBlockSize;
        this.scale = gridBlockSize*groundSize;
        this.viewRectangle = viewRectangle;

        scheduledDrawingPositions = new HashMap<>();

        initShapeDrawer();

        this.lanes = lanes;
    }

    private void initShapeDrawer() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        textureShapeDrawer = new Texture(pixmap);
        pixmap.dispose();
        regionShapeDrawer = new TextureRegion(textureShapeDrawer, 0, 0, 1, 1);

        shapeDrawer = new ShapeDrawer(batch, regionShapeDrawer);
    }

    public void drawScheduledItems() {
        if (scheduledDrawingPositions.containsKey(QueueKey.DRAW_GROUND_SELECTION)) {
            Vector2 gridPos = scheduledDrawingPositions.get(QueueKey.DRAW_GROUND_SELECTION);
            drawGroundSelection((int) gridPos.x, (int) gridPos.y);
        }
    }

    public void drawGround() {
        batch.disableBlending();
        for(int x = 0; x <= this.getGridWidth(); x++) {
            for (int y = 0; y <= this.getGridHeight(); y++) {
                drawGridBlock(x, y, grassImg);
            }
        }

        //drawLane(0, 5, 12, 5);
        //drawLane(12, 5, 12, 2);
        //drawLane(12, 2, 12, 5);
        //drawLane(12, 2, 15, 2);

        drawLane(lanes);
        batch.enableBlending();
    }

    public void scheduleDrawGroundSelectionAt(int gridX, int gridY) {
        // This method is called by the InputProcessor indirectly
        // and this happens before the render() method baing called
        // therefore we can draw using a SpriteBatch#begin/end but it will
        // stay under all the other drawings.
        scheduledDrawingPositions.put(QueueKey.DRAW_GROUND_SELECTION, new Vector2(gridX, gridY));
    }

    public void removeScheduleOfGroundSelection() {
        scheduledDrawingPositions.remove(QueueKey.DRAW_GROUND_SELECTION);
    }

    public void drawGroundSelection(int gridX, int gridY) {
        shapeDrawer.setColor(Color.RED);
        shapeDrawer.rectangle(gridX*scale, gridY*scale, scale, scale);
    }

    public void drawTowers(ArrayList<TowerEntity> towers) {
        for(TowerEntity tower : towers) {
            tower.draw(batch, scale);
        }
    }

    public void drawEnemies(ArrayList<EnemyEntity> enemies) {
        for(EnemyEntity enemy : enemies) {
            enemy.draw(batch, scale);
        }
    }

    public int getGridWidth() {
        return Math.round(viewRectangle.width) / scale;
    }

    public int getGridHeight() {
        return Math.round(viewRectangle.height) / scale;
    }

    public int getScale() {
        return scale;
    }

    public void dispose() {
        grassImg.dispose();
        laneImg.dispose();
        textureShapeDrawer.dispose();
    }

    private void drawGridBlock(int x, int y, Texture texture) {
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

    /*
    private void drawLane(int startX, int startY, int finalX, int finalY) {
        if(startX == finalX) {
            for(int y = startY; y <= finalY; y++) {
                drawGridBlock(startX, y, laneImg);
            }
        }

        if(startY == finalY) {
            for(int x = startX; x <= finalX; x++) {
                drawGridBlock(x, startY, laneImg);
            }
        }
    }
    */

    private void drawLane(ArrayList<Lane> lanes) {
        for(Lane lane : lanes) {
            if(lane.getStartX() == lane.getFinalX()) {
                if(lane.getStartY() < lane.getFinalY()) {
                    for (int y = lane.getStartY(); y <= lane.getFinalY(); y++) {
                        drawGridBlock(lane.getStartX(), y, laneImg);
                    }
                }

                if(lane.getStartY() > lane.getFinalY()) {
                    for (int y = lane.getFinalY(); y <= lane.getStartY(); y++) {
                        drawGridBlock(lane.getStartX(), y, laneImg);
                    }
                }
            }

            if(lane.getStartY() == lane.getFinalY()) {
                if(lane.getStartX() < lane.getFinalX()) {
                    for (int x = lane.getStartX(); x <= lane.getFinalX(); x++) {
                        drawGridBlock(x, lane.getStartY(), laneImg);
                    }
                }

                if(lane.getStartX() > lane.getFinalX()) {
                    for (int x = lane.getFinalX(); x <= lane.getStartX(); x++) {
                        drawGridBlock(x, lane.getStartY(), laneImg);
                    }
                }
            }
        }


    }
}
