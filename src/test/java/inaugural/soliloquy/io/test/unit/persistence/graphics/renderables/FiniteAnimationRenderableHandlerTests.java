package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.FiniteAnimationRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteAnimationRenderableHandlerTests
        extends AbstractImageAssetRenderableHandlerTests<Animation> {
    protected final long START = randomLong();
    protected final long PAUSE = randomLong();

    @Mock private FiniteAnimationRenderable mockRenderable;
    @Mock private FiniteAnimationRenderableFactory mockFactory;

    private String writtenValue;

    private TypeHandler<FiniteAnimationRenderable> handler;

    @BeforeEach
    public void setUp() {
        super.setUp(Animation.class);

        writtenValue = String.format(
                "{\"start\":%d,\"pause\":%d,\"assetId\":\"%s\",\"borderThickness\":\"%s\"," +
                        "\"borderColor\":\"%s\",\"colorShifts\":[\"%s\"],\"area\":\"%s\"," +
                        "\"onPress\":[{\"button\":%d,\"actionId\":\"%s\"}]," +
                        "\"onRelease\":[{\"button\":%d,\"actionId\":\"%s\"}]," +
                        "\"mouseOver\":\"%s\",\"mouseLeave\":\"%s\",\"z\":%d,\"uuid\":\"%s\"," +
                        "\"type\":\"%s\"}",
                START, PAUSE, ASSET_ID, BORDER_THICKNESS, BORDER_COLOR, COLOR_SHIFT, AREA,
                ON_PRESS_BUTTON, ON_PRESS_ACTION_ID, ON_RELEASE_BUTTON, ON_RELEASE_ACTION_ID,
                ON_MOUSE_OVER_ACTION_ID, ON_MOUSE_LEAVE_ACTION_ID, Z, UUID,
                mockRenderable.getClass().getCanonicalName());

        handler = new FiniteAnimationRenderableHandler(mockGetAsset, MOCK_GET_ACTION,
                mockProviderHandler, mockShiftHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(null, MOCK_GET_ACTION,
                        mockProviderHandler, mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(mockGetAsset, null,
                        mockProviderHandler, mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(mockGetAsset, MOCK_GET_ACTION,
                        null, mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(mockGetAsset, MOCK_GET_ACTION,
                        mockProviderHandler, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(mockGetAsset, MOCK_GET_ACTION,
                        mockProviderHandler, mockShiftHandler, null));
    }

    @Test
    public void testWrite() {
        setUpMockRenderable(mockRenderable);
        when(mockRenderable.animationId()).thenReturn(ASSET_ID);
        when(mockRenderable.startTimestamp()).thenReturn(START);
        when(mockRenderable.pausedTimestamp()).thenReturn(PAUSE);

        var output = handler.write(mockRenderable);

        assertEquals(writtenValue, output);
        verify(mockRenderable, once()).animationId();
        verifyWritten(mockRenderable);
        verify(mockRenderable, once()).startTimestamp();
        verify(mockRenderable, once()).pausedTimestamp();
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any(),
                anyLong(),
                any()
        )).thenReturn(mockRenderable);

        var output = handler.read(writtenValue);

        assertSame(mockRenderable, output);
        verifyRead();
        verify(mockFactory, once()).make(
                same(mockAsset),
                same(mockBorderThicknessProvider),
                same(mockBorderColorProvider),
                eq(mapOf(pairOf(ON_PRESS_BUTTON, MOCK_ON_PRESS_ACTION))),
                eq(mapOf(pairOf(ON_RELEASE_BUTTON, MOCK_ON_RELEASE_ACTION))),
                same(MOCK_ON_MOUSE_OVER_ACTION),
                same(MOCK_ON_MOUSE_LEAVE_ACTION),
                eq(listOf(mockShift)),
                same(mockAreaProvider),
                eq(Z),
                eq(UUID),
                isNull(),
                eq(START),
                eq(PAUSE)
        );
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
