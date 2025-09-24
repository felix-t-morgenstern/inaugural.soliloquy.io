package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.factories.FunctionalProviderFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.entities.Function;
import soliloquy.specs.io.graphics.renderables.providers.FunctionalProvider.EventInfo;
import soliloquy.specs.io.graphics.renderables.providers.factories.FunctionalProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static inaugural.soliloquy.tools.testing.Mock.LookupAndEntitiesWithId;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FunctionalProviderFactoryImplTests {
    private final UUID UUID = randomUUID();

    private final String PROVIDE_FUNCTION_ID = randomString();
    @SuppressWarnings("rawtypes") private final LookupAndEntitiesWithId<Function>
            MOCK_FUNCTION_AND_LOOKUP = generateMockLookupFunctionWithId(Function.class,
            PROVIDE_FUNCTION_ID);
    @SuppressWarnings("unchecked") private final Function<EventInfo, Integer> MOCK_PROVIDE_FUNCTION = MOCK_FUNCTION_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("rawtypes") private final java.util.function.Function<String, Function> MOCK_GET_FUNCTION = MOCK_FUNCTION_AND_LOOKUP.lookup;

    private final String PAUSE_ACTION_ID = randomString();
    private final String UNPAUSE_ACTION_ID = randomString();
    @SuppressWarnings("rawtypes") private final LookupAndEntitiesWithId<Action> MOCK_ACTIONS_AND_LOOKUP = generateMockLookupFunctionWithId(Action.class, PAUSE_ACTION_ID, UNPAUSE_ACTION_ID);
    @SuppressWarnings("unchecked") private final Action<EventInfo> MOCK_PAUSE_ACTION = MOCK_ACTIONS_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("unchecked") private final Action<EventInfo> MOCK_UNPAUSE_ACTION = MOCK_ACTIONS_AND_LOOKUP.entities.get(1);
    @SuppressWarnings("rawtypes") private final java.util.function.Function<String, Action> MOCK_GET_ACTION = MOCK_ACTIONS_AND_LOOKUP.lookup;

    private final String DATA_KEY = randomString();
    private final int DATA_VAL = randomInt();
    private final Map<String, Object> DATA = mapOf(DATA_KEY, DATA_VAL);

    private final Long PAUSE_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockValidator;

    private FunctionalProviderFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new FunctionalProviderFactoryImpl(MOCK_GET_FUNCTION, MOCK_GET_ACTION, mockValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new FunctionalProviderFactoryImpl(null, MOCK_GET_ACTION, mockValidator));
        assertThrows(IllegalArgumentException.class, () -> new FunctionalProviderFactoryImpl(MOCK_GET_FUNCTION, null, mockValidator));
        assertThrows(IllegalArgumentException.class, () -> new FunctionalProviderFactoryImpl(MOCK_GET_FUNCTION, MOCK_GET_ACTION, null));
    }

    @Test
    public void testMake() {
        var output = factory.make(UUID, PROVIDE_FUNCTION_ID, PAUSE_ACTION_ID, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA);

        assertNotNull(output);
        assertEquals(UUID, output.uuid());
        assertEquals(PAUSE_TIMESTAMP, output.pausedTimestamp());
        assertEquals(DATA, output.data());
        assertNotSame(DATA, output.data());
        verify(MOCK_GET_FUNCTION, once()).apply(anyString());
        verify(MOCK_GET_FUNCTION, once()).apply(PROVIDE_FUNCTION_ID);
        verify(MOCK_GET_ACTION, times(2)).apply(anyString());
        verify(MOCK_GET_ACTION, once()).apply(PAUSE_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(UNPAUSE_ACTION_ID);

        var provideTimestamp = randomLong();
        output.provide(provideTimestamp);
        verify(mockValidator, once()).validateTimestamp(provideTimestamp);
        var provideEventInfoCapture = ArgumentCaptor.forClass(EventInfo.class);
        verify(MOCK_PROVIDE_FUNCTION, once()).apply(provideEventInfoCapture.capture());
        var provideEventInfo = provideEventInfoCapture.getValue();
        assertEquals(provideTimestamp, provideEventInfo.timestamp());
        assertEquals(PAUSE_TIMESTAMP, provideEventInfo.pauseTimestamp());
        assertEquals(DATA, provideEventInfo.data());
        assertNotSame(DATA, provideEventInfo.data());

        var unpauseTimestamp = randomLongWithInclusiveFloor(PAUSE_TIMESTAMP);
        output.reportUnpause(unpauseTimestamp);
        verify(mockValidator, once()).validateTimestamp(unpauseTimestamp);
        var unpauseEventInfoCapture = ArgumentCaptor.forClass(EventInfo.class);
        verify(MOCK_UNPAUSE_ACTION, once()).accept(unpauseEventInfoCapture.capture());
        var unpauseEventInfo = unpauseEventInfoCapture.getValue();
        assertEquals(unpauseTimestamp, unpauseEventInfo.timestamp());
        assertEquals(PAUSE_TIMESTAMP, unpauseEventInfo.pauseTimestamp());
        assertEquals(DATA, unpauseEventInfo.data());
        assertNotSame(DATA, unpauseEventInfo.data());

        var pauseTimestamp2 = randomLongWithInclusiveFloor(unpauseTimestamp);
        output.reportPause(pauseTimestamp2);
        verify(mockValidator, once()).validateTimestamp(pauseTimestamp2);
        var pauseEventInfoCapture = ArgumentCaptor.forClass(EventInfo.class);
        verify(MOCK_PAUSE_ACTION, once()).accept(pauseEventInfoCapture.capture());
        var pauseEventInfo = pauseEventInfoCapture.getValue();
        assertEquals(pauseTimestamp2, pauseEventInfo.timestamp());
        assertNull(pauseEventInfo.pauseTimestamp());
        assertEquals(DATA, pauseEventInfo.data());
        assertNotSame(DATA, pauseEventInfo.data());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        var invalidId = "I am not a valid Id!";

        assertThrows(IllegalArgumentException.class, () -> factory.make(null, PROVIDE_FUNCTION_ID, PAUSE_ACTION_ID, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, null, PAUSE_ACTION_ID, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, "", PAUSE_ACTION_ID, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, invalidId, PAUSE_ACTION_ID, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, PROVIDE_FUNCTION_ID, "", UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, PROVIDE_FUNCTION_ID, invalidId, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, PROVIDE_FUNCTION_ID, PAUSE_ACTION_ID, "", PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, PROVIDE_FUNCTION_ID, PAUSE_ACTION_ID, invalidId, PAUSE_TIMESTAMP, DATA));
        assertThrows(IllegalArgumentException.class, () -> factory.make(UUID, PROVIDE_FUNCTION_ID, PAUSE_ACTION_ID, UNPAUSE_ACTION_ID, PAUSE_TIMESTAMP, null));
    }
}
