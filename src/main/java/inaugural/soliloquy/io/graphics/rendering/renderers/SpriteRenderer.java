package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.translate;

public class SpriteRenderer extends CanRenderSnippets<SpriteRenderable> {
    private final ColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR;

    public SpriteRenderer(RenderingBoundaries renderingBoundaries,
                          WindowResolutionManager windowResolutionManager,
                          ColorShiftStackAggregator colorShiftStackAggregator,
                          Long mostRecentTimestamp) {
        super(renderingBoundaries, windowResolutionManager, mostRecentTimestamp);
        COLOR_SHIFT_STACK_AGGREGATOR = Check.ifNull(colorShiftStackAggregator,
                "colorShiftStackAggregator");
    }

    @Override
    public void render(SpriteRenderable spriteRenderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(spriteRenderable, "spriteRenderable");
        Check.ifNull(spriteRenderable.getSprite(), "spriteRenderable.getSprite()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        // TODO: Throw if rendering area or border thickness or color providers are null

        var borderThickness = Check.ifNull(spriteRenderable.getBorderThicknessProvider(),
                        "spriteRenderable.getBorderThicknessProvider()")
                .provide(timestamp);
        var borderColor = Check.ifNull(spriteRenderable.getBorderColorProvider(),
                        "spriteRenderable.getBorderColorProvider()")
                .provide(timestamp);
        var renderingArea = Check.ifNull(spriteRenderable.getRenderingDimensionsProvider(),
                        "spriteRenderable.getRenderingDimensionsProvider()")
                .provide(timestamp);

        validateRenderableWithDimensionsMembers(renderingArea,
                spriteRenderable.colorShiftProviders(),
                spriteRenderable.uuid(), "spriteRenderable");

        if (borderThickness != null) {
            if (borderColor == null) {
                throw new IllegalArgumentException("SpriteRenderable.render: spriteRenderable " +
                        "cannot have non-null thickness, and null color");
            }

            Check.throwOnLtValue(borderThickness, 0f, "spriteRenderable borderThickness");

            Check.throwOnGtValue(borderThickness, 1f, "spriteRenderable borderThickness");

            float yThickness = borderThickness;
            var xThickness = yThickness / getScreenWidthToHeightRatio.get();

            // upper-left
            super.render(translate(renderingArea, -xThickness, -yThickness),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // upper-center
            super.render(translate(renderingArea, 0f, -yThickness),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // upper-right
            super.render(translate(renderingArea, xThickness, -yThickness),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // center-right
            super.render(translate(renderingArea, xThickness, 0),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // bottom-right
            super.render(translate(renderingArea, xThickness, yThickness),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // bottom-center
            super.render(translate(renderingArea, 0f, yThickness),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // bottom-left
            super.render(translate(renderingArea, -xThickness, yThickness),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
            // center-left
            super.render(translate(renderingArea, -xThickness, 0f),
                    spriteRenderable.getSprite(),
                    INTACT_COLOR,
                    borderColor);
        }

        NetColorShifts netColorShifts = netColorShifts(spriteRenderable.colorShiftProviders(),
                COLOR_SHIFT_STACK_AGGREGATOR, timestamp);

        super.render(
                renderingArea,
                spriteRenderable.getSprite(),
                INTACT_COLOR,
                netColorShifts
        );
    }
}
