package inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeSprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        SpriteRenderingDimensions = new FakeFloatBox(0.25f, 0.125f, 0.75f, 0.875f);

        SpriteRenderable = mock(SpriteRenderable.class);
        when(SpriteRenderable.getSprite()).thenReturn(Sprite);
        when(SpriteRenderable.uuid()).thenReturn(java.util.UUID.randomUUID());
        when(SpriteRenderable.getZ()).thenReturn(0);
        when(SpriteRenderable.colorShiftProviders()).thenReturn(new ArrayList<>());
        when(SpriteRenderable.getRenderingDimensionsProvider()).thenReturn(
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), SpriteRenderingDimensions,
                        null));
        when(SpriteRenderable.getBorderThicknessProvider()).thenReturn(
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), borderThickness, null));
        when(SpriteRenderable.getBorderColorProvider()).thenReturn(
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), borderColor, null));

        SpriteRenderable.setRenderingDimensionsProvider(
                new StaticProviderImpl<>(java.util.UUID.randomUUID(), SpriteRenderingDimensions,
                        null));

        SpriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                windowResolutionManager,
                colorShiftStackAggregator == null ?
                        new FakeColorShiftStackAggregator() :
                        colorShiftStackAggregator,
                null);

        return listOf(SpriteRenderer);
    }

    public static void stackRendererAction(long timestamp) {
        SpriteRenderer.render(SpriteRenderable, timestamp);
    }

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, true));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
