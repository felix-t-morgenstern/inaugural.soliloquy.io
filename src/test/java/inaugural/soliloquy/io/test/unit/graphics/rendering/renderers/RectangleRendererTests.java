package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleSegmentRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeRectangleRenderable;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

// TODO: Implement actual logic tests, now that rendering is broken off into BasicTriangleRenderer
@ExtendWith(MockitoExtension.class)
public class RectangleRendererTests {
    private final FloatBox RENDERING_DIMENS = randomFloatBox();
    private final Color COLOR_TOP_LEFT = randomColor();
    private final Color COLOR_TOP_RIGHT = randomColor();
    private final Color COLOR_BOTTOM_LEFT = randomColor();
    private final Color COLOR_BOTTOM_RIGHT = randomColor();
    private final int TEX_ID = randomInt();
    private final float TEX_WIDTH = randomFloat();
    private final float TEX_HEIGHT = randomFloat();
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            generateMockStaticProvider(floatBoxOf(0f, 0f, 1f, 1f));
    private final UUID UUID = java.util.UUID.randomUUID();
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private ProviderAtTime<FloatBox> mockRenderingDimensProvider;
    @Mock private ProviderAtTime<Color> mockTopLeftColorProvider;
    @Mock private ProviderAtTime<Color> mockTopRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomLeftColorProvider;
    @Mock private ProviderAtTime<Integer> mockBackgroundTextureIdProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private RectangleRenderable mockRenderable;

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private TriangleSegmentRenderer mockTriangleSegmentRenderer;

    private Renderer<RectangleRenderable> renderer;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingDimensProvider.provide(anyLong())).thenReturn(RENDERING_DIMENS);
        lenient().when(mockTopLeftColorProvider.provide(anyLong())).thenReturn(COLOR_TOP_LEFT);
        lenient().when(mockTopRightColorProvider.provide(anyLong())).thenReturn(COLOR_TOP_RIGHT);
        lenient().when(mockBottomLeftColorProvider.provide(anyLong()))
                .thenReturn(COLOR_BOTTOM_LEFT);
        lenient().when(mockBottomRightColorProvider.provide(anyLong()))
                .thenReturn(COLOR_BOTTOM_RIGHT);
        lenient().when(mockBackgroundTextureIdProvider.provide(anyLong())).thenReturn(TEX_ID);
        lenient().when(mockTextureTileWidthProvider.provide(anyLong())).thenReturn(TEX_WIDTH);
        lenient().when(mockTextureTileHeightProvider.provide(anyLong())).thenReturn(TEX_HEIGHT);

        lenient().when(mockRenderable.getRenderingDimensionsProvider())
                .thenReturn(mockRenderingDimensProvider);
        lenient().when(mockRenderable.getTopLeftColorProvider())
                .thenReturn(mockTopLeftColorProvider);
        lenient().when(mockRenderable.getTopRightColorProvider())
                .thenReturn(mockTopRightColorProvider);
        lenient().when(mockRenderable.getBottomLeftColorProvider())
                .thenReturn(mockBottomLeftColorProvider);
        lenient().when(mockRenderable.getBottomRightColorProvider())
                .thenReturn(mockBottomRightColorProvider);
        lenient().when(mockRenderable.getTextureIdProvider())
                .thenReturn(mockBackgroundTextureIdProvider);
        lenient().when(mockRenderable.getTextureTileWidthProvider())
                .thenReturn(mockTextureTileWidthProvider);
        lenient().when(mockRenderable.getTextureTileHeightProvider())
                .thenReturn(mockTextureTileHeightProvider);

        renderer = new RectangleRenderer(mockTimestampValidator,
                mockTriangleSegmentRenderer);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderer(null, mockTriangleSegmentRenderer));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderer(mockTimestampValidator, null));
    }

    @Test
    public void testSetMeshAndShader() {
        assertThrows(UnsupportedOperationException.class, () -> renderer.setMesh(mock(Mesh.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> renderer.setShader(mock(Shader.class)));
    }
}
