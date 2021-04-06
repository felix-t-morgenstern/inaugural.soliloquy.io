package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.archetypes.SpriteRenderableArchetype;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

public class SpriteRenderer extends CanRenderSnippets<SpriteRenderable> {
    private final static SpriteRenderable ARCHETYPE = new SpriteRenderableArchetype();

    public SpriteRenderer(RenderingBoundaries renderingBoundaries,
                          FloatBoxFactory floatBoxFactory) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
    }

    @Override
    public void render(SpriteRenderable spriteRenderable) throws IllegalArgumentException {
        Check.ifNull(spriteRenderable, "spriteRenderable");

        Check.ifNull(spriteRenderable.sprite(), "spriteRenderable.sprite()");

        Check.ifNull(spriteRenderable.colorShifts(), "spriteRenderable.colorShifts()");

        Check.throwOnLteZero(spriteRenderable.renderingArea().width(),
                "spriteRenderable.width()");

        Check.throwOnLteZero(spriteRenderable.renderingArea().height(),
                "spriteRenderable.height()");

        float snippetLeftX =
                (float)spriteRenderable.sprite().leftX() /
                        spriteRenderable.sprite().image().width();

        float snippetTopY =
                (float)spriteRenderable.sprite().topY() /
                        spriteRenderable.sprite().image().height();

        float snippetRightX =
                (float)spriteRenderable.sprite().rightX() /
                        spriteRenderable.sprite().image().width();

        float snippetBottomY =
                (float)spriteRenderable.sprite().bottomY() /
                        spriteRenderable.sprite().image().height();

        super.render(spriteRenderable.renderingArea(),
                snippetLeftX, snippetTopY, snippetRightX, snippetBottomY,
                spriteRenderable.sprite().image().textureId(),
                1.0f, 1.0f, 1.0f, 1.0f);
    }
}
