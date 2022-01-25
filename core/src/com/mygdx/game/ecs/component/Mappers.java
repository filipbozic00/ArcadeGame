package com.mygdx.game.ecs.component;

import com.badlogic.ashley.core.ComponentMapper;

public final class Mappers {
    public static final ComponentMapper<BoundsComponent> BOUNDS =
            ComponentMapper.getFor(BoundsComponent.class);

    public static final ComponentMapper<DimensionComponent> DIMENSION =
            ComponentMapper.getFor(DimensionComponent.class);

    public static final ComponentMapper<PositionComponent> POSITION =
            ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<MovementComponentXYR> MOVEMENT =
            ComponentMapper.getFor(MovementComponentXYR.class);

    public static final ComponentMapper<TextureComponent> TEXTURE =
            ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<ZOrderComponent> Z_ORDER =
            ComponentMapper.getFor(ZOrderComponent.class);

    public static final ComponentMapper<SamuraiComponent> SAMURAI_COMPONENT_COMPONENT_MAPPER =
            ComponentMapper.getFor(SamuraiComponent.class);

    public static final ComponentMapper<NinjaComponent> NINJA_COMPONENT_COMPONENT_MAPPER =
            ComponentMapper.getFor(NinjaComponent.class);

    public static final ComponentMapper<ShurikenComponent> SHURIKEN_COMPONENT_COMPONENT_MAPPER =
            ComponentMapper.getFor(ShurikenComponent.class);


    public static final ComponentMapper<KatanaComponent> KATANA_COMPONENT_COMPONENT_MAPPER =
            ComponentMapper.getFor(KatanaComponent.class);

    public static final ComponentMapper<ParticleComponent> FLAME_COMPONENT_COMPONENT_MAPPER =
            ComponentMapper.getFor(ParticleComponent.class);


    public static final ComponentMapper<ParticleComponent> peCom = ComponentMapper.getFor(ParticleComponent.class);

    private Mappers() {
    }

}
