package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;

import java.util.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomFloat;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteSinusoidMovingProviderFactoryImplTests {
    private final String FACTORY_1_TYPE_NAME = Float.class.getCanonicalName();
    /** @noinspection rawtypes */
    @Mock private FiniteSinusoidMovingProvider factory1Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<List<Float>, Function<Long, Function<Long,
            FiniteSinusoidMovingProvider>>>>> FACTORY_1 =
            uuid -> valuesAtTime -> sharpnesses -> pausedTimestamp -> mostRecentTimestamp -> {
                factory1InputUuid = uuid;
                factory1InputValuesAtTimes = valuesAtTime;
                factory1InputTransitionSharpnesses = sharpnesses;
                factory1InputPausedTimestamp = pausedTimestamp;
                factory1InputMostRecentTimestamp = mostRecentTimestamp;
                factory1Output = mock(FiniteSinusoidMovingProvider.class);
                return factory1Output;
            };

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();
    /** @noinspection rawtypes */
    @Mock private FiniteSinusoidMovingProvider factory2Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<List<Float>, Function<Long, Function<Long,
            FiniteSinusoidMovingProvider>>>>> FACTORY_2 =
            uuid -> valuesAtTime -> sharpnesses -> pausedTimestamp -> mostRecentTimestamp -> {
                factory2InputUuid = uuid;
                factory2InputValuesAtTimes = valuesAtTime;
                factory2InputTransitionSharpnesses = sharpnesses;
                factory2InputPausedTimestamp = pausedTimestamp;
                factory2InputMostRecentTimestamp = mostRecentTimestamp;
                factory2Output = mock(FiniteSinusoidMovingProvider.class);
                return factory2Output;
            };

    private UUID factory1InputUuid;
    /** @noinspection rawtypes */
    private Map factory1InputValuesAtTimes;
    private List<Float> factory1InputTransitionSharpnesses;
    private Long factory1InputPausedTimestamp;
    private Long factory1InputMostRecentTimestamp;

    @SuppressWarnings("unused")
    private UUID factory2InputUuid;
    /** @noinspection rawtypes, unused */
    private Map factory2InputValuesAtTimes;
    private List<Float> factory2InputTransitionSharpnesses;
    @SuppressWarnings("unused")
    private Long factory2InputPausedTimestamp;
    @SuppressWarnings("unused")
    private Long factory2InputMostRecentTimestamp;

    private FiniteSinusoidMovingProviderFactory finiteSinusoidMovingProviderFactory;

    @BeforeEach
    public void setUp() {
        finiteSinusoidMovingProviderFactory = new FiniteSinusoidMovingProviderFactoryImpl(
                mapOf(
                        pairOf(FACTORY_1_TYPE_NAME, FACTORY_1),
                        pairOf(FACTORY_2_TYPE_NAME, FACTORY_2)
                ));
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(mapOf()));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(mapOf(pairOf(null, FACTORY_1))));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(mapOf(pairOf("", FACTORY_1))));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(
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
        var transitionSharpnesses = listOf(randomFloat(), randomFloat());
        var pausedTimestamp = randomLong();
        var mostRecentTimestamp = pausedTimestamp + 1;

        var provider = finiteSinusoidMovingProviderFactory.make(uuid, valuesAtTimestamps,
                transitionSharpnesses, pausedTimestamp, mostRecentTimestamp);

        assertNotNull(provider);
        assertSame(factory1Output, provider);
        assertSame(uuid, factory1InputUuid);
        assertSame(valuesAtTimestamps, factory1InputValuesAtTimes);
        assertSame(transitionSharpnesses, factory1InputTransitionSharpnesses);
        assertEquals(pausedTimestamp, factory1InputPausedTimestamp);
        assertEquals(mostRecentTimestamp, factory1InputMostRecentTimestamp);
    }

    // NB: No specific test is provided for make with invalid params, since the individual
    //     factories provided to this class should handle those edge cases.

}
