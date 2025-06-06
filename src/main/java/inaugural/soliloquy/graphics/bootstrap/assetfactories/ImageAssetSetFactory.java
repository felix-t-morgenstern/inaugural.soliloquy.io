package inaugural.soliloquy.graphics.bootstrap.assetfactories;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.common.infrastructure.ImmutableMap;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.Map;

import static inaugural.soliloquy.tools.Tools.emptyIfNull;
import static inaugural.soliloquy.tools.Tools.nullIfEmpty;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class ImageAssetSetFactory
        extends AbstractAssetFactory<ImageAssetSetDefinition, ImageAssetSet> {
    private final ImmutableMap<String, Sprite> GET_SPRITE;
    private final ImmutableMap<String, Animation> GET_ANIMATION;
    private final ImmutableMap<String, GlobalLoopingAnimation> GET_GLOBAL_LOOPING_ANIMATION;

    public ImageAssetSetFactory(ImmutableMap<String, Sprite> getSprite,
                                ImmutableMap<String, Animation> getAnimation,
                                ImmutableMap<String, GlobalLoopingAnimation> getGlobalLoopingAnimation) {
        GET_SPRITE = Check.ifNull(getSprite, "getSprite");
        GET_ANIMATION = Check.ifNull(getAnimation, "getAnimation");
        GET_GLOBAL_LOOPING_ANIMATION = Check.ifNull(getGlobalLoopingAnimation,
                "getGlobalLoopingAnimation");
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

        var assetsByTypeAndDirection = Collections.<String, Map<Direction, ImageAsset>>mapOf();

        var supportsMouseEventCapturing = true;

        for (var assetDefinition : imageAssetSetDefinition.assetDefinitions()) {
            Check.ifNullOrEmpty(assetDefinition.assetId(), "assetDefinition.assetId()");

            var type = nullIfEmpty(assetDefinition.type());
            var direction = Direction.fromValue(assetDefinition.direction());

            ImageAsset imageAsset;
            switch (assetDefinition.assetType()) {
                case SPRITE -> {
                    if (!GET_SPRITE.containsKey(assetDefinition.assetId())) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no Sprite found with id (" +
                                        assetDefinition.assetId() + ")");
                    }
                    imageAsset = GET_SPRITE.get(assetDefinition.assetId());
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((Sprite) imageAsset).image().supportsMouseEventCapturing();
                    }
                }
                case ANIMATION -> {
                    if (!GET_ANIMATION.containsKey(assetDefinition.assetId())) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no Animation found with id (" +
                                        assetDefinition.assetId() + ")");
                    }
                    imageAsset = GET_ANIMATION.get(assetDefinition.assetId());
                    if (supportsMouseEventCapturing) {
                        supportsMouseEventCapturing =
                                ((Animation) imageAsset).supportsMouseEventCapturing();
                    }
                }
                case GLOBAL_LOOPING_ANIMATION -> {
                    if (!GET_GLOBAL_LOOPING_ANIMATION.containsKey(
                            assetDefinition.assetId())) {
                        throw new IllegalArgumentException(
                                "ImageAssetSetFactory.make: no GlobalLoopingAnimation found " +
                                        "with id (" + assetDefinition.assetId() + ")");
                    }
                    imageAsset = GET_GLOBAL_LOOPING_ANIMATION.get(assetDefinition.assetId());
                }
                default -> throw new IllegalArgumentException(
                        "ImageAssetSetFactory.make: assetDefinition has illegal assetType (" +
                                assetDefinition.assetType().toString() + ")");
            }

            Map<Direction, ImageAsset> assetsByDirection;
            if (assetsByTypeAndDirection.containsKey(type)) {
                assetsByDirection = assetsByTypeAndDirection.get(type);
            }
            else {
                assetsByTypeAndDirection.put(type, assetsByDirection = mapOf());
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
    }
}
