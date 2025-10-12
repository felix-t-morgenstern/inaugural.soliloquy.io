package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FunctionalProviderImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.entities.Function;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider.Inputs;

@ExtendWith(MockitoExtension.class)
public class FunctionalProviderImplTests {
    private final UUID UUID = randomUUID();
    private final Long PAUSED_TIMESTAMP = randomLong();
    private final long TIMESTAMP = randomLong();
    private final String DATA_KEY = randomString();
    private final int DATA_VAL = randomInt();
    private final Map<String, Object> DATA = mapOf(DATA_KEY, DATA_VAL);

    @Mock Function<Inputs, Integer> mockProvide;
    @Mock Action<Inputs> mockPause;
    @Mock Action<Inputs> mockUnpause;
    @Mock private TimestampValidator mockTimestampValidator;

    private FunctionalProvider<Integer> provider;
    private FunctionalProvider<Integer> unpausedProvider;

    @BeforeEach
    public void setUp() {
        provider = new FunctionalProviderImpl<>(UUID, mockProvide, mockPause, mockUnpause, DATA,
                PAUSED_TIMESTAMP, mockTimestampValidator);

        unpausedProvider =
                new FunctionalProviderImpl<>(UUID, mockProvide, mockPause, mockUnpause, DATA, null,
                        mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FunctionalProviderImpl<>(null, mockProvide, mockPause, mockUnpause, DATA,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FunctionalProviderImpl<>(UUID, null, mockPause, mockUnpause, DATA,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FunctionalProviderImpl<>(UUID, mockProvide, mockPause, mockUnpause, null,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FunctionalProviderImpl<>(UUID, mockProvide, mockPause, mockUnpause, DATA,
                        PAUSED_TIMESTAMP, null));
    }

    @Test
    public void testProvide() {
        var val = randomInt();
        when(mockProvide.apply(any())).thenReturn(val);

        var provided = provider.provide(TIMESTAMP);

        assertEquals(val, provided);
        var inOrder = Mockito.inOrder(mockTimestampValidator, mockProvide);
        inOrder.verify(mockTimestampValidator, once()).validateTimestamp(TIMESTAMP);
        var eventInfoCapture = ArgumentCaptor.forClass(Inputs.class);
        inOrder.verify(mockProvide, once()).apply(eventInfoCapture.capture());
        var eventInfo = eventInfoCapture.getValue();
        assertNotNull(eventInfo);
        assertEquals(TIMESTAMP, eventInfo.timestamp());
        assertEquals(PAUSED_TIMESTAMP, eventInfo.pauseTimestamp());
        assertEquals(DATA, eventInfo.data());
        assertNotSame(DATA, eventInfo.data());
    }

    @Test
    public void testReportPause() {
        unpausedProvider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, unpausedProvider.pausedTimestamp());
        var inOrder = Mockito.inOrder(mockTimestampValidator, mockPause);
        inOrder.verify(mockTimestampValidator, once()).validateTimestamp(PAUSED_TIMESTAMP);
        var eventInfoCapture = ArgumentCaptor.forClass(Inputs.class);
        inOrder.verify(mockPause, once()).accept(eventInfoCapture.capture());
        var eventInfo = eventInfoCapture.getValue();
        assertNotNull(eventInfo);
        assertEquals(PAUSED_TIMESTAMP, eventInfo.timestamp());
        assertNull(eventInfo.pauseTimestamp());
        assertEquals(DATA, eventInfo.data());
        assertNotSame(DATA, eventInfo.data());
    }

    @Test
    public void testReportPauseWhilePaused() {
        assertThrows(UnsupportedOperationException.class,
                () -> provider.reportPause(PAUSED_TIMESTAMP));
    }

    @Test
    public void testReportUnpause() {
        var unpauseTimestamp = randomLongWithInclusiveFloor(PAUSED_TIMESTAMP + 1);

        provider.reportUnpause(unpauseTimestamp);

        assertNull(provider.pausedTimestamp());
        var inOrder = Mockito.inOrder(mockTimestampValidator, mockUnpause);
        inOrder.verify(mockTimestampValidator, once()).validateTimestamp(unpauseTimestamp);
        var eventInfoCapture = ArgumentCaptor.forClass(Inputs.class);
        inOrder.verify(mockUnpause, once()).accept(eventInfoCapture.capture());
        var eventInfo = eventInfoCapture.getValue();
        assertNotNull(eventInfo);
        assertEquals(unpauseTimestamp, eventInfo.timestamp());
        assertEquals(PAUSED_TIMESTAMP, eventInfo.pauseTimestamp());
        assertEquals(DATA, eventInfo.data());
        assertNotSame(DATA, eventInfo.data());
    }

    @Test
    public void testReportUnpauseWhileUnpaused() {
        assertThrows(UnsupportedOperationException.class,
                () -> unpausedProvider.reportUnpause(PAUSED_TIMESTAMP));
    }

    @Test
    public void testReportUnpauseWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> provider.reportUnpause(PAUSED_TIMESTAMP - 1));
    }

    @Test
    public void testUuid() {
        assertEquals(UUID, provider.uuid());
    }

    @Test
    public void testPausedTimestamp() {
        assertEquals(PAUSED_TIMESTAMP, provider.pausedTimestamp());
    }

    @Test
    public void testData() {
        assertEquals(DATA, provider.data());
        assertNotSame(DATA, provider.data());
    }

    @Test
    public void testRepresentation() {
        var provideId = randomString();
        when(mockProvide.id()).thenReturn(provideId);
        var pauseId = randomString();
        when(mockPause.id()).thenReturn(pauseId);
        var unpauseId = randomString();
        when(mockUnpause.id()).thenReturn(unpauseId);

        var representation = provider.representation();

        assertNotNull(representation);
        assertInstanceOf(FunctionalProviderImpl.Representation.class, representation);
        var cast = (FunctionalProviderImpl.Representation) representation;
        assertEquals(provideId, cast.provideId());
        assertEquals(pauseId, cast.pauseId());
        assertEquals(unpauseId, cast.unpauseId());
        assertEquals(PAUSED_TIMESTAMP, cast.pauseTimestamp());
        assertEquals(DATA, cast.data());
        assertNotSame(DATA, cast.data());
    }
}
