package inaugural.soliloquy.io.test.display.renderables.providers.finite.linear;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.io.test.display.rendering.renderers.spriterenderer.SpriteRendererTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeSprite;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class FiniteLinearMovingProviderTest extends SpriteRendererTest {
    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            float borderThickness, Color borderColor,
            ColorShiftStackAggregator colorShiftStackAggregator,
            WindowResolutionManager windowResolutionManager) {
        Sprite = new FakeSprite(null, 266, 271, 313, 343);
        SpriteRenderable = new SpriteRenderableImpl(Sprite, staticProvider(borderThickness),
                staticProvider(borderColor), listOf(),
                staticProvider(floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f)), 0,
                java.util.UUID.randomUUID(), FirstChildStack, RENDERING_BOUNDARIES);

        SpriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                windowResolutionManager,
                colorShiftStackAggregator == null ?
                        new FakeColorShiftStackAggregator() :
                        colorShiftStackAggregator,
                null);

        return listOf(SpriteRenderer);
    }
}
