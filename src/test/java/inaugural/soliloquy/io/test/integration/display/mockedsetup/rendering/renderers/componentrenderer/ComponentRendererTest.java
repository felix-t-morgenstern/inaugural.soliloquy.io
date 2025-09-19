package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.componentrenderer;

import inaugural.soliloquy.io.api.WindowResolution;
import inaugural.soliloquy.io.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.StaticProvider;
import inaugural.soliloquy.io.graphics.rendering.FrameExecutorImpl;
import inaugural.soliloquy.io.graphics.rendering.MeshImpl;
import inaugural.soliloquy.io.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.io.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.ComponentRendererImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.io.mouse.MouseListener;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeColorShiftStackAggregator;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGraphicsPreloader;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;
import soliloquy.specs.io.graphics.rendering.timing.FrameTimer;
import soliloquy.specs.io.input.mouse.MouseCursor;

import java.util.Set;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class ComponentRendererTest extends DisplayTest {
    private final static String AXE_07_LOCATION =
            "./src/test/resources/images/items/Axe_512x512_NoBG_07.png";
    private final static String AXE_09_LOCATION =
            "./src/test/resources/images/items/Axe_512x512_NoBG_09.png";
    private final static String SWORD_06_LOCATION =
            "./src/test/resources/images/items/Sword06_986×2658.png";

    public static void runTest(Consumer<GraphicsCoreLoop> closeAfterSomeTime) {
        var resolution = WindowResolution.RES_1920x1080;

        var windowResolutionManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED, resolution);

        var frameTimer = mock(FrameTimer.class);

        MockTopLevelComponent = mock(Component.class);
        when(MockTopLevelComponent.getRenderingBoundariesProvider()).thenReturn(
                WHOLE_SCREEN_PROVIDER);
        MockFirstChildComponent = mock(Component.class);
        when(MockFirstChildComponent.containingComponent()).thenReturn(MockTopLevelComponent);
        when(MockFirstChildComponent.getRenderingBoundariesProvider()).thenReturn(
                WHOLE_SCREEN_PROVIDER);
        when(MockTopLevelComponent.contentsRepresentation()).thenReturn(setOf(MockFirstChildComponent));

        Renderers = mapOf();
        TimestampValidator = new TimestampValidator(null);
        var componentRenderer =
                new ComponentRendererImpl(Renderers, RENDERING_BOUNDARIES, TimestampValidator);

        var spriteAxe07Width = 512;
        var spriteAxe07Height = 512;
        var spriteAxe07 = generateMockSprite(0, 0, spriteAxe07Width, spriteAxe07Height);
        var spriteAxe09Width = 512;
        var spriteAxe09Height = 512;
        var spriteAxe09 = generateMockSprite(0, 0, spriteAxe09Width, spriteAxe09Height);
        var spriteSword06Width = 986;
        var spriteSword06Height = 2658;
        var spriteSword06 = generateMockSprite(0, 0, spriteSword06Width, spriteSword06Height);

        var axeScreenHeight = 0.5f;
        var swordScreenHeight = 1.0f;

        var spriteAxe07ScreenWidth = (spriteAxe07Width / (float) spriteAxe07Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        var spriteAxe07TopY = 0.125f;
        var spriteAxe07LeftX = 0.625f - (spriteAxe07ScreenWidth / 2f);
        var spriteRenderable1 = new SpriteRenderableImpl(
                spriteAxe07,
                ZERO_PROVIDER, BLACK_PROVIDER,
                listOf(),
                new StaticProvider<>(java.util.UUID.randomUUID(), floatBoxOf(
                        spriteAxe07LeftX,
                        spriteAxe07TopY,
                        spriteAxe07LeftX + spriteAxe07ScreenWidth,
                        spriteAxe07TopY + axeScreenHeight
                ), null),
                1, java.util.UUID.randomUUID(), MockFirstChildComponent,
                RENDERING_BOUNDARIES, TimestampValidator);

        var spriteAxe09ScreenWidth = (spriteAxe09Width / (float) spriteAxe09Height) *
                axeScreenHeight / resolution.widthToHeightRatio();
        var spriteAxe09TopY = 0.375f;
        var spriteAxe09LeftX = 0.375f - (spriteAxe09ScreenWidth / 2f);
        var spriteRenderable2 = new SpriteRenderableImpl(
                spriteAxe09,
                ZERO_PROVIDER, BLACK_PROVIDER,
                listOf(),
                new StaticProvider<>(java.util.UUID.randomUUID(), floatBoxOf(
                        spriteAxe09LeftX,
                        spriteAxe09TopY,
                        spriteAxe09LeftX + spriteAxe09ScreenWidth,
                        spriteAxe09TopY + axeScreenHeight
                ), null),
                1, java.util.UUID.randomUUID(), MockFirstChildComponent,
                RENDERING_BOUNDARIES, TimestampValidator);

        var spriteSword06ScreenWidth = 0.3710f;
        var spriteSword06TopY = 0f;
        var spriteSword06LeftX = 0.5f - (spriteSword06ScreenWidth / 2f);
        var spriteRenderable3 = new SpriteRenderableImpl(
                spriteSword06,
                ZERO_PROVIDER, BLACK_PROVIDER,
                listOf(),
                new StaticProvider<>(java.util.UUID.randomUUID(), floatBoxOf(
                        spriteSword06LeftX,
                        spriteSword06TopY,
                        spriteSword06LeftX + spriteSword06ScreenWidth,
                        spriteSword06TopY + swordScreenHeight
                ), null),
                1, java.util.UUID.randomUUID(), MockFirstChildComponent,
                RENDERING_BOUNDARIES, TimestampValidator);

        var graphicsPreloader = new FakeGraphicsPreloader();

        var spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                windowResolutionManager::windowWidthToHeightRatio,
                new FakeColorShiftStackAggregator(), null);
        //noinspection rawtypes
        Set<Renderer> renderersWithMesh = setOf(spriteRenderer);
        //noinspection rawtypes
        Set<Renderer> renderersWithShader = setOf(spriteRenderer);

        Renderers.put(SpriteRenderableImpl.class, spriteRenderer);
        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(
                setOf(spriteRenderable1, spriteRenderable2, spriteRenderable3));

        var frameExecutor = new FrameExecutorImpl(componentRenderer, 100);
        frameExecutor.setTopLevelComponent(MockTopLevelComponent);

        var graphicsCoreLoop =
                new GraphicsCoreLoopImpl("My title bar",
                        frameTimer, 20, windowResolutionManager, GLOBAL_CLOCK, frameExecutor,
                        new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX,
                        MeshImpl::new, renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader,
                        mock(MouseCursor.class), mock(MouseListener.class));

        graphicsPreloader.LoadAction = () -> {
            when(spriteAxe07.image()).thenReturn(new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(AXE_07_LOCATION, false)));
            when(spriteAxe09.image()).thenReturn(new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(AXE_09_LOCATION, false)));
            when(spriteSword06.image()).thenReturn(new ImageFactoryImpl(0.5f)
                    .make(new ImageDefinition(SWORD_06_LOCATION, false)));
            when(frameTimer.shouldExecuteNextFrame()).thenReturn(true);
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime.accept(graphicsCoreLoop));
    }
}
