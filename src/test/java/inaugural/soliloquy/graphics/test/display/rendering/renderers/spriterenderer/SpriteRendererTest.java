package inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeSprite;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class SpriteRendererTest extends DisplayTest {
    protected final static String RPG_WEAPONS_RELATIVE_LOCATION =
            "./src/test/resources/images/items/RPG_Weapons.png";

    protected static FakeSprite Sprite;
    protected static SpriteRenderable SpriteRenderable;
    protected static FloatBox SpriteRenderingDimensions;
    protected static Renderer<soliloquy.specs.graphics.renderables.SpriteRenderable> SpriteRenderer;

    /** @noinspection rawtypes */
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            float borderThickness, Color borderColor,
            ColorShiftStackAggregator colorShiftStackAggregator,
            WindowResolutionManager windowResolutionManager) {
        Sprite = new FakeSprite(null, 266, 271, 313, 343);

        SpriteRenderingDimensions = floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f);

        SpriteRenderable = new SpriteRenderableImpl(Sprite, staticProvider(borderThickness),
                staticProvider(borderColor), listOf(), staticProvider(SpriteRenderingDimensions), 0,
                java.util.UUID.randomUUID(), FirstChildStack, RENDERING_BOUNDARIES);

        FirstChildStack.add(SpriteRenderable);

        SpriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                windowResolutionManager,
                colorShiftStackAggregator == null ?
                        new FakeColorShiftStackAggregator() :
                        colorShiftStackAggregator,
                null);

        Renderers.registerRenderer(SpriteRenderableImpl.class, SpriteRenderer);

        return listOf(SpriteRenderer);
    }

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, true));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
