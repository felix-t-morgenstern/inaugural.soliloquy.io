package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class GlobalLoopingAnimationRendererTests extends AbstractRendererTests {
    private final float LEFT_X = 0.11f;
    private final float TOP_Y = 0.22f;
    private final float RIGHT_X = 0.33f;
    private final float BOTTOM_Y = 0.44f;
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private Image mockImage;
    @Mock private AnimationFrameSnippet mockSnippet;
    @Mock private GlobalLoopingAnimation mockAnimation;
    @Mock private ColorShiftStackAggregator mockShiftAggregator;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Supplier<Float> mockGetScreenWToHRatio;
    @Mock private TimestampValidator mockTimestampValidator;

    private Renderer<GlobalLoopingAnimationRenderable> renderer;

    @BeforeAll
    public static void setUpFixture() {
        AbstractRendererTests.setUpFixture();
    }

    @AfterAll
    public static void tearDownFixture() {
        AbstractRendererTests.tearDownFixture();
    }

    @BeforeEach
    public void setUp() {
        lenient().when(mockSnippet.image()).thenReturn(mockImage);
        lenient().when(mockSnippet.leftX()).thenReturn(0);
        lenient().when(mockSnippet.topY()).thenReturn(0);
        lenient().when(mockSnippet.rightX()).thenReturn(0);
        lenient().when(mockSnippet.bottomY()).thenReturn(0);

        lenient().when(mockAnimation.provide(anyLong())).thenReturn(mockSnippet);

        lenient().when(mockRenderingBoundaries.currentBoundaries())
                .thenReturn(floatBoxOf(0f, 0f, 1f, 1f));

        renderer =
                new GlobalLoopingAnimationRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                        mockShiftAggregator, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(null, mockGetScreenWToHRatio,
                        mockShiftAggregator, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(mockRenderingBoundaries, null,
                        mockShiftAggregator, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(mockRenderingBoundaries,
                        mockGetScreenWToHRatio, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(mockRenderingBoundaries,
                        mockGetScreenWToHRatio, mockShiftAggregator, null));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        var shifts = Collections.<ColorShift>listOf();

        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(null, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(mockAnimation, null,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(mockAnimation, shifts,
                        null,
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(mockAnimation, shifts,
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(mockAnimation, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        null),
                0L
        ));
    }

    @Test
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var shifts = Collections.<ColorShift>listOf();
        var renderable = generateMockRenderable(mockAnimation, shifts,
                generateMockStaticProvider(floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));

        renderer.render(renderable, MOST_RECENT_TIMESTAMP + 123);

        verify(mockShiftAggregator, once()).aggregate(same(shifts),
                eq(MOST_RECENT_TIMESTAMP + 123));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var shifts = Collections.<ColorShift>listOf();
        var globalLoopingAnimationRenderable =
                generateMockRenderable(mockAnimation, shifts,
                        generateMockStaticProvider(floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        var timestamp = randomLong();

        renderer.render(globalLoopingAnimationRenderable, timestamp);

        verify(mockTimestampValidator, once())
                .validateTimestamp(renderer.getClass().getCanonicalName(), timestamp);
    }

    private GlobalLoopingAnimationRenderable generateMockRenderable(
            GlobalLoopingAnimation animation,
            List<ColorShift> shifts,
            ProviderAtTime<FloatBox> dimensProvider,
            UUID uuid
    ) {
        var mockRenderable = mock(GlobalLoopingAnimationRenderable.class);
        lenient().when(mockRenderable.getGlobalLoopingAnimation()).thenReturn(animation);
        lenient().when(mockRenderable.colorShifts()).thenReturn(shifts);
        lenient().when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(dimensProvider);
        lenient().when(mockRenderable.uuid()).thenReturn(uuid);
        ProviderAtTime<Float> borderThickness = generateMockStaticProvider(null);
        lenient().when(mockRenderable.getBorderThicknessProvider()).thenReturn(borderThickness);
        ProviderAtTime<Color> borderColor = generateMockStaticProvider(null);
        lenient().when(mockRenderable.getBorderColorProvider()).thenReturn(borderColor);

        return mockRenderable;
    }
}
