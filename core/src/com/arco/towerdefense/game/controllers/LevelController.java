package com.arco.towerdefense.game.controllers;

import com.arco.towerdefense.game.utils.json.LevelJson;
import com.arco.towerdefense.game.utils.json.Wave;
import com.arco.towerdefense.game.utils.path.CheckPoint;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class LevelController {
    private int id;
    private float dificulty;
    private int initMoney;
    private ArrayList<WaveController> waves;
    private WaveController currentWave;
    private ArrayList<Vector2> checkPoints;
    public boolean completed = false;

    public LevelController(LevelJson levelJson, ArrayList<WaveController> waves) {
        this.id = levelJson.id;
        this.dificulty = levelJson.dificulty;
        this.waves = waves;
        this.initMoney = levelJson.money;
        this.currentWave = null;
        this.checkPoints = levelJson.checkPoints;
    }

    public ArrayList<Vector2> getCheckPoints() {
        return checkPoints;
    }

    public WaveController getCurrentWave() {
        return currentWave;
    }

    public int getMoney() {
        if(currentWave== null)
            return 0;

        return currentWave.getMoney();
    }

    public void setMoney(int money) { currentWave.setMoney(money); }

    public int getHearts() {
        if(currentWave== null)
            return 0;
        return currentWave.getHearts();
    }

    private WaveController getNextWave(WaveController wave) {
        int next = waves.indexOf(wave) + 1;

        if(waves.get(next) == null)
            return null;

        return waves.get(next);
    }

    private WaveController getFirstWave() {
        return this.waves.get(0);
    }
    
    public void update(float delta) {
        updateWaves(delta);
    }

    private void updateWaves(float delta) {
        if(currentWave == null)
            newWave();

        if (!currentWave.completed) {
            currentWave.update(delta);
        } else {
            if(currentWave.getId() < waves.size())
                newWave();
        }

        if(currentWave.getId() == waves.size() && currentWave.completed) {
            completed = true;
            System.out.println("PARABENS VC CONCLUIU O LEVEL");
        }
    }


    private void newWave() {
        if(currentWave == null) {
            currentWave = getFirstWave();
            currentWave.setHearts(10);
            currentWave.setInitMoney(initMoney);
        }

        else {
            int hearts = currentWave.getHearts();
            int money = currentWave.getMoney();
            currentWave = waves.get(currentWave.getId());
            currentWave.setHearts(hearts);
            currentWave.setInitMoney(money);
        }

        System.out.println("COMEÇANDO A WAVE: " + currentWave.getId());

    }

    public boolean gameOver() { return currentWave.gameOver; }

    public int getWaveID() {
        return currentWave.getId();
    }



}
