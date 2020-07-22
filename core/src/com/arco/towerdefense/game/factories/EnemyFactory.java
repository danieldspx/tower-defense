package com.arco.towerdefense.game.factories;

import com.arco.towerdefense.game.entities.EnemyEntity;
import com.arco.towerdefense.game.entities.TowerEntity;
import com.arco.towerdefense.game.utils.Consts;
import com.arco.towerdefense.game.utils.json.EnemyJson;
import com.arco.towerdefense.game.utils.json.TowerJson;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.UUID;

public class EnemyFactory {
    ArrayList<EnemyJson> enemiesJson;

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
        UUID targetID = UUID.randomUUID();
        EnemyEntity enemyEntity = new EnemyEntity(enemyJson.id, enemyJson.speed, enemyJson.skinPath, targetID);

        return enemyEntity;
    }
}
