package com.arco.towerdefense.game.factories;

import com.arco.towerdefense.game.GameSingleton;
import com.arco.towerdefense.game.entities.EnemyEntity;
import com.arco.towerdefense.game.entities.TowerEntity;
import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.Utils;
import com.arco.towerdefense.game.utils.json.EnemyJson;
import com.arco.towerdefense.game.utils.json.TowerJson;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnemyFactory {
    private ArrayList<EnemyJson> enemiesJson;
    private static AtomicLong idCounter = new AtomicLong();

    public EnemyFactory() {
        Json json = new Json();
        enemiesJson = json.fromJson(ArrayList.class, EnemyJson.class, Gdx.files.internal(Consts.ENEMIES_JSON));
    }

    public EnemyEntity createById(int id) {
        for (EnemyJson enemyJson: enemiesJson) {
            if (enemyJson.id == id) {
                return create(enemyJson);
            }
        }
        return null;
    }

    public EnemyEntity create(EnemyJson enemyJson) {
        EnemyEntity enemyEntity = new EnemyEntity(enemyJson.id, enemyJson.speed, enemyJson.health, enemyJson.skinPath, createID(), enemyJson.reward);

        return enemyEntity;
    }

    public static String createID()
    {
        return String.valueOf(idCounter.getAndIncrement());
    }

}
