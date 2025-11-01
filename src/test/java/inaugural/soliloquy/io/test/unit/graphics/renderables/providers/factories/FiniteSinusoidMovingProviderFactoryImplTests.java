package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;

import java.util.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomFloat;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteSinusoidMovingProviderFactoryImplTests {
    /** @noinspection rawtypes */
    @Mock private FiniteSinusoidMovingProvider factory1Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<float[], Function<Long, Function<TimestampValidator, FiniteSinusoidMovingProvider>>>>> FACTORY_1 =
            uuid -> valuesAtTime -> sharpnesses -> pausedTimestamp -> mostRecentTimestamp -> {
                factory1InputUuid = uuid;
                factory1InputValuesAtTimes = valuesAtTime;
                factory1InputTransitionSharpnesses = sharpnesses;
                factory1InputPausedTimestamp = pausedTimestamp;
                factory1InputValidator = mostRecentTimestamp;
                factory1Output = mock(FiniteSinusoidMovingProvider.class);
                return factory1Output;
            };

    private final String FACTORY_2_TYPE_NAME = FloatBox.class.getCanonicalName();
    /** @noinspection rawtypes */
    @Mock private FiniteSinusoidMovingProvider factory2Output;
    /** @noinspection rawtypes */
    private final Function<UUID, Function<Map, Function<float[], Function<Long, Function<TimestampValidator, FiniteSinusoidMovingProvider>>>>> FACTORY_2 =
            uuid -> valuesAtTime -> sharpnesses -> pausedTimestamp -> mostRecentTimestamp -> {
                factory2InputUuid = uuid;
                factory2InputValuesAtTimes = valuesAtTime;
                factory2InputTransitionSharpnesses = sharpnesses;
                factory2InputPausedTimestamp = pausedTimestamp;
                factory2InputValidator = mostRecentTimestamp;
                factory2Output = mock(FiniteSinusoidMovingProvider.class);
                return factory2Output;
            };

    private UUID factory1InputUuid;
    /** @noinspection rawtypes */
    private Map factory1InputValuesAtTimes;
    private float[] factory1InputTransitionSharpnesses;
    private Long factory1InputPausedTimestamp;
    private TimestampValidator factory1InputValidator;

    @SuppressWarnings("unused")
    private UUID factory2InputUuid;
    /** @noinspection rawtypes, unused */
    private Map factory2InputValuesAtTimes;
    private float[] factory2InputTransitionSharpnesses;
    @SuppressWarnings("unused")
    private Long factory2InputPausedTimestamp;
    @SuppressWarnings("unused")
    private TimestampValidator factory2InputValidator;

    @Mock private TimestampValidator mockTimestampValidator;

    private FiniteSinusoidMovingProviderFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new FiniteSinusoidMovingProviderFactoryImpl(
                mapOf(
                        pairOf(Float.class, FACTORY_1),
                        pairOf(Integer.class, FACTORY_2)
                ), mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(mapOf(), mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(mapOf(pairOf(null, FACTORY_1)), mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(
                        mapOf(pairOf(Object.class, null)), mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingProviderFactoryImpl(
                        mapOf(pairOf(Object.class, FACTORY_1)), null));
    }

    @Test
    public void testMake() {
        var uuid = UUID.randomUUID();
        var valuesAtTimestamps = mapOf(
                pairOf(0L, randomFloat()),
                pairOf(1L, randomFloat()),
                pairOf(2L, randomFloat())
        );
        var transitionSharpnesses = arrayFloats(randomFloat(), randomFloat());
        var pausedTimestamp = randomLong();

        var provider = factory.make(uuid, valuesAtTimestamps, transitionSharpnesses, pausedTimestamp);

        assertNotNull(provider);
        assertSame(factory1Output, provider);
        assertSame(uuid, factory1InputUuid);
        assertSame(valuesAtTimestamps, factory1InputValuesAtTimes);
        assertSame(transitionSharpnesses, factory1InputTransitionSharpnesses);
        assertEquals(pausedTimestamp, factory1InputPausedTimestamp);
        assertSame(mockTimestampValidator, factory1InputValidator);
    }

    // NB: No specific test is provided for make with invalid params, since the individual
    //     factories provided to this class should handle those edge cases.

}
