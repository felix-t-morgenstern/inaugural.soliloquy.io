package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomFloat;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteLinearMovingProviderFactoryImplTests {
    private final String FACTORY_1_TYPE_NAME = Float.class.getCanonicalName();

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();

    @Mock private FiniteLinearMovingProvider<Float> mockFactory1Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<Long, Function<Long,
            FiniteLinearMovingProvider>>>> FACTORY_1 =
            uuid -> valuesAtTime -> pausedTimestamp -> mostRecentTimestamp -> {
                factory1InputUuid = uuid;
                factory1InputValuesAtTimes = valuesAtTime;
                factory1InputPausedTimestamp = pausedTimestamp;
                factory1InputMostRecentTimestamp = mostRecentTimestamp;
                return mockFactory1Output;
            };

    @Mock private FiniteLinearMovingProvider<FloatBox> mockFactory2Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<Long, Function<Long,
            FiniteLinearMovingProvider>>>> FACTORY_2 =
            uuid -> valuesAtTime -> pausedTimestamp -> mostRecentTimestamp -> {
                factory2InputUuid = uuid;
                factory2InputValuesAtTimes = valuesAtTime;
                factory2InputPausedTimestamp = pausedTimestamp;
                factory2InputMostRecentTimestamp = mostRecentTimestamp;
                return mockFactory2Output;
            };
    
    private UUID factory1InputUuid;
    /** @noinspection rawtypes */
    private Map factory1InputValuesAtTimes;
    private Long factory1InputPausedTimestamp;
    private Long factory1InputMostRecentTimestamp;

    @SuppressWarnings("unused")
    private UUID factory2InputUuid;
    /** @noinspection rawtypes, unused */
    private Map factory2InputValuesAtTimes;
    @SuppressWarnings("unused")
    private Long factory2InputPausedTimestamp;
    @SuppressWarnings("unused")
    private Long factory2InputMostRecentTimestamp;

    private FiniteLinearMovingProviderFactory finiteLinearMovingProviderFactory;

    @BeforeEach
    public void setUp() {
        finiteLinearMovingProviderFactory = new FiniteLinearMovingProviderFactoryImpl(
                mapOf(
                        pairOf(FACTORY_1_TYPE_NAME, FACTORY_1),
                        pairOf(FACTORY_2_TYPE_NAME, FACTORY_2)
                ));
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(mapOf()));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(
                        mapOf(pairOf(null, FACTORY_1))));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(
                        mapOf(pairOf("", FACTORY_1))));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingProviderFactoryImpl(
                        mapOf(pairOf(FACTORY_1_TYPE_NAME, null))));
    }

    @Test
    public void testMake() {
        var uuid = UUID.randomUUID();
        var valuesAtTimestamps = mapOf(
                pairOf(0L, randomFloat()),
                pairOf(1L, randomFloat()),
                pairOf(2L, randomFloat())
        );
        Long pausedTimestamp = randomLong();
        Long mostRecentTimestamp = pausedTimestamp + 1;

        FiniteLinearMovingProvider<Float> provider = finiteLinearMovingProviderFactory
                .make(uuid, valuesAtTimestamps, pausedTimestamp, mostRecentTimestamp);

        assertSame(mockFactory1Output, provider);
        assertSame(uuid, factory1InputUuid);
        assertSame(valuesAtTimestamps, factory1InputValuesAtTimes);
        assertEquals(pausedTimestamp, factory1InputPausedTimestamp);
        assertEquals(mostRecentTimestamp, factory1InputMostRecentTimestamp);
    }

    // NB: No specific test is provided for make with invalid params, since the individual
    //     factories provided to this class should handle those edge cases.
}
