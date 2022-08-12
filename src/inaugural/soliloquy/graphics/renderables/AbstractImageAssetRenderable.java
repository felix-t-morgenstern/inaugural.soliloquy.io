package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.AssetSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

abstract class AbstractImageAssetRenderable extends AbstractRenderableWithMouseEvents
        implements ImageAssetRenderable {
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS;

    private ProviderAtTime<Float> _borderThicknessProvider;
    private ProviderAtTime<Color> _borderColorProvider;
    protected ProviderAtTime<FloatBox> _renderingAreaProvider;

    protected AbstractImageAssetRenderable(List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                           ProviderAtTime<Float> borderThicknessProvider,
                                           ProviderAtTime<Color> borderColorProvider,
                                           ProviderAtTime<FloatBox> renderingAreaProvider,
                                           int z,
                                           UUID uuid,
                                           Consumer<Renderable> updateZIndexInContainer,
                                           Consumer<Renderable> removeFromContainer)
    {
        this(false, null, null, null, null, colorShiftProviders, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    protected AbstractImageAssetRenderable(Map<Integer, Action<Long>> onPress,
                                           Map<Integer, Action<Long>> onRelease,
                                           Action<Long> onMouseOver,
                                           Action<Long> onMouseLeave,
                                           List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                           ProviderAtTime<Float> borderThicknessProvider,
                                           ProviderAtTime<Color> borderColorProvider,
                                           ProviderAtTime<FloatBox> renderingAreaProvider,
                                           int z,
                                           UUID uuid,
                                           Consumer<Renderable> updateZIndexInContainer,
                                           Consumer<Renderable> removeFromContainer)
    {
        this(true, onPress, onRelease, onMouseOver, onMouseLeave,
                colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingAreaProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
    }

    private AbstractImageAssetRenderable(boolean capturesMouseEvents,
                                         Map<Integer, Action<Long>> onPress,
                                         Map<Integer, Action<Long>> onRelease,
                                         Action<Long> onMouseOver,
                                         Action<Long> onMouseLeave,
                                         List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                         int z,
                                         UUID uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer) {
        super(capturesMouseEvents, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                updateZIndexInContainer, removeFromContainer);
        COLOR_SHIFT_PROVIDERS = Check.ifNull(colorShiftProviders, "colorShiftProviders");
        setRenderingDimensionsProvider(renderingDimensionsProvider);
        setBorderColorProvider(borderColorProvider);
        setBorderThicknessProvider(borderThicknessProvider);
    }

    @Override
    public List<ProviderAtTime<ColorShift>> colorShiftProviders() {
        return COLOR_SHIFT_PROVIDERS;
    }

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return _borderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider)
            throws IllegalArgumentException {
        _borderThicknessProvider =
                Check.ifNull(borderThicknessProvider, "borderThicknessProvider");
    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return _borderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider)
            throws IllegalArgumentException {
        _borderColorProvider = Check.ifNull(borderColorProvider, "borderColorProvider");
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return _renderingAreaProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
            throws IllegalArgumentException {
        _renderingAreaProvider = Check.ifNull(renderingDimensionsProvider,
                "renderingDimensionsProvider");
    }

    protected boolean capturesMouseEventAtPoint(float x, float y, long timestamp,
                                                Supplier<AssetSnippet> snippetSupplier) {
        throwIfNotSupportingMouseEvents("capturesMouseEventAtPoint");
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        Check.throwOnLtValue(x, 0f, "x");
        Check.throwOnLtValue(y, 0f, "y");
        Check.throwOnGtValue(x, 1f, "x");
        Check.throwOnGtValue(y, 1f, "y");
        FloatBox renderingArea = _renderingAreaProvider.provide(timestamp);
        if (x < renderingArea.leftX()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: x (" + x
                    + ") is to the left of left boundary of renderable (" + renderingArea.leftX() +
                    ")");
        }
        if (x > renderingArea.rightX()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: x (" + x
                    + ") is to the right of right boundary of renderable (" + renderingArea.rightX()
                    + ")");
        }
        if (y < renderingArea.topY()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: y (" + y
                    + ") is above top boundary of renderable (" + renderingArea.topY() + ")");
        }
        if (y > renderingArea.bottomY()) {
            throw new IllegalArgumentException(className() + ".capturesMouseEventAtPoint: y (" + y
                    + ") is below bottom boundary of renderable (" + renderingArea.bottomY() +
                    ")");
        }
        AssetSnippet snippet = snippetSupplier.get();
        float offsetX = 0f;
        float offsetY = 0f;
        if (snippet instanceof AnimationFrameSnippet) {
            offsetX = ((AnimationFrameSnippet)snippet).offsetX();
            offsetY = ((AnimationFrameSnippet)snippet).offsetY();
        }
        Image image = snippet.image();
        int imageX =
                (int)((((x - offsetX) - renderingArea.leftX()) / renderingArea.width())
                        * (snippet.rightX() - snippet.leftX())) + snippet.leftX();
        int imageY =
                (int)((((y - offsetY) - renderingArea.topY()) / renderingArea.height())
                        * (snippet.bottomY() - snippet.topY())) + snippet.topY();
        return image.capturesMouseEventsAtPixel(imageX, imageY);
    }
}
