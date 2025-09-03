package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.ImageAssetSetRenderer;
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
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetRendererTests extends AbstractRendererTests {
    private final float LEFT_X = 0.11f;
    private final float TOP_Y = 0.22f;
    private final float RIGHT_X = 0.33f;
    private final float BOTTOM_Y = 0.44f;

    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private ColorShiftStackAggregator mockShiftAggregator;
    @Mock private Supplier<Float> mockGetScreenWToHRatio;
    @Mock private TimestampValidator mockTimestampValidator;

    @Mock private Map<String, String> mockDisplayParams;
    @Mock private Image mockImage;
    @Mock private ImageAssetSet mockImageAssetSet;
    @Mock private Sprite mockSprite;
    @Mock private AnimationFrameSnippet mockAnimationSnippet;
    @Mock private Animation mockAnimation;
    @Mock private GlobalLoopingAnimation mockGlobalLoopingAnimation;

    private Renderer<ImageAssetSetRenderable> renderer;

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
        lenient().when(mockSprite.image()).thenReturn(mockImage);

        lenient().when(mockAnimationSnippet.image()).thenReturn(mockImage);

        lenient().when(mockRenderingBoundaries.currentBoundaries())
                .thenReturn(floatBoxOf(0f, 0f, 1f, 1f));

        renderer = new ImageAssetSetRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                mockShiftAggregator, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderer(null, mockGetScreenWToHRatio, mockShiftAggregator,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderer(mockRenderingBoundaries, null, mockShiftAggregator,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderer(mockRenderingBoundaries, mockGetScreenWToHRatio,
                        mockShiftAggregator, null));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setShader(null));
    }

    @Test
    public void testRenderAnimation() {
        var animationStart = randomLong();
        var timestamp = randomLong();
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockAnimation);
        when(mockAnimation.snippetAtFrame(anyInt())).thenReturn(mockAnimationSnippet);
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        var mockRenderable = generateMockImageAssetSetRenderable(mockImageAssetSet, listOf(),
                generateMockStaticProvider(randomValidFloatBox()), generateMockStaticProvider(null),
                generateMockStaticProvider(null), UUID.randomUUID());
        when(mockRenderable.getAnimationStart()).thenReturn(animationStart);

        renderer.render(mockRenderable, timestamp);

        verify(mockImageAssetSet, once()).getImageAssetWithDisplayParams(same(mockDisplayParams));
        verify(mockAnimation, once()).snippetAtFrame((int) (timestamp - animationStart));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        var shifts = Collections.<ColorShift>listOf();
        var borderThickness = randomFloatWithInclusiveFloor(0f);
        var borderColor = randomColor();
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockSprite);
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(null, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, null,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        null,
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        null,
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(null),
                        null,
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, LEFT_X, BOTTOM_Y)),
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, null,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, TOP_Y)),
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(borderThickness),
                        generateMockStaticProvider(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(-0.0001f),
                        generateMockStaticProvider(borderColor),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(1.0001f),
                        generateMockStaticProvider(borderColor),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                generateMockImageAssetSetRenderable(mockImageAssetSet, null,
                        generateMockStaticProvider(
                                floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(null),
                        null),
                0L
        ));
    }

    @Test
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var timestamp = randomLong();
        var shifts = Collections.<ColorShift>listOf();
        var mockRenderable = generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                generateMockStaticProvider(
                        floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                generateMockStaticProvider(null),
                generateMockStaticProvider(null),
                UUID.randomUUID());
        when(mockRenderable.getImageAssetSet()).thenReturn(mockImageAssetSet);
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockSprite);
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        renderer.render(mockRenderable, timestamp);

        verify(mockShiftAggregator, once()).aggregate(same(shifts), eq(timestamp));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var timestamp = randomLong();
        var shifts = Collections.<ColorShift>listOf();
        var mockRenderable = generateMockImageAssetSetRenderable(mockImageAssetSet, shifts,
                generateMockStaticProvider(
                        floatBoxOf(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)),
                generateMockStaticProvider(null),
                generateMockStaticProvider(null),
                UUID.randomUUID());
        when(mockRenderable.getImageAssetSet()).thenReturn(mockImageAssetSet);
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockSprite);
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));

        renderer.render(mockRenderable, timestamp);

        verify(mockTimestampValidator, once())
                .validateTimestamp(renderer.getClass().getCanonicalName(), timestamp);
    }

    private ImageAssetSetRenderable generateMockImageAssetSetRenderable(
            ImageAssetSet imageAssetSet,
            List<ColorShift> shifts,
            ProviderAtTime<FloatBox> dimens,
            ProviderAtTime<Float> borderThickness,
            ProviderAtTime<Color> borderColor,
            UUID uuid
    ) {
        var mockRenderable = mock(ImageAssetSetRenderable.class);

        lenient().when(mockRenderable.getImageAssetSet()).thenReturn(imageAssetSet);
        lenient().when(mockRenderable.displayParams()).thenReturn(mockDisplayParams);
        lenient().when(mockRenderable.colorShifts()).thenReturn(shifts);
        lenient().when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(dimens);
        lenient().when(mockRenderable.getBorderThicknessProvider()).thenReturn(borderThickness);
        lenient().when(mockRenderable.getBorderColorProvider()).thenReturn(borderColor);
        lenient().when(mockRenderable.uuid()).thenReturn(uuid);

        return mockRenderable;
    }
}
