package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.AssetSnippet;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

abstract class AbstractImageAssetRenderable extends AbstractRenderableWithMouseEvents
        implements ImageAssetRenderable {
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS;

    private ProviderAtTime<Float> borderThicknessProvider;
    private ProviderAtTime<Color> borderColorProvider;
    protected ProviderAtTime<FloatBox> renderingAreaProvider;

    protected AbstractImageAssetRenderable(List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                           ProviderAtTime<Float> borderThicknessProvider,
                                           ProviderAtTime<Color> borderColorProvider,
                                           ProviderAtTime<FloatBox> renderingAreaProvider,
                                           int z,
                                           UUID uuid,
                                           RenderableStack containingStack,
                                           RenderingBoundaries renderingBoundaries) {
        this(false, null, null, null, null, colorShiftProviders, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, containingStack,
                renderingBoundaries);
    }

    protected AbstractImageAssetRenderable(
            Map<Integer, Action<MouseEventInputs>> onPress,
            Map<Integer, Action<MouseEventInputs>> onRelease,
            Action<MouseEventInputs> onMouseOver,
            Action<MouseEventInputs> onMouseLeave,
            List<ProviderAtTime<ColorShift>> colorShiftProviders,
            ProviderAtTime<Float> borderThicknessProvider,
            ProviderAtTime<Color> borderColorProvider,
            ProviderAtTime<FloatBox> renderingAreaProvider,
            int z,
            UUID uuid,
            RenderableStack containingStack,
            RenderingBoundaries renderingBoundaries) {
        this(true, onPress, onRelease, onMouseOver, onMouseLeave,
                colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingAreaProvider, z, uuid, containingStack, renderingBoundaries);
    }

    private AbstractImageAssetRenderable(boolean capturesMouseEvents,
                                         Map<Integer, Action<MouseEventInputs>> onPress,
                                         Map<Integer, Action<MouseEventInputs>> onRelease,
                                         Action<MouseEventInputs> onMouseOver,
                                         Action<MouseEventInputs> onMouseLeave,
                                         List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                         int z,
                                         UUID uuid,
                                         RenderableStack containingStack,
                                         RenderingBoundaries renderingBoundaries) {
        super(capturesMouseEvents, onPress, onRelease, onMouseOver, onMouseLeave, z, uuid,
                containingStack, renderingBoundaries);
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
        return borderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider)
            throws IllegalArgumentException {
        this.borderThicknessProvider =
                Check.ifNull(borderThicknessProvider, "borderThicknessProvider");
    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return borderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider)
            throws IllegalArgumentException {
        this.borderColorProvider = Check.ifNull(borderColorProvider, "borderColorProvider");
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return renderingAreaProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
            throws IllegalArgumentException {
        renderingAreaProvider = Check.ifNull(renderingDimensionsProvider,
                "renderingDimensionsProvider");
    }

    protected boolean capturesMouseEventAtPoint(Vertex point, long timestamp,
                                                Supplier<AssetSnippet> snippetSupplier) {
        throwIfNotSupportingMouseEvents("capturesMouseEventAtPoint");
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        Check.throwOnLtValue(point.X, 0f, "point.X");
        Check.throwOnLtValue(point.Y, 0f, "point.Y");
        Check.throwOnGtValue(point.X, 1f, "point.X");
        Check.throwOnGtValue(point.Y, 1f, "point.Y");

        var renderingBoundaries = RENDERING_BOUNDARIES.currentBoundaries();
        if (point.X < renderingBoundaries.LEFT_X || point.X > renderingBoundaries.RIGHT_X ||
                point.Y < renderingBoundaries.TOP_Y || point.Y > renderingBoundaries.BOTTOM_Y) {
            return false;
        }

        var renderingArea = renderingAreaProvider.provide(timestamp);
        if (point.X < renderingArea.LEFT_X) {
            throw new IllegalArgumentException(
                    className() + ".capturesMouseEventAtPoint: point.X (" + point.X +
                            ") is to the left of left boundary of renderable (" +
                            renderingArea.LEFT_X + ")");
        }
        if (point.X > renderingArea.RIGHT_X) {
            throw new IllegalArgumentException(
                    className() + ".capturesMouseEventAtPoint: point.X (" + point.X +
                            ") is to the right of right boundary of renderable (" +
                            renderingArea.RIGHT_X + ")");
        }
        if (point.Y < renderingArea.TOP_Y) {
            throw new IllegalArgumentException(
                    className() + ".capturesMouseEventAtPoint: point.Y (" + point.Y +
                            ") is above top boundary of renderable (" + renderingArea.TOP_Y + ")");
        }
        if (point.Y > renderingArea.BOTTOM_Y) {
            throw new IllegalArgumentException(
                    className() + ".capturesMouseEventAtPoint: point.Y (" + point.Y +
                            ") is below bottom boundary of renderable (" + renderingArea.BOTTOM_Y +
                            ")");
        }
        var snippet = snippetSupplier.get();
        var offsetX = 0f;
        var offsetY = 0f;
        if (snippet instanceof AnimationFrameSnippet) {
            offsetX = ((AnimationFrameSnippet) snippet).offsetX();
            offsetY = ((AnimationFrameSnippet) snippet).offsetY();
        }
        var image = snippet.image();
        var imageX =
                (int) ((((point.X - offsetX) - renderingArea.LEFT_X) / renderingArea.width()) *
                        (snippet.rightX() - snippet.leftX())) + snippet.leftX();
        var imageY =
                (int) ((((point.Y - offsetY) - renderingArea.TOP_Y) / renderingArea.height()) *
                        (snippet.bottomY() - snippet.topY())) + snippet.topY();
        return image.capturesMouseEventsAtPixel(imageX, imageY);
    }
}
