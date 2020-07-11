package com.arco.towerdefense.game.screens;

import com.arco.towerdefense.game.TowerDefenseGame;
import com.arco.towerdefense.game.entities.Player;
import com.arco.towerdefense.game.utils.Ground;
import com.arco.towerdefense.game.utils.HighLighter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameScreen implements Screen {
    final TowerDefenseGame game;

    public Ground ground;
    public HighLighter highLighter;

    Vector2 cursorLocation;
    ArrayList<Player> players;


    public GameScreen(TowerDefenseGame game) {
        this.game = game;

        ground = new Ground(game.V_WIDTH, game.V_HEIGHT, 16, 2);

        highLighter = new HighLighter();

        cursorLocation = new Vector2(0,0);

        players = new ArrayList<>();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    //update method with all logic game
    public void update() {
        //update cursor
        cursorLocation.x = Gdx.input.getX();
        cursorLocation.y = game.V_HEIGHT - Gdx.input.getY();

        //update mouse ground position
        for(int x = 0; x <= ground.getGridWidth(); x++) {
            for (int y = 0; y <= ground.getGridHeight(); y++) {
                int realx = x*ground.getScale();
                int realy = y*ground.getScale();
                if(cursorLocation.x > realx-ground.getScale() && cursorLocation.x < realx && cursorLocation.y > realy-ground.getScale() && cursorLocation.y < realy) {
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        players.add(new Player(realx-ground.getScale(), realy-ground.getScale(), ground.getScale(), ground.getScale()));
                    }
                    highLighter.update(realx-ground.getScale(), realy-ground.getScale(), ground.getScale(), ground.getScale());
                }
            }
        }
    }

    //draw method consquence of update actions
    public void draw() {
        //set ShapeDrawer with batch to draw
        highLighter.setShapeDrawer(game.batch);

        //clears
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //game logic draw
        game.batch.begin();

            ground.draw(game.batch);
            highLighter.draw();

            //players draw update
            for (Player player: players) {
                player.draw(game.batch);
            }

        game.batch.end();
    }


    @Override
    public void dispose() {
        ground.dispose();
        highLighter.dispose();

        //dispose all players
        for (Player player: players) {
            player.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
