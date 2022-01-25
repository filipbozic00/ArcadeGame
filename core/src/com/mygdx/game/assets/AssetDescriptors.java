package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {
    public static final AssetDescriptor<BitmapFont> FONT32 =
            new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT32, BitmapFont.class);

    public static final AssetDescriptor<TextureAtlas> GAME_PLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY, TextureAtlas.class);

    public static final AssetDescriptor<Sound> SOUND1 =
            new AssetDescriptor<Sound>(AssetPaths.SOUND1, Sound.class);

    public static final AssetDescriptor<Sound> SOUND2 =
            new AssetDescriptor<Sound>(AssetPaths.SOUND2, Sound.class);

    public static final AssetDescriptor<Sound> SOUND3 =
            new AssetDescriptor<Sound>(AssetPaths.SOUND3, Sound.class);


    private AssetDescriptors() {
    }
}
