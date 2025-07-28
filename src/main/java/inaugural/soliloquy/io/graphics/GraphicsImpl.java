package inaugural.soliloquy.io.graphics;

import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.assets.*;

import java.util.function.Function;

public class GraphicsImpl implements Graphics {
    private final Function<String, Sprite> GET_SPRITE;
    private final Function<String, Animation> GET_ANIMATION;
    private final Function<String, GlobalLoopingAnimation> GET_GLOBAL_LOOPING_ANIMATION;
    private final Function<String, ImageAssetSet> GET_IMAGE_ASSET_SET;
    private final Function<String, Font> GET_FONT;

    public GraphicsImpl(
            Function<String, Sprite> getSprite,
            Function<String, Animation> getAnimation,
            Function<String, GlobalLoopingAnimation> getGlobalLoopingAnimation,
            Function<String, ImageAssetSet> getImageAssetSet,
            Function<String, Font> getFont
    ) {
        GET_SPRITE = getSprite;
        GET_ANIMATION = getAnimation;
        GET_GLOBAL_LOOPING_ANIMATION = getGlobalLoopingAnimation;
        GET_IMAGE_ASSET_SET = getImageAssetSet;
        GET_FONT = getFont;
    }

    @Override
    public Sprite getSprite(String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Animation getAnimation(String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public GlobalLoopingAnimation getGlobalLoopingAnimation(String s)
            throws IllegalArgumentException {
        return null;
    }

    @Override
    public ImageAssetSet getImageAssetSet(String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Font getFont(String s) throws IllegalArgumentException {
        return null;
    }
}
