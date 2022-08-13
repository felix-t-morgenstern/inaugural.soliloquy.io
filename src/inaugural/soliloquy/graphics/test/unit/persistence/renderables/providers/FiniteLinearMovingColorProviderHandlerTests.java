package inaugural.soliloquy.graphics.test.unit.persistence.renderables.providers;

import inaugural.soliloquy.graphics.persistence.renderables.providers.FiniteLinearMovingColorProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FiniteLinearMovingColorProviderHandlerTests {
    private static final Long TIMESTAMP_1 = 123L;
    private static final Long TIMESTAMP_2 = 456L;
    private static final Long TIMESTAMP_3 = 789L;
    private static final Color VALUE_1 = Color.MAGENTA;
    private static final Color VALUE_2 = Color.RED;
    private static final Color VALUE_3 = Color.BLACK;
    private static final HashMap<Long, Color> COLORS_AT_TIMESTAMPS = new HashMap<Long, Color>() {{
        put(TIMESTAMP_1, VALUE_1);
        put(TIMESTAMP_2, VALUE_2);
        put(TIMESTAMP_3, VALUE_3);
    }};
    private static final ArrayList<Boolean> HUE_MOVEMENT_IS_CLOCKWISE = new ArrayList<Boolean>() {{
        add(true);
        add(false);
        add(false);
    }};
    private static final Long PAUSED_TIMESTAMP = 123L;
    private static final Long MOST_RECENT_TIMESTAMP = 456L;

    private static final String UUID_WRITE_OUTPUT = "uuidWriteOutput";

    @Mock private FiniteLinearMovingColorProvider _mockFiniteLinearMovingColorProvider;
    @Mock private FiniteLinearMovingColorProviderFactory
            _mockFiniteLinearMovingColorProviderFactory;
    @Mock private FiniteLinearMovingColorProvider _mockFiniteLinearMovingColorProviderFactoryOutput;
    @Mock private static UUID _finiteLinearMovingColorProviderInputUuid;
    @Mock private static UUID _uuidReadOutput;
    @Mock private TypeHandler<UUID> _uuidHandler;

    private TypeHandler<FiniteLinearMovingColorProvider> _finiteLinearMovingColorProviderHandler;

    private static final String WRITTEN_VALUE =
            "{\"uuid\":\"uuidWriteOutput\",\"colors\":[{\"timestamp\":123,\"r\":255,\"g\":0," +
                    "\"b\":255,\"a\":255},{\"timestamp\":456,\"r\":255,\"g\":0,\"b\":0," +
                    "\"a\":255},{\"timestamp\":789,\"r\":0,\"g\":0,\"b\":0,\"a\":255}]," +
                    "\"movementIsClockwise\":[true,false,false],\"pausedTimestamp\":123," +
                    "\"mostRecentTimestamp\":456}";

    @BeforeEach
    void setUp() {
        _mockFiniteLinearMovingColorProvider = mock(FiniteLinearMovingColorProvider.class);
        when(_mockFiniteLinearMovingColorProvider.uuid())
                .thenReturn(_finiteLinearMovingColorProviderInputUuid);
        when(_mockFiniteLinearMovingColorProvider.valuesAtTimestampsRepresentation())
                .thenReturn(COLORS_AT_TIMESTAMPS);
        when(_mockFiniteLinearMovingColorProvider.hueMovementIsClockwise())
                .thenReturn(HUE_MOVEMENT_IS_CLOCKWISE);
        when(_mockFiniteLinearMovingColorProvider.pausedTimestamp()).thenReturn(PAUSED_TIMESTAMP);
        when(_mockFiniteLinearMovingColorProvider.mostRecentTimestamp())
                .thenReturn(MOST_RECENT_TIMESTAMP);

        //noinspection unchecked
        _uuidHandler = Mockito.mock(TypeHandler.class);
        when(_uuidHandler.read(Mockito.anyString())).thenReturn(_uuidReadOutput);
        when(_uuidHandler.write(Mockito.any())).thenReturn(UUID_WRITE_OUTPUT);

        _mockFiniteLinearMovingColorProviderFactoryOutput =
                mock(FiniteLinearMovingColorProvider.class);

        _mockFiniteLinearMovingColorProviderFactory =
                mock(FiniteLinearMovingColorProviderFactory.class);
        when(_mockFiniteLinearMovingColorProviderFactory
                .make(any(), anyMap(), anyList(), anyLong(), anyLong()))
                .thenReturn(_mockFiniteLinearMovingColorProviderFactoryOutput);

        _finiteLinearMovingColorProviderHandler = new FiniteLinearMovingColorProviderHandler(
                _uuidHandler, _mockFiniteLinearMovingColorProviderFactory);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderHandler(null,
                        _mockFiniteLinearMovingColorProviderFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingColorProviderHandler(_uuidHandler,
                        null));
    }

    @Test
    void testWrite() {
        assertEquals(WRITTEN_VALUE, _finiteLinearMovingColorProviderHandler
                .write(_mockFiniteLinearMovingColorProvider));
    }

    @Test
    void testWriteWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderHandler.write(null));
    }

    @Test
    void testRead() {
        FiniteLinearMovingColorProvider readValue =
                _finiteLinearMovingColorProviderHandler.read(WRITTEN_VALUE);

        assertSame(_mockFiniteLinearMovingColorProviderFactoryOutput, readValue);
        verify(_uuidHandler, times(1)).read(UUID_WRITE_OUTPUT);
        verify(_mockFiniteLinearMovingColorProviderFactory, times(1))
                .make(_uuidReadOutput, COLORS_AT_TIMESTAMPS, HUE_MOVEMENT_IS_CLOCKWISE,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testReadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderHandler.read(null));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderHandler.read(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TypeHandler.class.getCanonicalName() + "<" +
                        FiniteLinearMovingColorProvider.class.getCanonicalName() + ">",
                _finiteLinearMovingColorProviderHandler.getInterfaceName());
    }
}
