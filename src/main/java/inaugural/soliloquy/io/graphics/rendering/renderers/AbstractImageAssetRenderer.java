package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.assets.AssetSnippet;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.translate;

abstract class AbstractImageAssetRenderer<TSnippet extends AssetSnippet,
        TRenderable extends ImageAssetRenderable>
        extends CanRenderSnippets<TRenderable> {
    protected final ColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR;

    protected AbstractImageAssetRenderer(RenderingBoundaries renderingBoundaries,
                                         Supplier<Float> getScreenWToHRatio,
                                         ColorShiftStackAggregator shiftAggregator,
                                         TimestampValidator timestampValidator) {
        super(renderingBoundaries, getScreenWToHRatio, timestampValidator);
        COLOR_SHIFT_STACK_AGGREGATOR = Check.ifNull(shiftAggregator, "shiftAggregator");
    }

    protected void render(
            TRenderable renderable,
            BiFunction<TRenderable, Long, TSnippet> getSnippet,
            long timestamp,
            boolean checkRenderableAndTimestamp
    ) throws IllegalArgumentException {
        if (checkRenderableAndTimestamp) {
            checkRenderableAndTimestamp(renderable, timestamp);
        }

        var asset = getSnippet.apply(renderable, timestamp);
        Check.ifNull(asset, "asset provided by renderable");

        var borderThickness = Check.ifNull(renderable.getBorderThicknessProvider(),
                        "renderable.getBorderThicknessProvider()")
                .provide(timestamp);
        var borderColor = Check.ifNull(renderable.getBorderColorProvider(),
                        "renderable.getBorderColorProvider()")
                .provide(timestamp);
        var renderingArea = Check.ifNull(renderable.getRenderingDimensionsProvider(),
                        "renderable.getRenderingDimensionsProvider()")
                .provide(timestamp);

        validateRenderableWithDimensionsMembers(renderingArea, renderable.colorShifts(),
                renderable.uuid(), "renderable");

        if (borderThickness != null) {
            if (borderColor == null) {
                throw new IllegalArgumentException("SpriteRenderable.render: renderable " +
                        "cannot have non-null thickness, and null color");
            }

            Check.throwOnLtValue(borderThickness, 0f, "renderable borderThickness");

            Check.throwOnGtValue(borderThickness, 1f, "renderable borderThickness");

            var xThickness = borderThickness / getScreenWToHRatio.get();

            // upper-left
            super.render(translate(renderingArea, -xThickness, -borderThickness),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // upper-center
            super.render(translate(renderingArea, 0f, -borderThickness),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // upper-right
            super.render(translate(renderingArea, xThickness, -borderThickness),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // center-right
            super.render(translate(renderingArea, xThickness, 0),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // bottom-right
            super.render(translate(renderingArea, xThickness, borderThickness),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // bottom-center
            super.render(translate(renderingArea, 0f, borderThickness),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // bottom-left
            super.render(translate(renderingArea, -xThickness, borderThickness),
                    asset,
                    INTACT_COLOR,
                    borderColor);
            // center-left
            super.render(translate(renderingArea, -xThickness, 0f),
                    asset,
                    INTACT_COLOR,
                    borderColor);
        }

        var netColorShifts = netColorShifts(renderable.colorShifts(),
                COLOR_SHIFT_STACK_AGGREGATOR, timestamp);

        super.render(
                renderingArea,
                asset,
                INTACT_COLOR,
                netColorShifts
        );
    }

    // NB: I hate this, but it satisfies the workflow for now, its benefit is a marginal
    // avoidance of duplicate checks, and it's internal
    protected void render(
            TRenderable renderable,
            BiFunction<TRenderable, Long, TSnippet> getSnippet,
            long timestamp
    ) throws IllegalArgumentException {
        render(renderable, getSnippet, timestamp, true);
    }

    protected void checkRenderableAndTimestamp(TRenderable renderable, long timestamp) {
        Check.ifNull(renderable, "renderable");
        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);
    }
}
