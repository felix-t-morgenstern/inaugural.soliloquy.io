package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.spriterenderer;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeSprite;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Set;
import java.util.function.Supplier;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class SpriteRendererTest extends DisplayTest {
    protected final static String RPG_WEAPONS_RELATIVE_LOCATION =
            "./src/test/resources/images/items/RPG_Weapons.png";

    protected static FakeSprite Sprite;
    protected static SpriteRenderable SpriteRenderable;
    protected static FloatBox SpriteRenderingDimensions;
    protected static Renderer<SpriteRenderable> SpriteRenderer;

    /** @noinspection rawtypes */
    public static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            float borderThickness, Color borderColor,
            ColorShiftStackAggregator colorShiftStackAggregator,
            Supplier<Float> getScreenWToHRatio) {
        Sprite = new FakeSprite(null, 266, 271, 313, 343);

        SpriteRenderingDimensions = floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f);

        SpriteRenderable = new SpriteRenderableImpl(Sprite, staticProvider(borderThickness),
                staticProvider(borderColor), listOf(), staticProvider(SpriteRenderingDimensions), 0,
                java.util.UUID.randomUUID(), MockTopLevelComponent, RENDERING_BOUNDARIES,
                TimestampValidator);

        lenient().when(MockFirstChildComponent.contents()).thenReturn(setOf(SpriteRenderable));

        SpriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                getScreenWToHRatio,
                colorShiftStackAggregator == null ?
                        mock(ColorShiftStackAggregator.class) :
                        colorShiftStackAggregator,
                TimestampValidator);

        Renderers.put(SpriteRenderableImpl.class, SpriteRenderer);

        return setOf(SpriteRenderer);
    }

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, true));
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
