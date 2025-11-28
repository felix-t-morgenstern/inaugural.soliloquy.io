package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.ImageAssetSetRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetRenderableHandlerTests
        extends AbstractImageAssetRenderableHandlerTests<ImageAssetSet> {
    private final long ANIMATION_START = randomLong();
    private final String DISPLAY_PARAM_KEY = randomString();
    private final String DISPLAY_PARAM_VAL = randomString();

    @Mock private ImageAssetSetRenderable mockRenderable;
    @Mock private ImageAssetSetRenderableFactory mockFactory;

    private String writtenValue;

    private TypeHandler<ImageAssetSetRenderable> handler;

    @BeforeEach
    public void setUp() {
        super.setUp(ImageAssetSet.class);

        writtenValue = String.format(
                "{\"displayParams\":[{\"key\":\"%s\",\"val\":\"%s\"}],\"animationStart\":%d," +
                        "\"assetId\":\"%s\",\"borderThickness\":\"%s\",\"borderColor\":\"%s\"," +
                        "\"colorShifts\":[\"%s\"],\"area\":\"%s\",\"onPress\":[{\"button\":%d," +
                        "\"actionId\":\"%s\"}],\"onRelease\":[{\"button\":%d," +
                        "\"actionId\":\"%s\"}],\"mouseOver\":\"%s\",\"mouseLeave\":\"%s\"," +
                        "\"z\":%d,\"uuid\":\"%s\",\"type\":\"%s\"}",
                DISPLAY_PARAM_KEY, DISPLAY_PARAM_VAL, ANIMATION_START, ASSET_ID, BORDER_THICKNESS,
                BORDER_COLOR, COLOR_SHIFT, AREA, ON_PRESS_BUTTON, ON_PRESS_CONSUMER_ID,
                ON_RELEASE_BUTTON, ON_RELEASE_CONSUMER_ID, ON_MOUSE_OVER_CONSUMER_ID,
                ON_MOUSE_LEAVE_CONSUMER_ID, Z, UUID, mockRenderable.getClass().getCanonicalName());

        handler = new ImageAssetSetRenderableHandler(mockGetAsset, MOCK_GET_CONSUMER,
                mockProviderHandler, mockShiftHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableHandler(null, MOCK_GET_CONSUMER, mockProviderHandler,
                        mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableHandler(mockGetAsset, null, mockProviderHandler,
                        mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableHandler(mockGetAsset, MOCK_GET_CONSUMER, null,
                        mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableHandler(mockGetAsset, MOCK_GET_CONSUMER,
                        mockProviderHandler, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableHandler(mockGetAsset, MOCK_GET_CONSUMER,
                        mockProviderHandler, mockShiftHandler, null));
    }

    @Test
    public void testWrite() {
        setUpMockRenderable(mockRenderable);
        when(mockRenderable.getImageAssetSet()).thenReturn(mockAsset);
        var mockDisplayParams = generateMockMap(pairOf(DISPLAY_PARAM_KEY, DISPLAY_PARAM_VAL));
        when(mockRenderable.displayParams()).thenReturn(mockDisplayParams);
        when(mockRenderable.getAnimationStart()).thenReturn(ANIMATION_START);

        var output = handler.write(mockRenderable);

        assertEquals(writtenValue, output);
        verify(mockRenderable, once()).getImageAssetSet();
        verify(mockRenderable, once()).displayParams();
        verifyWritten(mockRenderable);
        verify(mockAsset, once()).id();
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
                any(),
                anyInt(),
                any(),
                any()
        )).thenReturn(mockRenderable);

        var output = handler.read(writtenValue);

        assertSame(mockRenderable, output);
        verifyRead();
        verify(mockFactory, once()).make(
                same(mockAsset),
                eq(mapOf(pairOf(DISPLAY_PARAM_KEY, DISPLAY_PARAM_VAL))),
                same(mockBorderThicknessProvider),
                same(mockBorderColorProvider),
                eq(mapOf(pairOf(ON_PRESS_BUTTON, MOCK_ON_PRESS_CONSUMER))),
                eq(mapOf(pairOf(ON_RELEASE_BUTTON, MOCK_ON_RELEASE_CONSUMER))),
                same(MOCK_ON_MOUSE_OVER_CONSUMER),
                same(MOCK_ON_MOUSE_LEAVE_CONSUMER),
                eq(listOf(mockShift)),
                same(mockAreaProvider),
                eq(Z),
                eq(UUID),
                isNull()
        );
        verify(mockRenderable, once()).setAnimationStart(ANIMATION_START);
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
