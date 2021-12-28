package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RectangleRenderableImpl
        extends AbstractRenderableWithArea
        implements RectangleRenderable {
    private static final ProviderAtTime<Float> NO_BORDER_THICKNESS_PROVIDER =
            new NullProvider<>(0f);
    private static final ProviderAtTime<Color> NO_BORDER_COLOR_PROVIDER =
            new NullProvider<>(Color.BLACK);

    private ProviderAtTime<Color> _topLeftColorProvider;
    private ProviderAtTime<Color> _topRightColorProvider;
    private ProviderAtTime<Color> _bottomRightColorProvider;
    private ProviderAtTime<Color> _bottomLeftColorProvider;
    private ProviderAtTime<Integer> _backgroundTextureIdProvider;
    private float _backgroundTextureTileWidth;
    private float _backgroundTextureTileHeight;

    // NB: This field is used so that setting a border thickness or color provider _ONLY_ throws an
    //     exception _after_ this class has been constructed, since it must initially set those
    //     providers to the static class members above which always return null.
    private final boolean CONSTRUCTOR_IS_COMPLETED;

    public RectangleRenderableImpl(ProviderAtTime<Color> topLeftColorProvider,
                                   ProviderAtTime<Color> topRightColorProvider,
                                   ProviderAtTime<Color> bottomRightColorProvider,
                                   ProviderAtTime<Color> bottomLeftColorProvider,
                                   ProviderAtTime<Integer> backgroundTextureIdProvider,
                                   float backgroundTextureTileWidth,
                                   float backgroundTextureTileHeight,
                                   Map<Integer, Action<Long>> onPress,
                                   Map<Integer, Action<Long>> onRelease,
                                   Action<Long> onMouseOver,
                                   Action<Long> onMouseLeave,
                                   List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                   ProviderAtTime<FloatBox> renderingAreaProvider,
                                   int z,
                                   EntityUuid uuid,
                                   Consumer<Renderable> updateZIndexInContainer,
                                   Consumer<Renderable> removeFromContainer) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                NO_BORDER_THICKNESS_PROVIDER, NO_BORDER_COLOR_PROVIDER, renderingAreaProvider, z,
                uuid, updateZIndexInContainer, removeFromContainer);
        setTopLeftColorProvider(topLeftColorProvider);
        setTopRightColorProvider(topRightColorProvider);
        setBottomRightColorProvider(bottomRightColorProvider);
        setBottomLeftColorProvider(bottomLeftColorProvider);
        setBackgroundTextureIdProvider(backgroundTextureIdProvider);
        setBackgroundTextureTileWidth(backgroundTextureTileWidth);
        setBackgroundTextureTileHeight(backgroundTextureTileHeight);
        CONSTRUCTOR_IS_COMPLETED = true;
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return true;
    }

    @Override
    protected String className() {
        return RectangleRenderableImpl.class.getCanonicalName();
    }

    @Override
    public ProviderAtTime<Color> getTopLeftColorProvider() {
        return _topLeftColorProvider;
    }

    @Override
    public void setTopLeftColorProvider(ProviderAtTime<Color> topLeftColorProvider)
            throws IllegalArgumentException {
        _topLeftColorProvider = Check.ifNull(topLeftColorProvider, "topLeftColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getTopRightColorProvider() {
        return _topRightColorProvider;
    }

    @Override
    public void setTopRightColorProvider(ProviderAtTime<Color> topRightColorProvider)
            throws IllegalArgumentException {
        _topRightColorProvider = Check.ifNull(topRightColorProvider, "topRightColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getBottomRightColorProvider() {
        return _bottomRightColorProvider;
    }

    @Override
    public void setBottomRightColorProvider(ProviderAtTime<Color> bottomRightColorProvider)
            throws IllegalArgumentException {
        _bottomRightColorProvider = Check.ifNull(bottomRightColorProvider,
                "bottomRightColorProvider");
    }

    @Override
    public ProviderAtTime<Color> getBottomLeftColorProvider() {
        return _bottomLeftColorProvider;
    }

    @Override
    public void setBottomLeftColorProvider(ProviderAtTime<Color> bottomLeftColorProvider)
            throws IllegalArgumentException {
        _bottomLeftColorProvider = Check.ifNull(bottomLeftColorProvider,
                "bottomLeftColorProvider");
    }

    @Override
    public ProviderAtTime<Integer> getBackgroundTextureIdProvider() {
        return _backgroundTextureIdProvider;
    }

    @Override
    public void setBackgroundTextureIdProvider(ProviderAtTime<Integer> backgroundTextureIdProvider)
            throws IllegalArgumentException {
        _backgroundTextureIdProvider = Check.ifNull(backgroundTextureIdProvider,
                "backgroundTextureIdProvider");
    }

    @Override
    public float getBackgroundTextureTileWidth() {
        return _backgroundTextureTileWidth;
    }

    @Override
    public void setBackgroundTextureTileWidth(float backgroundTextureTileWidth)
            throws IllegalArgumentException {
        _backgroundTextureTileWidth = Check.throwOnLteZero(backgroundTextureTileWidth,
                "backgroundTextureTileWidth");
    }

    @Override
    public float getBackgroundTextureTileHeight() {
        return _backgroundTextureTileHeight;
    }

    @Override
    public void setBackgroundTextureTileHeight(float backgroundTextureTileHeight)
            throws IllegalArgumentException {
        _backgroundTextureTileHeight = Check.throwOnLteZero(backgroundTextureTileHeight,
                "backgroundTextureTileHeight");
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider) {
        if (CONSTRUCTOR_IS_COMPLETED) {
            throw new UnsupportedOperationException(
                    "RectangleRenderableImpl.setBorderThicknessProvider: Rectangles have no " +
                            "borders. (If you want one, just draw a larger rectangle beneath " +
                            "this rectangle.)");
        }
        else {
            super.setBorderThicknessProvider(borderThicknessProvider);
        }
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider) {
        if (CONSTRUCTOR_IS_COMPLETED) {
            throw new UnsupportedOperationException(
                    "RectangleRenderableImpl.setBorderColorProvider: Rectangles have no " +
                            "borders. (If you want one, just draw a larger rectangle beneath " +
                            "this rectangle.)");
        }
        else {
            super.setBorderColorProvider(borderColorProvider);
        }
    }

    @Override
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        return _capturesMouseEvents;
    }

    @Override
    public String getInterfaceName() {
        return RectangleRenderable.class.getCanonicalName();
    }

    private static class NullProvider<T> implements StaticProvider<T> {
        private final T ARCHETYPE;

        private NullProvider(T archetype) {
            ARCHETYPE = archetype;
        }

        @Override
        public T provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public T getArchetype() {
            return ARCHETYPE;
        }

        @Override
        public EntityUuid uuid() {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

        }

        @Override
        public Long pausedTimestamp() {
            return null;
        }

        @Override
        public Long mostRecentTimestamp() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return null;
        }
    }
}
