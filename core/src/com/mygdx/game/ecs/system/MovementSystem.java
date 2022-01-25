package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.ecs.component.Mappers;
import com.mygdx.game.ecs.component.MovementComponentXYR;
import com.mygdx.game.ecs.component.PositionComponent;

public class MovementSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            MovementComponentXYR.class
    ).get();

    public MovementSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        MovementComponentXYR movement = Mappers.MOVEMENT.get(entity);

        position.x += movement.xSpeed;
        position.y += movement.ySpeed;
        position.r += movement.rSpeed;
    }
}
