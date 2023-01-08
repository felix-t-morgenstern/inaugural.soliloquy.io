package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.HashMap;
import java.util.Map;

import static inaugural.soliloquy.tools.Tools.emptyIfNull;
import static inaugural.soliloquy.tools.Tools.nullIfEmpty;

public class ImageAssetSetFactory
        extends AbstractAssetFactory<ImageAssetSetDefinition, ImageAssetSet> {
    private final Registry<Sprite> SPRITES_REGISTRY;
    private final Registry<Animation> ANIMATIONS_REGISTRY;
    private final Registry<GlobalLoopingAnimation> GLOBAL_LOOPING_ANIMATIONS_REGISTRY;

    public ImageAssetSetFactory(Registry<Sprite> spritesRegistry,
                                Registry<Animation> animationsRegistry,
                                Registry<GlobalLoopingAnimation> globalLoopingAnimationsRegistry) {
        SPRITES_REGISTRY = Check.ifNull(spritesRegistry, "spritesRegistry");
        ANIMATIONS_REGISTRY = Check.ifNull(animationsRegistry, "animationsRegistry");
        GLOBAL_LOOPING_ANIMATIONS_REGISTRY = Check.ifNull(globalLoopingAnimationsRegistry,
                "globalLoopingAnimationsRegistry");
    }

    @Override
    public ImageAssetSet make(ImageAssetSetDefinition imageAssetSetDefinition)
            throws IllegalArgumentException {
        Check.ifNull(imageAssetSetDefinition, "imageAssetSetDefinition");

        Check.ifNull(imageAssetSetDefinition.assetDefinitions(),
                "imageAssetSetDefinition.assetDefinitions()");
        if (imageAssetSetDefinition.assetDefinitions().isEmpty()) {
            throw new IllegalArgumentException("ImageAssetSetFactory.create: " +
                    "imageAssetSetDefinition.assetDefinitions() cannot be empty");
        }

        Check.ifNullOrEmpty(imageAssetSetDefinition.id(), "imageAssetSetDefinition.id()");

        var assetsByTypeAndDirection = new HashMap<String, Map<Direction, ImageAsset>>();

        var supportsMouseEventCapturing = true;

        for (var assetDefinition : imageAssetSetDefinition.assetDefinitions()) {
            Check.ifNullOrEmpty(assetDefinition.assetId(), "assetDefinition.assetId()");

            var type = nullIfEmpty(assetDefinition.type());
            var direction = Direction.fromValue(assetDefinition.direction());

            ImageAsset imageAsset;
            switch (assetDefinition.assetType()) {
                case SPRITE -> {
                    if (!SPRITES_REGISTRY.contains(assetDefinition.assetId())) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no Sprite found with id (" +
                                        assetDefinition.assetId() + ")");
                    }
                    imageAsset = SPRITES_REGISTRY.get(assetDefinition.assetId());
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((Sprite) imageAsset).image().supportsMouseEventCapturing();
                    }
                }
                case ANIMATION -> {
                    if (!ANIMATIONS_REGISTRY.contains(assetDefinition.assetId())) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no Animation found with id (" +
                                        assetDefinition.assetId() + ")");
                    }
                    imageAsset = ANIMATIONS_REGISTRY.get(assetDefinition.assetId());
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((Animation) imageAsset).supportsMouseEventCapturing();
                    }
                }
                case GLOBAL_LOOPING_ANIMATION -> {
                    if (!GLOBAL_LOOPING_ANIMATIONS_REGISTRY.contains(assetDefinition.assetId())) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no GlobalLoopingAnimation found " +
                                        "with id (" + assetDefinition.assetId() + ")");
                    }
                    imageAsset = GLOBAL_LOOPING_ANIMATIONS_REGISTRY.get(assetDefinition.assetId());
                }
                case default -> throw new IllegalArgumentException(
                        "ImageAssetSetFactory.make: assetDefinition has illegal assetType (" +
                                assetDefinition.assetType().toString() + ")");
            }

            Map<Direction, ImageAsset> assetsByDirection;
            if (assetsByTypeAndDirection.containsKey(type)) {
                assetsByDirection = assetsByTypeAndDirection.get(type);
            }
            else {
                assetsByTypeAndDirection.put(type, assetsByDirection = new HashMap<>());
            }
            if (assetsByDirection.containsKey(direction)) {
                throw new IllegalArgumentException(
                        "ImageAssetSetFactory: duplicate pair of type and direction (" + type +
                                "," +
                                direction + ")");
            }
            assetsByDirection.put(direction, imageAsset);
        }

        return new ImageAssetSetImpl(assetsByTypeAndDirection, imageAssetSetDefinition.id(),
                supportsMouseEventCapturing);
    }

    @Override
    public String getInterfaceName() {
        return AssetFactory.class.getCanonicalName() + "<" +
                ImageAssetSetDefinition.class.getCanonicalName() + "," +
                ImageAssetSet.class.getCanonicalName() + ">";
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class ImageAssetSetImpl implements ImageAssetSet {
        private final Map<String, Map<Direction, ImageAsset>> ASSETS_BY_TYPE_AND_DIRECTION;
        private final String ID;
        private final boolean SUPPORTS_MOUSE_EVENT_CAPTURING;

        public ImageAssetSetImpl(Map<String, Map<Direction, ImageAsset>> assetsByTypeAndDirection,
                                 String id, boolean supportsMouseEventCapturing) {
            ASSETS_BY_TYPE_AND_DIRECTION = assetsByTypeAndDirection;
            ID = id;
            SUPPORTS_MOUSE_EVENT_CAPTURING = supportsMouseEventCapturing;
        }

        @Override
        public ImageAsset getImageAssetForTypeAndDirection(String type, Direction direction)
                throws IllegalArgumentException {
            type = emptyIfNull(type);
            return ASSETS_BY_TYPE_AND_DIRECTION.get(nullIfEmpty(type)).get(direction);
        }

        @Override
        public boolean supportsMouseEventCapturing() {
            return SUPPORTS_MOUSE_EVENT_CAPTURING;
        }

        @Override
        public String id() throws IllegalStateException {
            return ID;
        }

        @Override
        public String getInterfaceName() {
            return ImageAssetSet.class.getCanonicalName();
        }
    }
}
