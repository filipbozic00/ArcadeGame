package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.ecs.component.Mappers;
import com.mygdx.game.ecs.component.BoundsComponent;
import com.mygdx.game.ecs.component.KatanaComponent;
import com.mygdx.game.ecs.component.NinjaComponent;
import com.mygdx.game.ecs.component.ShurikenComponent;
import com.mygdx.game.ecs.component.SamuraiComponent;
import com.mygdx.game.ecs.system.passive.SoundSystem;

public class CollisionSystem extends EntitySystem {

    private static final Family SAMURAI_FAMILY = Family.all(SamuraiComponent.class, BoundsComponent.class).get();
    private static final Family SHURIKEN_FAMILY = Family.all(ShurikenComponent.class, BoundsComponent.class).get();
    private static final Family NINJA_FAMILY = Family.all(NinjaComponent.class, BoundsComponent.class).get();
    private static final Family KATANA_FAMILY = Family.all(KatanaComponent.class, BoundsComponent.class).get();


    private SoundSystem soundSystem;

    public CollisionSystem() {
    }

    @Override
    public void addedToEngine(Engine engine) {
        soundSystem = engine.getSystem(SoundSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (GameManager.INSTANCE.isGameOver()) return;

        ImmutableArray<Entity> samurai = getEngine().getEntitiesFor(SAMURAI_FAMILY);
        ImmutableArray<Entity> shurikens = getEngine().getEntitiesFor(SHURIKEN_FAMILY);
        ImmutableArray<Entity> ninjas = getEngine().getEntitiesFor(NINJA_FAMILY);
        ImmutableArray<Entity> katanas = getEngine().getEntitiesFor(KATANA_FAMILY);

        for (Entity samuraiEntity : samurai) {
            BoundsComponent samuraiBounds = Mappers.BOUNDS.get(samuraiEntity);

            // check collision with asteroids
            for (Entity shurikenEntity : shurikens) {
                ShurikenComponent shurikenComponent = Mappers.SHURIKEN_COMPONENT_COMPONENT_MAPPER.get(shurikenEntity);

                if (shurikenComponent.hit) {
                    continue;
                }

                BoundsComponent shurikenBounds = Mappers.BOUNDS.get(shurikenEntity);

                if (Intersector.overlaps(samuraiBounds.rectangle, shurikenBounds.rectangle)) {
                   // disk.hit = true;
                    GameManager.INSTANCE.damage();
                    soundSystem.pick();
                    getEngine().removeEntity(shurikenEntity);
                }
            }

            // check collision with astronauts
            for (Entity cupcakeEntity : ninjas) {
                NinjaComponent ninjaComponent = Mappers.NINJA_COMPONENT_COMPONENT_MAPPER.get(cupcakeEntity);

                if (ninjaComponent.hit) {
                    continue;
                }

                BoundsComponent ninjaBounds = Mappers.BOUNDS.get(cupcakeEntity);

                if (Intersector.overlaps(samuraiBounds.rectangle, ninjaBounds.rectangle)) {
                    ninjaComponent.hit = true;
                    GameManager.INSTANCE.incResult();
                    soundSystem.pick();
                    getEngine().removeEntity(cupcakeEntity);
                }
            }
            for (Entity cakeEntity : katanas) {
                KatanaComponent katanaComponent = Mappers.KATANA_COMPONENT_COMPONENT_MAPPER.get(cakeEntity);

                if (katanaComponent.hit) {
                    continue;
                }

                BoundsComponent katanaBounds = Mappers.BOUNDS.get(cakeEntity);

                if (Intersector.overlaps(samuraiBounds.rectangle, katanaBounds.rectangle)) {
                    katanaComponent.hit = true;
                    GameManager.INSTANCE.powerUp();
                    soundSystem.pick();
                    getEngine().removeEntity(cakeEntity);
                }
            }
        }
    }
}
