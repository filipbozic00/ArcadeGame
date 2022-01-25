package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ecs.component.Mappers;
import com.mygdx.game.ecs.component.DimensionComponent;
import com.mygdx.game.ecs.component.ParticleComponent;
import com.mygdx.game.ecs.component.PositionComponent;
import com.mygdx.game.ecs.component.TextureComponent;
import com.mygdx.game.ecs.component.ZOrderComparator;
import com.mygdx.game.ecs.component.ZOrderComponent;

public class RenderSystem extends SortedIteratingSystem {


    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            DimensionComponent.class,
            TextureComponent.class,
            ZOrderComponent.class,
            ParticleComponent.class
    ).get();

    private final SpriteBatch batch;
    private final Viewport viewport;

    public RenderSystem(SpriteBatch batch, Viewport viewport) {
        super(FAMILY, ZOrderComparator.INSTANCE);
        this.batch = batch;
        this.viewport = viewport;
    }

    @Override
    public void update(float deltaTime) {   // override to avoid calling batch.begin/end for each entity
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        super.update(deltaTime);    // calls processEntity method, which is wrapped with begin/end

        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);
        TextureComponent texture = Mappers.TEXTURE.get(entity);
        ParticleComponent particle = Mappers.FLAME_COMPONENT_COMPONENT_MAPPER.get(entity);

        if (particle.particleEffect != null) {
            particle.particleEffect.setPosition(position.x + dimension.width / 2, position.y);
            particle.particleEffect.update(Gdx.graphics.getDeltaTime());
            if (particle.particleEffect.isComplete())
                particle.particleEffect.reset();
            particle.particleEffect.draw(batch);
        }
        batch.draw(texture.region,
                position.x, position.y,
                dimension.width / 2, dimension.height / 2,
                dimension.width, dimension.height,
                1, 1,
                position.r);
    }
}

