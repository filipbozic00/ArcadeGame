package com.mygdx.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.AssetPaths;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.ecs.component.BoundsComponent;
import com.mygdx.game.ecs.component.CleanUpComponent;
import com.mygdx.game.ecs.component.DimensionComponent;
import com.mygdx.game.ecs.component.KatanaComponent;
import com.mygdx.game.ecs.component.MovementComponentXYR;
import com.mygdx.game.ecs.component.NinjaComponent;
import com.mygdx.game.ecs.component.ParticleComponent;
import com.mygdx.game.ecs.component.PositionComponent;
import com.mygdx.game.ecs.component.SamuraiComponent;
import com.mygdx.game.ecs.component.ShurikenComponent;
import com.mygdx.game.ecs.component.TextureComponent;
import com.mygdx.game.ecs.component.WorldWrapComponent;
import com.mygdx.game.ecs.component.ZOrderComponent;


public class EntityFactorySystem extends EntitySystem {

  private static final int BCK_Z_ORDER = 1;
  private static final int SHURIKEN_Z_ORDER = 2;
  private static final int NINJA_Z_ORDER = 3;
  private static final int KATANA_Z_ORDER = 4;
  private static final int SAMURAI_Z_ORDER = 5;

  private final AssetManager assetManager;

  private PooledEngine engine;
  private TextureAtlas gamePlayAtlas;

  public EntityFactorySystem(AssetManager assetManager) {
    this.assetManager = assetManager;
    setProcessing(false);   // passive system
    init();
  }

  private void init() {
    gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
  }

  @Override
  public void addedToEngine(Engine engine) {
    this.engine = (PooledEngine) engine;
  }

  public void createBackground() {
    PositionComponent position = engine.createComponent(PositionComponent.class);
    position.x = 0;
    position.y = 0;
    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.WIDTH;
    dimension.height = GameConfig.HEIGHT;
    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.BCK);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = BCK_Z_ORDER;

    ParticleComponent particle = engine.createComponent(ParticleComponent.class);
    particle.particleEffect = null;

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(texture);
    entity.add(particle);
    entity.add(zOrder);
    engine.addEntity(entity);
  }

  public void createSamurai() {
    PositionComponent position = engine.createComponent(PositionComponent.class);
    position.x = GameConfig.WIDTH / 2f - GameConfig.SAMURAI_WIDTH / 2f;
    position.y = 20;

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.SAMURAI_WIDTH;
    dimension.height = GameConfig.SAMURAI_HEIGHT;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    MovementComponentXYR movement = engine.createComponent(MovementComponentXYR.class);

    SamuraiComponent samuraiComponent = engine.createComponent(SamuraiComponent.class);

    WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.SAMURAI);

    ParticleComponent particle = engine.createComponent(ParticleComponent.class);
    particle.particleEffect = new ParticleEffect();
    particle.particleEffect.load(Gdx.files.internal(AssetPaths.PARTICLE_RED), Gdx.files.internal(("")));
    particle.particleEffect.setPosition(
            bounds.rectangle.x + dimension.width / 2, bounds.rectangle.y
    );
    particle.particleEffect.getEmitters().first().getAngle().setHigh(270);
    particle.particleEffect.getEmitters().first().getAngle().setLow(270);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = SAMURAI_Z_ORDER;

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movement);
    entity.add(samuraiComponent);
    entity.add(worldWrap);
    entity.add(texture);
    entity.add(particle);
    entity.add(zOrder);
    engine.addEntity(entity);
  }


  public void createShuriken() {
    PositionComponent position = engine.createComponent(PositionComponent.class);

    position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.SHURIKEN_WIDTH);
    position.y = GameConfig.HEIGHT;

    MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
    movementComponent.xSpeed = -GameConfig.SHURIKEN_SPEED_X_MIN * MathUtils.random(-1f, 1f);
    movementComponent.ySpeed = -GameConfig.SHURIKEN_SPEED_X_MIN * MathUtils.random(1f, 2f);
    movementComponent.rSpeed = MathUtils.random(-1f, 1f);

    float randFactor = MathUtils.random(1f, 4f);
    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.SHURIKEN_WIDTH * randFactor;
    dimension.height = GameConfig.SHURIKEN_HEIGHT * randFactor;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    ShurikenComponent shurikenComponent = engine.createComponent(ShurikenComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.SHURIKEN);


    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = SHURIKEN_Z_ORDER;

    // WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

    CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

    ParticleComponent particle = engine.createComponent(ParticleComponent.class);
    particle.particleEffect = null;


    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movementComponent);
    entity.add(shurikenComponent);
    entity.add(texture);

    entity.add(zOrder);
    // entity.add(worldWrap);

    entity.add(cleanUp);
    entity.add(particle);
    engine.addEntity(entity);
  }

  public void createNinja() {
    PositionComponent position = engine.createComponent(PositionComponent.class);
    position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.NINJA_SIZE);
    position.y = GameConfig.HEIGHT;

    MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
    movementComponent.xSpeed = GameConfig.NINJA_SPEED_X_MIN * MathUtils.random(-0.1f, 0.1f);
    movementComponent.ySpeed = -GameConfig.NINJA_SPEED_X_MIN * MathUtils.random(1f, 2f);
    movementComponent.rSpeed = MathUtils.random(-1f, 1f);

    NinjaComponent ninjaComponent = engine.createComponent(NinjaComponent.class);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.NINJA_SIZE;
    dimension.height = GameConfig.NINJA_SIZE;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.NINJA);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = NINJA_Z_ORDER;

    CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

    ParticleComponent particle = engine.createComponent(ParticleComponent.class);
    particle.particleEffect = null;

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movementComponent);
    entity.add(ninjaComponent);
    entity.add(texture);
    entity.add(zOrder);
    entity.add(cleanUp);
    entity.add(particle);
    engine.addEntity(entity);
  }

  public void createKatana() {
    PositionComponent position = engine.createComponent(PositionComponent.class);
    position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.KATANA_SIZE);
    position.y = GameConfig.HEIGHT;

    MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
    movementComponent.xSpeed = GameConfig.SHURIKEN_SPEED_X_MIN * MathUtils.random(-0.1f, 0.1f);
    movementComponent.ySpeed = -GameConfig.SHURIKEN_SPEED_X_MIN * MathUtils.random(1f, 2f);
    movementComponent.rSpeed = MathUtils.random(-1f, 1f);

    KatanaComponent katanaComponent = engine.createComponent(KatanaComponent.class);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.KATANA_SIZE;
    dimension.height = GameConfig.KATANA_SIZE;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.KATANA);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = KATANA_Z_ORDER;

    CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

    ParticleComponent particle = engine.createComponent(ParticleComponent.class);
    particle.particleEffect = null;

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movementComponent);
    entity.add(katanaComponent);
    entity.add(texture);
    entity.add(zOrder);
    entity.add(cleanUp);
    entity.add(particle);
    engine.addEntity(entity);
  }}
