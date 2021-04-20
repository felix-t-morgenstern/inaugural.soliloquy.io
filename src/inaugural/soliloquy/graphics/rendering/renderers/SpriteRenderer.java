package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.awt.*;
import java.util.List;

public class SpriteRenderer extends CanRenderSnippets<SpriteRenderable> {
    public SpriteRenderer(RenderingBoundaries renderingBoundaries,
                          FloatBoxFactory floatBoxFactory,
                          WindowResolutionManager windowResolutionManager) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE, windowResolutionManager);
    }

    @Override
    public void render(SpriteRenderable spriteRenderable, long timestamp)
            throws IllegalArgumentException {
        validateRenderableWithArea(spriteRenderable, "spriteRenderable");

        Check.ifNull(spriteRenderable.sprite(), "spriteRenderable.sprite()");

        validateTimestamp(timestamp, "SpriteRenderer");

        if (spriteRenderable.borderThickness() != null) {
            if (spriteRenderable.borderColor() == null) {
                throw new IllegalArgumentException("SpriteRenderable.render: spriteRenderable " +
                        "cannot have non-null thickness, and null color");
            }

            Check.throwOnLtValue(spriteRenderable.borderThickness(), 0f,
                    "spriteRenderable.borderThickness()");

            Check.throwOnGtValue(spriteRenderable.borderThickness(), 1f,
                    "spriteRenderable.borderThickness()");

            float yThickness = spriteRenderable.borderThickness();
            // TODO: Get a producer to generate the width-to-height ratio of the window
            float xThickness = yThickness / _screenWidthToHeightRatio;

            // upper-left
            super.render(spriteRenderable.renderingArea().translate(-xThickness, -yThickness),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // upper-center
            super.render(spriteRenderable.renderingArea().translate(0f, -yThickness),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // upper-right
            super.render(spriteRenderable.renderingArea().translate(xThickness, -yThickness),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // center-right
            super.render(spriteRenderable.renderingArea().translate(xThickness, 0),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // bottom-right
            super.render(spriteRenderable.renderingArea().translate(xThickness, yThickness),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // bottom-center
            super.render(spriteRenderable.renderingArea().translate(0f, yThickness),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // bottom-left
            super.render(spriteRenderable.renderingArea().translate(-xThickness, yThickness),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
            // center-left
            super.render(spriteRenderable.renderingArea().translate(-xThickness, 0f),
                    spriteRenderable.sprite(),
                    1.0f, 1.0f, 1.0f, 1.0f,
                    spriteRenderable.borderColor());
        }

        super.render(spriteRenderable.renderingArea(),
                spriteRenderable.sprite(),
                1.0f, 1.0f, 1.0f, 1.0f);
    }

    private final static SpriteRenderable ARCHETYPE = new SpriteRenderable() {
        @Override
        public String getInterfaceName() {
            return SpriteRenderable.class.getCanonicalName();
        }

        @Override
        public EntityUuid id() {
            return null;
        }

        @Override
        public FloatBox renderingArea() {
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
        public Float borderThickness() {
            return null;
        }

        @Override
        public Color borderColor() {
            return null;
        }
    };
}
