package inaugural.soliloquy.io.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.Map;
import java.util.function.Function;

public class ImageAssetSetFactory
        extends AbstractAssetFactory<ImageAssetSetDefinition, ImageAssetSet> {
    private final Function<String, Sprite> GET_SPRITE;
    private final Function<String, Animation> GET_ANIMATION;
    private final Function<String, GlobalLoopingAnimation> GET_GLOBAL_LOOPING_ANIMATION;

    public ImageAssetSetFactory(Function<String, Sprite> getSprite,
                                Function<String, Animation> getAnimation,
                                Function<String, GlobalLoopingAnimation> getGlobalLoopingAnimation) {
        GET_SPRITE = Check.ifNull(getSprite, "getSprite");
        GET_ANIMATION = Check.ifNull(getAnimation, "getAnimation");
        GET_GLOBAL_LOOPING_ANIMATION = Check.ifNull(getGlobalLoopingAnimation,
                "getGlobalLoopingAnimation");
    }

    @Override
    public ImageAssetSet make(ImageAssetSetDefinition definition)
            throws IllegalArgumentException {
        Check.ifNull(definition, "definition");

        Check.ifNull(definition.assetDefinitions(), "definition.assetDefinitions()");
        if (definition.assetDefinitions().isEmpty()) {
            throw new IllegalArgumentException(
                    "ImageAssetSetFactory.create: definition.assetDefinitions() cannot be empty");
        }

        Check.ifNullOrEmpty(definition.id(), "definition.id()");

        // ImageAssetSets default to supporting mouse event capturing, but if any of the
        // constituent ImageAssets do not, then the entire ImageAssetSet does not.
        var supportsMouseEventCapturing = true;

        var assetsByDisplayParams = Collections.<Map<String, String>, ImageAsset>mapOf();

        for (var assetDefinition : definition.assetDefinitions()) {
            Check.ifNullOrEmpty(assetDefinition.ASSET_ID, "assetDefinition.ASSET_ID");

            var displayParams = Collections.<String, String>mapOf();
            for (var displayParam : assetDefinition.DISPLAY_PARAMS) {
                displayParams.put(displayParam.NAME, displayParam.VAL);
            }

            ImageAsset imageAsset;
            switch (assetDefinition.ASSET_TYPE) {
                case SPRITE -> {
                    imageAsset = GET_SPRITE.apply(assetDefinition.ASSET_ID);
                    if (imageAsset == null) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no Sprite found with id (" +
                                        assetDefinition.ASSET_ID + ")");
                    }
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((Sprite) imageAsset).image().supportsMouseEventCapturing();
                    }
                }
                case ANIMATION -> {
                    imageAsset = GET_ANIMATION.apply(assetDefinition.ASSET_ID);
                    if (imageAsset == null) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no Animation found with id (" +
                                        assetDefinition.ASSET_ID + ")");
                    }
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((Animation) imageAsset).supportsMouseEventCapturing();
                    }
                }
                case GLOBAL_LOOPING_ANIMATION -> {
                    imageAsset = GET_GLOBAL_LOOPING_ANIMATION.apply(assetDefinition.ASSET_ID);
                    if (imageAsset == null) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no GlobalLoopingAnimation found " +
                                        "with id (" + assetDefinition.ASSET_ID + ")");
                    }
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((GlobalLoopingAnimation) imageAsset).supportsMouseEvents();
                    }
                }
                default -> throw new IllegalArgumentException(
                        "ImageAssetSetFactory.make: assetDefinition has illegal assetType (" +
                                assetDefinition.ASSET_TYPE + ")");
            }

            assetsByDisplayParams.put(displayParams, imageAsset);
        }

        return new ImageAssetSetImpl(assetsByDisplayParams, definition.id(),
                supportsMouseEventCapturing);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class ImageAssetSetImpl implements ImageAssetSet {
        private final Map<Map<String, String>, ImageAsset> ASSETS_BY_DISPLAY_PARAMS;
        private final String ID;
        private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;

        public ImageAssetSetImpl(Map<Map<String, String>, ImageAsset> assetsByDisplayParams,
                                 String id, boolean supportsMouseEventCapturing) {
            ASSETS_BY_DISPLAY_PARAMS = assetsByDisplayParams;
            ID = id;
            SUPPORTS_MOUSE_EVENT_CAPTURING = supportsMouseEventCapturing;
        }

        @Override
        public ImageAsset getImageAssetWithDisplayParams(Map<String, String> displayParams)
                throws IllegalArgumentException {
            var output = ASSETS_BY_DISPLAY_PARAMS.get(Check.ifNull(displayParams, "displayParams"));
            if (output == null) {
                throw new IllegalArgumentException(
                        "ImageAssetSetFactory.ImageAssetSet.getImageAssetWithDisplayParams: no " +
                                "ImageAsset found for display params... {" + displayParams + "}");
            }
            return output;
        }

        @Override
        public boolean supportsMouseEventCapturing() {
            return SUPPORTS_MOUSE_EVENT_CAPTURING;
        }

        @Override
        public String id() throws IllegalStateException {
            return ID;
        }
    }
}
