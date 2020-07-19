package com.arco.towerdefense.game.screens;

import com.arco.towerdefense.game.GameSingleton;
import com.arco.towerdefense.game.controllers.GroundController;
import com.arco.towerdefense.game.TowerDefenseGame;
import com.arco.towerdefense.game.controllers.InteractionZoneController;
import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;

public class GameScreen implements Screen {
    final TowerDefenseGame game;
    private GroundController groundController;
    private InteractionZoneController interactionZoneController;
    private Texture homeButton;

    public GameScreen(TowerDefenseGame game) {
        this.game = game;

        groundController = new GroundController(game.batch,2, Consts.V_WIDTH, Consts.V_HEIGHT);
        interactionZoneController = new InteractionZoneController(game.batch, groundController);

        homeButton = GameSingleton.getInstance().getTexture(Consts.HOME_BUTTON);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
            groundController.update(delta);
            interactionZoneController.update();
            homeButtonUpdate();
        game.batch.end();
    }

    public void homeButtonUpdate() {
        game.batch.draw(homeButton, Consts.V_WIDTH - 32, Consts.V_HEIGHT - 32, 32, 32);

        if(Utils.isCursorInside(Consts.V_WIDTH - 32, Consts.V_HEIGHT - 32, 32, 32)) {
            if (Gdx.input.isTouched()) {
                game.setScreen(game.menuScreen);
            }
        }
    }

    @Override
    public void dispose() {
        groundController.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(interactionZoneController);
        multiplexer.addProcessor(groundController);
        GameSingleton.getInstance().setInputProcessor(Gdx.input.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(GameSingleton.getInstance().getInputProcessor());
    }
}
