package inaugural.soliloquy.graphics.bootstrap;

import inaugural.soliloquy.graphics.api.dto.SpriteDefinitionDTO;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.List;
import java.util.Map;

public class GraphicsPreloaderImpl implements GraphicsPreloader {
//    private final ImageFactory IMAGE_FACTORY;
//    private final AssetFactory<FontDefinition, Font> FONT_FACTORY;
//    private final AssetFactory<SpriteDefinition, Sprite> SPRITE_FACTORY;
//    private final AssetFactory<AnimationDefinition, Animation> ANIMATION_FACTORY;
//    private final AssetFactory<ImageAssetSetDefinition, ImageAssetSet> IMAGE_ASSET_SET_FACTORY;
//
//    private final List<SpriteDefinitionDTO> spriteDefinitionDTOs;
//
//    private final Map<String, Image> IMAGES;
//    private final Map<String, Font> FONTS;

    @Override
    public void load() {
        // Assemble images info

        // Get a total count of assets needed to import

        // Load images serially

        // Load fonts serially, while loading other assets parallelly
        // As fonts are done loading, continue loading other assets as necessary
        // Use POD controller, in tandem with time in-between font batches, or after polling
        // intervals, to calculate progress per second; use POD controller to aim towards target
    }

    @Override
    public float percentageComplete() {
        return 0;
    }

    @Override
    public float percentageComplete(String component) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
