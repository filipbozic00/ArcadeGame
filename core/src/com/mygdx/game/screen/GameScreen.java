package com.mygdx.game.screen;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.ecs.system.BoundsSystem;
import com.mygdx.game.ecs.system.KatanaSpawnSystem;
import com.mygdx.game.ecs.system.CleanUpSystem;
import com.mygdx.game.ecs.system.CollisionSystem;
import com.mygdx.game.ecs.system.NinjaSpawnSystem;
import com.mygdx.game.ecs.system.ShurikenSpawnSystem;
import com.mygdx.game.ecs.system.HudRenderSystem;
import com.mygdx.game.ecs.system.SamuraiInputSystem;
import com.mygdx.game.ecs.system.MovementSystem;
import com.mygdx.game.ecs.system.RenderSystem;
import com.mygdx.game.ecs.system.WorldWrapSystem;
import com.mygdx.game.ecs.system.debug.DebugCameraSystem;
import com.mygdx.game.ecs.system.debug.DebugInputSystem;
import com.mygdx.game.ecs.system.debug.DebugRenderSystem;
import com.mygdx.game.ecs.system.debug.GridRenderSystem;
import com.mygdx.game.ecs.system.passive.EntityFactorySystem;
import com.mygdx.game.ecs.system.passive.SoundSystem;
import com.mygdx.game.ecs.system.passive.StartUpSystem;

/**
 * Artwork from https://goodstuffnononsense.com/about/
 * https://goodstuffnononsense.com/hand-drawn-icons/space-icons/
 */
public class GameScreen extends ScreenAdapter {

    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);


    private final AssetManager assetManager;
    private final SpriteBatch batch;
//    private final AstronautGame game;
    private final Texture background;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport hudViewport;
    private ShapeRenderer renderer;
    private PooledEngine engine;
    private BitmapFont font;
    private boolean debug = true;

    public GameScreen(MyGdxGame game) {
//        this.game = game;
        assetManager = game.getAssetManager();
        batch = game.getBatch();
        background = new Texture(Gdx.files.internal("C:\\Users\\filip\\AndroidStudioProjects\\ecs2\\desktop\\src\\assets-raw\\gameplay\\owlboy3.jpg"));

    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, camera);
        hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer = new ShapeRenderer();
        font = assetManager.get(AssetDescriptors.FONT32);
       // batch.setProjectionMatrix(viewport.getCamera().combined);
        //batch.draw(background, 0, 0 , GameConfig.WIDTH, GameConfig.HEIGHT);


        engine = new PooledEngine();

        // passive systems
        engine.addSystem(new EntityFactorySystem(assetManager));
        engine.addSystem(new SoundSystem(assetManager));
        engine.addSystem(new StartUpSystem());  // called only at the start, to generate first entities

        engine.addSystem(new SamuraiInputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new WorldWrapSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new ShurikenSpawnSystem());
        engine.addSystem(new NinjaSpawnSystem());
        engine.addSystem(new KatanaSpawnSystem());
        engine.addSystem(new CleanUpSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new RenderSystem(batch, viewport));
        engine.addSystem(new HudRenderSystem(batch, hudViewport, font));

        // debug systems
        if (debug) {
            engine.addSystem(new GridRenderSystem(viewport, renderer));
            engine.addSystem(new DebugRenderSystem(viewport, renderer));
            engine.addSystem(new DebugCameraSystem(
                    GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2,
                    camera
            ));
        }
        engine.addSystem(new DebugInputSystem());

        GameManager.INSTANCE.resetResult();
        logAllSystemsInEngine();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) GameManager.INSTANCE.resetResult();

        ScreenUtils.clear(Color.BLACK);

        if (GameManager.INSTANCE.isGameOver()) {
            engine.update(0);
        } else {
            engine.update(delta);
        }

//        if (GameManager.INSTANCE.isGameOver()) {
//            game.setScreen(new MenuScreen(game));
//        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        engine.removeAllEntities();
        background.dispose();
    }

    public void logAllSystemsInEngine() {
        for (EntitySystem system : engine.getSystems()) {
            log.debug(system.getClass().getSimpleName());
        }
    }
}
