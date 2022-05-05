package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.LoopingLinearMovingColorProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoopingLinearMovingColorProviderHandlerTests {
    private final HashMap<Integer, Color> VALUES_WITHIN_PERIOD = new HashMap<Integer, Color>() {{
        put(111, Color.RED);
        put(222, Color.GREEN);
        put(333, Color.BLUE);
    }};
    private final int PERIOD_DURATION = 444;
    private final int PERIOD_MODULO_OFFSET = 555;
    private final Long PAUSED_TIMESTAMP = 666L;
    private final long MOST_RECENT_TIMESTAMP = 777L;
    private final ArrayList<Boolean> HUE_MOVEMENT_IS_CLOCKWISE = new ArrayList<Boolean>() {{
        add(true);
        add(false);
        add(false);
    }};

    private final String MOCK_UUID_HANDLER_DATA = "mockUuidHandlerData";

    @Mock
    private TypeHandler<EntityUuid> _mockUuidHandler;

    @Mock
    private EntityUuid _mockUuid;

    @Mock
    private LoopingLinearMovingColorProvider _mockLoopingLinearMovingColorProvider;

    @Mock
    private LoopingLinearMovingColorProviderFactory _mockLoopingLinearMovingColorProviderFactory;

    private TypeHandler<LoopingLinearMovingColorProvider> _loopingLinearMovingColorProviderHandler;

    private final String WRITTEN_DATA = "{\"uuid\":\"mockUuidHandlerData\",\"periodTimestamps\":[333,222,111],\"periodValues\":[{\"r\":0,\"g\":0,\"b\":255,\"a\":255},{\"r\":0,\"g\":255,\"b\":0,\"a\":255},{\"r\":255,\"g\":0,\"b\":0,\"a\":255}],\"hueMovementIsClockwise\":[true,false,false],\"periodDuration\":444,\"periodModuloOffset\":555,\"pausedTimestamp\":666,\"mostRecentTimestamp\":777}";

    @BeforeEach
    void setUp() {
        _mockUuid = mock(EntityUuid.class);

        //noinspection unchecked
        _mockUuidHandler = mock(TypeHandler.class);
        when(_mockUuidHandler.read(anyString())).thenReturn(_mockUuid);
        when(_mockUuidHandler.write(any())).thenReturn(MOCK_UUID_HANDLER_DATA);

        _mockLoopingLinearMovingColorProvider = mock(LoopingLinearMovingColorProvider.class);
        when(_mockLoopingLinearMovingColorProvider.uuid())
                .thenReturn(_mockUuid);
        when(_mockLoopingLinearMovingColorProvider.valuesWithinPeriod())
                .thenReturn(VALUES_WITHIN_PERIOD);
        when(_mockLoopingLinearMovingColorProvider.periodDuration())
                .thenReturn(PERIOD_DURATION);
        when(_mockLoopingLinearMovingColorProvider.periodModuloOffset())
                .thenReturn(PERIOD_MODULO_OFFSET);
        when(_mockLoopingLinearMovingColorProvider.pausedTimestamp())
                .thenReturn(PAUSED_TIMESTAMP);
        when(_mockLoopingLinearMovingColorProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);
        when(_mockLoopingLinearMovingColorProvider.hueMovementIsClockwise())
                .thenReturn(HUE_MOVEMENT_IS_CLOCKWISE);

        _mockLoopingLinearMovingColorProviderFactory =
                mock(LoopingLinearMovingColorProviderFactory.class);
        when(_mockLoopingLinearMovingColorProviderFactory
                .make(any(), anyMap(), anyList(), anyInt(), anyInt(), anyLong(), anyLong()))
                .thenReturn(_mockLoopingLinearMovingColorProvider);

        _loopingLinearMovingColorProviderHandler =
                new LoopingLinearMovingColorProviderHandler(
                        _mockLoopingLinearMovingColorProviderFactory, _mockUuidHandler);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderHandler(
                        null, _mockUuidHandler));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingColorProviderHandler(
                        _mockLoopingLinearMovingColorProviderFactory, null));
    }

    @Test
    void testWrite() {
        String writtenOutput = _loopingLinearMovingColorProviderHandler
                .write(_mockLoopingLinearMovingColorProvider);

        assertEquals(WRITTEN_DATA, writtenOutput);

        verify(_mockUuidHandler).write(_mockUuid);
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingColorProviderHandler.write(null));
    }

    @Test
    void testRead() {
        LoopingLinearMovingColorProvider output =
                _loopingLinearMovingColorProviderHandler.read(WRITTEN_DATA);

        assertSame(_mockLoopingLinearMovingColorProvider, output);
        verify(_mockLoopingLinearMovingColorProviderFactory)
                .make(_mockUuid, VALUES_WITHIN_PERIOD, HUE_MOVEMENT_IS_CLOCKWISE, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);
        verify(_mockUuidHandler).read(MOCK_UUID_HANDLER_DATA);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingColorProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingColorProviderHandler.read(""));
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_loopingLinearMovingColorProviderHandler.getArchetype());
        assertEquals(LoopingLinearMovingColorProvider.class.getCanonicalName(),
                _loopingLinearMovingColorProviderHandler.getArchetype().getInterfaceName());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                LoopingLinearMovingColorProvider.class.getCanonicalName() + ">",
                _loopingLinearMovingColorProviderHandler.getInterfaceName());
    }
}
