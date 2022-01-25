package com.mygdx.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class SamuraiComponent implements Component, Pool.Poolable {

    public Entity particleEffect;

    @Override
    public void reset() {
        particleEffect = null;
    }
}

