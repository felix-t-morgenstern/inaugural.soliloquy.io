package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.FiniteAnimationRenderer;
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
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
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

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.io.graphics.renderables.colorshifting.NetColorShifts.netShifts;

@ExtendWith(MockitoExtension.class)
public class FiniteAnimationRendererTests extends AbstractRendererTests {
    private final String ANIMATION_ID = randomString();
    private final float LEFT_X = 0.11f;
    private final float TOP_Y = 0.22f;
    private final float RIGHT_X = 0.33f;
    private final float BOTTOM_Y = 0.44f;
    private final ProviderAtTime<FloatBox> MOCK_BOX_PROVIDER =
            generateMockStaticProvider(floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y));
    private final long START_TIMESTAMP = randomLong();

    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private ColorShiftStackAggregator mockShiftAggregator;
    @Mock private Supplier<Float> mockGetScreenWToHRatio;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Image mockImage;
    @Mock private AnimationFrameSnippet mockSnippet;

    private Renderer<FiniteAnimationRenderable> renderer;

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
        lenient().when(mockRenderingBoundaries.currentBoundaries())
                .thenReturn(floatBoxOf(0f, 0f, 1f, 1f));
        lenient().when(mockShiftAggregator.aggregate(anyList(), anyLong()))
                .thenReturn(netShifts(0f, 0f, 0f, 0f, 0f));

        renderer = new FiniteAnimationRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                mockShiftAggregator, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderer(null, mockGetScreenWToHRatio, mockShiftAggregator,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderer(mockRenderingBoundaries, null,
                        mockShiftAggregator, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                        mockShiftAggregator, null));
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
        assertThrows(IllegalArgumentException.class, () -> renderer.render(null, START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, null, MOCK_BOX_PROVIDER, START_TIMESTAMP,
                        UUID.randomUUID()), START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, listOf(), null, START_TIMESTAMP,
                        UUID.randomUUID()), START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, listOf(), generateMockStaticProvider(null),
                        START_TIMESTAMP, UUID.randomUUID()), START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, listOf(), generateMockStaticProvider(null),
                        START_TIMESTAMP, UUID.randomUUID()), START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, listOf(),
                        generateMockStaticProvider(floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, TOP_Y)),
                        START_TIMESTAMP, UUID.randomUUID()), START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, listOf(),
                        generateMockStaticProvider(floatBoxOf(LEFT_X, TOP_Y, LEFT_X, BOTTOM_Y)),
                        START_TIMESTAMP, UUID.randomUUID()), START_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockRenderable(ANIMATION_ID, listOf(), MOCK_BOX_PROVIDER, START_TIMESTAMP,
                        null), START_TIMESTAMP));
    }

    @Test
    public void testRenderBeforeStartingTimestamp() {
        var mockRenderable =
                generateMockRenderable(ANIMATION_ID, listOf(), MOCK_BOX_PROVIDER,
                        START_TIMESTAMP, UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));

        renderer.render(mockRenderable, START_TIMESTAMP - 1L);

        verify(mockRenderable, never()).provide(anyLong());
    }

    @Test
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var mockShift = mock(ColorShift.class);
        var finiteAnimationRenderable =
                generateMockRenderable(ANIMATION_ID, listOf(mockShift),
                        MOCK_BOX_PROVIDER, START_TIMESTAMP, UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        renderer.render(finiteAnimationRenderable, START_TIMESTAMP);

        verify(mockShiftAggregator, once()).aggregate(eq(listOf(mockShift)), eq(START_TIMESTAMP));
    }

    @Test
    public void testRenderAfterAnimationEndDeletes() {
        var animationMsDuration = 456;
        var mockRenderable =
                generateMockRenderable(ANIMATION_ID, listOf(), MOCK_BOX_PROVIDER, START_TIMESTAMP,
                        UUID.randomUUID());
        when(mockRenderable.endTimestamp()).thenReturn(START_TIMESTAMP + animationMsDuration);
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));

        renderer.render(mockRenderable, START_TIMESTAMP + animationMsDuration);

        verify(mockRenderable, once()).delete();
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var mockRenderable =
                generateMockRenderable(ANIMATION_ID, listOf(), MOCK_BOX_PROVIDER, START_TIMESTAMP,
                        UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        var timestamp = randomLong();

        renderer.render(mockRenderable, timestamp);

        verify(mockTimestampValidator, once())
                .validateTimestamp(renderer.getClass().getCanonicalName(), timestamp);
    }

    private FiniteAnimationRenderable generateMockRenderable(
            String animationId,
            List<ColorShift> shifts,
            ProviderAtTime<FloatBox> dimensProvider,
            long startTimestamp,
            UUID uuid
    ) {
        var mockRenderable = mock(FiniteAnimationRenderable.class);
        lenient().when(mockRenderable.animationId()).thenReturn(animationId);
        lenient().when(mockRenderable.colorShifts()).thenReturn(shifts);
        lenient().when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(dimensProvider);
        var borderThickness =
                inaugural.soliloquy.tools.testing.Mock.<Float>generateMockStaticProvider(null);
        lenient().when(mockRenderable.getBorderThicknessProvider()).thenReturn(borderThickness);
        var borderColor =
                inaugural.soliloquy.tools.testing.Mock.<Color>generateMockStaticProvider(null);
        lenient().when(mockRenderable.getBorderColorProvider()).thenReturn(borderColor);
        lenient().when(mockRenderable.startTimestamp()).thenReturn(startTimestamp);
        lenient().when(mockRenderable.uuid()).thenReturn(uuid);

        lenient().when(mockRenderable.provide(anyLong())).thenReturn(mockSnippet);

        lenient().when(mockSnippet.image()).thenReturn(mockImage);

        return mockRenderable;
    }
}
