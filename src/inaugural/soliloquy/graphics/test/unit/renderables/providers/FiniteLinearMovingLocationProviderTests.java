package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingLocationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.factories.PairFactory;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.RANDOM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

class FiniteLinearMovingLocationProviderTests {
    private final long TIMESTAMP_1 = 100L;
    private final long TIMESTAMP_2 = 200L;
    private final long TIMESTAMP_3 = 300L;

    private final float X_1 = RANDOM.nextFloat();
    private final float X_2 = RANDOM.nextFloat();
    private final float X_3 = RANDOM.nextFloat();

    private final float Y_1 = RANDOM.nextFloat();
    private final float Y_2 = RANDOM.nextFloat();
    private final float Y_3 = RANDOM.nextFloat();

    private final HashMap<Long, Pair<Float, Float>> VALUES_AT_TIMES = new HashMap<>();

    private final Long PAUSED_TIMESTAMP = 175L;
    private final Long MOST_RECENT_TIMESTAMP = 50L;

    private final float WEIGHT_TIMESTAMP_1 = 0.25f;
    private final float WEIGHT_TIMESTAMP_2 = 1.0f - WEIGHT_TIMESTAMP_1;
    private final long TIMESTAMP =
            TIMESTAMP_1 + ((long) (WEIGHT_TIMESTAMP_2 * (TIMESTAMP_2 - TIMESTAMP_1)));
    private final float EXPECTED_X = (WEIGHT_TIMESTAMP_1 * X_1) + (WEIGHT_TIMESTAMP_2 * X_2);
    private final float EXPECTED_Y = (WEIGHT_TIMESTAMP_1 * Y_1) + (WEIGHT_TIMESTAMP_2 * Y_2);

    private static final UUID UUID = java.util.UUID.randomUUID();
    @Mock private PairFactory _mockPairFactory;
    @Mock private Pair<Float, Float> _mockPairFactoryOutput;
    @Mock private Pair<Float, Float> _mockPair1;
    @Mock private Pair<Float, Float> _mockPair2;
    @Mock private Pair<Float, Float> _mockPair3;

    private FiniteLinearMovingProvider<Pair<Float, Float>> _finiteLinearMovingLocationProvider;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        _mockPairFactoryOutput = mock(Pair.class);

        //noinspection unchecked
        _mockPair1 = mock(Pair.class);
        when(_mockPair1.getItem1()).thenReturn(X_1);
        when(_mockPair1.getItem2()).thenReturn(Y_1);
        //noinspection unchecked
        _mockPair2 = mock(Pair.class);
        when(_mockPair2.getItem1()).thenReturn(X_2);
        when(_mockPair2.getItem2()).thenReturn(Y_2);
        //noinspection unchecked
        _mockPair3 = mock(Pair.class);
        when(_mockPair3.getItem1()).thenReturn(X_3);
        when(_mockPair3.getItem2()).thenReturn(Y_3);

        VALUES_AT_TIMES.put(TIMESTAMP_1, _mockPair1);
        VALUES_AT_TIMES.put(TIMESTAMP_2, _mockPair2);
        VALUES_AT_TIMES.put(TIMESTAMP_3, _mockPair3);

        _mockPairFactory = mock(PairFactory.class);
        when(_mockPairFactory.make(anyFloat(), anyFloat())).thenReturn(_mockPairFactoryOutput);

        _finiteLinearMovingLocationProvider = new FiniteLinearMovingLocationProvider(UUID,
                VALUES_AT_TIMES, null, null, _mockPairFactory);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingLocationProvider(null, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP, _mockPairFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingLocationProvider(UUID, null,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP, _mockPairFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingLocationProvider(UUID, new HashMap<>(),
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP, _mockPairFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingLocationProvider(UUID, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, null, _mockPairFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingLocationProvider(UUID, VALUES_AT_TIMES,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP, _mockPairFactory));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingLocationProvider(UUID, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP, null));
    }

    @Test
    void testUuid() {
        assertSame(UUID, _finiteLinearMovingLocationProvider.uuid());
    }

    @Test
    void testPausedTimestamp() {
        _finiteLinearMovingLocationProvider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, _finiteLinearMovingLocationProvider.pausedTimestamp());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                new FiniteLinearMovingLocationProvider(UUID, VALUES_AT_TIMES, null,
                        MOST_RECENT_TIMESTAMP, _mockPairFactory)
                        .mostRecentTimestamp());
    }

    @Test
    void testProvide() {
        Pair<Float, Float> providedValue = _finiteLinearMovingLocationProvider.provide(TIMESTAMP);

        assertSame(_mockPairFactoryOutput, providedValue);
        verify(_mockPairFactory).make(EXPECTED_X, EXPECTED_Y);
    }


    @Test
    void testProvideBeforeStartOfRange() {
        Pair<Float, Float> providedValue =
                _finiteLinearMovingLocationProvider.provide(Long.MIN_VALUE);

        assertSame(_mockPair1, providedValue);
        verify(_mockPairFactory, never()).make(anyFloat(), anyFloat());
    }

    @Test
    void testProvideAfterEndOfRange() {
        Pair<Float, Float> providedValue =
                _finiteLinearMovingLocationProvider.provide(Long.MAX_VALUE);

        assertSame(_mockPair3, providedValue);
        verify(_mockPairFactory, never()).make(anyFloat(), anyFloat());
    }

    @Test
    void testProvideWhilePaused() {
        _finiteLinearMovingLocationProvider.reportPause(TIMESTAMP);
        Pair<Float, Float> providedValue =
                _finiteLinearMovingLocationProvider.provide(Long.MAX_VALUE);

        assertSame(_mockPairFactoryOutput, providedValue);
        verify(_mockPairFactory).make(EXPECTED_X, EXPECTED_Y);
    }


    @Test
    void testUnpauseUpdatesStartingTime() {
        long pauseDuration = 123123L;

        _finiteLinearMovingLocationProvider.reportPause(PAUSED_TIMESTAMP);
        _finiteLinearMovingLocationProvider.reportUnpause(PAUSED_TIMESTAMP + pauseDuration);
        Pair<Float, Float> providedValue =
                _finiteLinearMovingLocationProvider.provide(TIMESTAMP + pauseDuration);

        assertSame(_mockPairFactoryOutput, providedValue);
        verify(_mockPairFactory).make(EXPECTED_X, EXPECTED_Y);
    }

    @Test
    void testOutdatedTimestamp() {
        _finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingLocationProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingLocationProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP - 1));
    }

    @Test
    void testMostRecentTimestampUpdates() {
        _finiteLinearMovingLocationProvider.reportPause(MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _finiteLinearMovingLocationProvider.mostRecentTimestamp());

        _finiteLinearMovingLocationProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2);

        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _finiteLinearMovingLocationProvider.mostRecentTimestamp());

        _finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP + 3);

        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) _finiteLinearMovingLocationProvider.mostRecentTimestamp());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_finiteLinearMovingLocationProvider.getArchetype());
        assertNotNull(_finiteLinearMovingLocationProvider.getArchetype().getFirstArchetype());
        assertNotNull(_finiteLinearMovingLocationProvider.getArchetype().getSecondArchetype());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingProvider.class.getCanonicalName() + "<" +
                Pair.class.getCanonicalName() + "<" + Float.class.getCanonicalName() + "," +
                Float.class.getCanonicalName() + ">>",
                _finiteLinearMovingLocationProvider.getInterfaceName());
    }
}
