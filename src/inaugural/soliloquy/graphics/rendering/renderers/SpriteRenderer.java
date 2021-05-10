package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

public class SpriteRenderer extends CanRenderSnippets<SpriteRenderable> {
    public SpriteRenderer(RenderingBoundaries renderingBoundaries,
                          FloatBoxFactory floatBoxFactory,
                          WindowResolutionManager windowResolutionManager) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE, windowResolutionManager);
    }

    @Override
    public void render(SpriteRenderable spriteRenderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(spriteRenderable, "spriteRenderable");
        Check.ifNull(spriteRenderable.sprite(), "spriteRenderable.sprite()");

        validateTimestamp(timestamp, "SpriteRenderer");
        
        // TODO: Throw if rendering area or border thickness or color providers are null
        
        Float borderThickness = Check.ifNull(spriteRenderable.borderThicknessProvider(),
                "spriteRenderable.borderThicknessProvider()")
                .provide(timestamp);
        Color borderColor = Check.ifNull(spriteRenderable.borderColorProvider(),
                "spriteRenderable.borderColorProvider()")
                .provide(timestamp);
        FloatBox renderingArea = Check.ifNull(spriteRenderable.renderingAreaProvider(),
                "spriteRenderable.renderingAreaProvider()")
                .provide(timestamp);

        validateRenderableWithAreaMembers(renderingArea, spriteRenderable.colorShifts(),
                spriteRenderable.id(), "spriteRenderable");

        if (borderThickness != null) {
            if (borderColor == null) {
                throw new IllegalArgumentException("SpriteRenderable.render: spriteRenderable " +
                        "cannot have non-null thickness, and null color");
            }

            Check.throwOnLtValue(borderThickness, 0f, "spriteRenderable borderThickness");

            Check.throwOnGtValue(borderThickness, 1f, "spriteRenderable borderThickness");

            float yThickness = borderThickness;
            float xThickness = yThickness / _screenWidthToHeightRatio;

            // upper-left
            super.render(renderingArea.translate(-xThickness, -yThickness),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // upper-center
            super.render(renderingArea.translate(0f, -yThickness),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // upper-right
            super.render(renderingArea.translate(xThickness, -yThickness),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // center-right
            super.render(renderingArea.translate(xThickness, 0),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // bottom-right
            super.render(renderingArea.translate(xThickness, yThickness),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // bottom-center
            super.render(renderingArea.translate(0f, yThickness),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // bottom-left
            super.render(renderingArea.translate(-xThickness, yThickness),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
            // center-left
            super.render(renderingArea.translate(-xThickness, 0f),
                    spriteRenderable.sprite(),
                    INTACT_COLOR,
                    borderColor);
        }

        super.render(renderingArea,
                spriteRenderable.sprite(),
                INTACT_COLOR);
    }

    private final static SpriteRenderable ARCHETYPE = new SpriteRenderable() {
        @Override
        public EntityUuid id() {
            return null;
        }

        @Override
        public ProviderAtTime<FloatBox> renderingAreaProvider() {
            return null;
        }

        @Override
        public int z() {
            return 0;
        }

        @Override
        public void delete() {

        }

        @Override
        public boolean capturesMouseEvents() {
            return false;
        }

        @Override
        public void click() throws UnsupportedOperationException {

        }

        @Override
        public void mouseOver() throws UnsupportedOperationException {

        }

        @Override
        public void mouseLeave() throws UnsupportedOperationException {

        }

        @Override
        public List<ColorShift> colorShifts() {
            return null;
        }

        @Override
        public Sprite sprite() {
            return null;
        }

        @Override
        public ProviderAtTime<Float> borderThicknessProvider() {
            return null;
        }

        @Override
        public ProviderAtTime<Color> borderColorProvider() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return SpriteRenderable.class.getCanonicalName();
        }
    };
}
