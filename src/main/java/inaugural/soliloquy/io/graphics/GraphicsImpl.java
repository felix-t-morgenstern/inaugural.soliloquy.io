package inaugural.soliloquy.io.graphics;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.assets.*;

import java.util.function.Function;

public class GraphicsImpl implements Graphics {
    private final Function<String, Image> GET_IMAGE;
    private final Function<String, Sprite> GET_SPRITE;
    private final Function<String, Animation> GET_ANIMATION;
    private final Function<String, GlobalLoopingAnimation> GET_GLOBAL_LOOPING_ANIMATION;
    private final Function<String, ImageAssetSet> GET_IMAGE_ASSET_SET;
    private final Function<String, Font> GET_FONT;

    public GraphicsImpl(
            Function<String, Image> getImage,
            Function<String, Sprite> getSprite,
            Function<String, Animation> getAnimation,
            Function<String, GlobalLoopingAnimation> getGlobalLoopingAnimation,
            Function<String, ImageAssetSet> getImageAssetSet,
            Function<String, Font> getFont
    ) {
        GET_IMAGE = Check.ifNull(getImage, "getImage");
        GET_SPRITE = Check.ifNull(getSprite, "getSprite");
        GET_ANIMATION = Check.ifNull(getAnimation, "getAnimation");
        GET_GLOBAL_LOOPING_ANIMATION = Check.ifNull(getGlobalLoopingAnimation, "getGlobalLoopingAnimation");
        GET_IMAGE_ASSET_SET = Check.ifNull(getImageAssetSet, "getImageAssetSet");
        GET_FONT = Check.ifNull(getFont, "getFont");
    }

    @Override
    public Image getImage(String relLoc) throws IllegalArgumentException {
        return GET_IMAGE.apply(relLoc);
    }

    @Override
    public Sprite getSprite(String spriteId) throws IllegalArgumentException {
        return GET_SPRITE.apply(spriteId);
    }

    @Override
    public Animation getAnimation(String animationId) throws IllegalArgumentException {
        return GET_ANIMATION.apply(animationId);
    }

    @Override
    public GlobalLoopingAnimation getGlobalLoopingAnimation(String globalLoopingAnimationId)
            throws IllegalArgumentException {
        return GET_GLOBAL_LOOPING_ANIMATION.apply(globalLoopingAnimationId);
    }

    @Override
    public ImageAssetSet getImageAssetSet(String imageAssetSetId) throws IllegalArgumentException {
        return GET_IMAGE_ASSET_SET.apply(imageAssetSetId);
    }

    @Override
    public Font getFont(String fontId) throws IllegalArgumentException {
        return GET_FONT.apply(fontId);
    }
}
