package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.ComponentHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;
import soliloquy.specs.ui.EventInputs;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.io.input.keyboard.KeyBinding.keyBinding;

@ExtendWith(MockitoExtension.class)
public class ComponentHandlerTests {
    private final UUID UUID = randomUUID();
    private final int Z = randomInt();
    private final int KEY = randomInt();
    private final boolean OVERRIDES = randomBoolean();
    private final int KEY_PRIORITY = randomInt();
    private final String ON_KEY_PRESS_ID = randomString();
    private final String ON_KEY_RELEASE_ID = randomString();
    @SuppressWarnings("rawtypes") private final LookupAndEntitiesWithId<Action>
            MOCK_ACTIONS_AND_LOOKUP =
            generateMockLookupFunctionWithId(Action.class, ON_KEY_PRESS_ID, ON_KEY_RELEASE_ID);
    @SuppressWarnings("unchecked") private final Action<EventInputs> MOCK_ON_KEY_PRESS =
            MOCK_ACTIONS_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("unchecked") private final Action<EventInputs> MOCK_ON_KEY_RELEASE =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(1);
    @SuppressWarnings("rawtypes")
    private final Function<String, Action> MOCK_GET_ACTION = MOCK_ACTIONS_AND_LOOKUP.lookup;

    private final String DIMENS_WRITTEN = randomString();
    private final String DATA_WRITTEN = randomString();
    private final String CONTENT_WRITTEN = randomString();

    @Mock private ProviderAtTime<FloatBox> mockDimensProvider;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<ProviderAtTime> mockProviderHandler;
    @Mock private Renderable mockContent;
    @Mock private TypeHandler<Renderable> mockContentHandler;
    @Mock private Map<String, Object> mockData;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<Map> mockDataHandler;
    @Mock private PersistenceHandler mockPersistenceHandler;
    @Mock private Function<Component, Integer> mockGetKeyEventPriority;
    @Mock private Component mockComponent;
    @Mock private ComponentFactory mockFactory;

    private String writtenValue;

    private TypeHandler<Component> handler;

    @BeforeEach
    public void setUp() {
        hydrateMockHandler(
                mockProviderHandler,
                pairOf(mockDimensProvider, DIMENS_WRITTEN)
        );
        hydrateMockHandler(
                mockDataHandler,
                pairOf(mockData, DATA_WRITTEN)
        );
        hydrateMockHandler(
                mockContentHandler,
                pairOf(mockContent, CONTENT_WRITTEN)
        );
        //noinspection unchecked,rawtypes
        lenient().when(mockPersistenceHandler.getTypeHandler(anyString()))
                .thenReturn((TypeHandler) mockContentHandler);

        writtenValue = String.format(
                "{\"uuid\":\"%s\",\"bindings\":[{\"keys\":[%s],\"onPress\":\"%s\"," +
                        "\"onRelease\":\"%s\"}],\"overrides\":%s,\"priority\":%d," +
                        "\"dimens\":\"%s\",\"content\":[{\"type\":\"%s\",\"content\":\"%s\"}]," +
                        "\"data\":\"%s\",\"z\":%d}",
                UUID, KEY, ON_KEY_PRESS_ID, ON_KEY_RELEASE_ID, OVERRIDES, KEY_PRIORITY,
                DIMENS_WRITTEN, mockContent.getClass().getCanonicalName(), CONTENT_WRITTEN,
                DATA_WRITTEN, Z
        );

        handler = new ComponentHandler(mockProviderHandler, mockDataHandler, mockPersistenceHandler,
                MOCK_GET_ACTION, mockGetKeyEventPriority, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentHandler(null, mockDataHandler, mockPersistenceHandler,
                        MOCK_GET_ACTION, mockGetKeyEventPriority, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentHandler(mockProviderHandler, null, mockPersistenceHandler,
                        MOCK_GET_ACTION, mockGetKeyEventPriority, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentHandler(mockProviderHandler, mockDataHandler,
                        mockPersistenceHandler, null, mockGetKeyEventPriority, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentHandler(mockProviderHandler, mockDataHandler, null,
                        MOCK_GET_ACTION, mockGetKeyEventPriority, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentHandler(mockProviderHandler, mockDataHandler,
                        mockPersistenceHandler, MOCK_GET_ACTION, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentHandler(mockProviderHandler, mockDataHandler,
                        mockPersistenceHandler, MOCK_GET_ACTION, mockGetKeyEventPriority, null));
    }

    @Test
    public void testWrite() {
        when(mockComponent.uuid()).thenReturn(UUID);
        when(mockComponent.getZ()).thenReturn(Z);
        when(mockComponent.contentsRepresentation()).thenReturn(setOf(mockContent));
        when(mockComponent.keyBindings()).thenReturn(setOf(keyBinding(arrayInts(KEY),
                MOCK_ON_KEY_PRESS, MOCK_ON_KEY_RELEASE)));
        when(mockComponent.blocksLowerKeyBindings()).thenReturn(OVERRIDES);
        when(mockComponent.getRenderingBoundariesProvider()).thenReturn(mockDimensProvider);
        when(mockComponent.data()).thenReturn(mockData);
        when(mockGetKeyEventPriority.apply(any())).thenReturn(KEY_PRIORITY);

        var output = handler.write(mockComponent);

        assertEquals(writtenValue, output);
        verify(MOCK_ON_KEY_PRESS, once()).id();
        verify(MOCK_ON_KEY_RELEASE, once()).id();
        verify(mockComponent, once()).getZ();
        verify(mockComponent, once()).uuid();
        verify(mockComponent, once()).contentsRepresentation();
        verify(mockPersistenceHandler, once()).getTypeHandler(
                mockContent.getClass().getCanonicalName());
        verify(mockContentHandler, once()).write(mockContent);
        verify(mockComponent, once()).getRenderingBoundariesProvider();
        verify(mockProviderHandler, once()).write(mockDimensProvider);
        verify(mockComponent, once()).data();
        verify(mockDataHandler, once()).write(mockData);
        verify(mockGetKeyEventPriority, once()).apply(mockComponent);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(any(), anyInt(), any(), anyBoolean(), anyInt(), any(), any(), any()))
                .thenReturn(mockComponent);

        var output = handler.read(writtenValue);

        assertNotNull(output);
        assertSame(mockComponent, output);
        var bindingCaptor = ArgumentCaptor.forClass(Set.class);
        //noinspection unchecked
        verify(mockFactory, once()).make(
                eq(UUID),
                eq(Z),
                bindingCaptor.capture(),
                eq(OVERRIDES),
                eq(KEY_PRIORITY),
                same(mockDimensProvider),
                isNull(),
                same(mockData)
        );
        verify(mockProviderHandler, once()).read(DIMENS_WRITTEN);
        verify(mockPersistenceHandler, once()).getTypeHandler(
                mockContent.getClass().getCanonicalName());
        verify(mockContentHandler, once()).read(CONTENT_WRITTEN);
        verify(mockDataHandler, once()).read(DATA_WRITTEN);
        verify(mockComponent, once()).add(mockContent);
        @SuppressWarnings("unchecked") var bindings = (Set<KeyBinding>) bindingCaptor.getValue();
        assertEquals(1, bindings.size());
        @SuppressWarnings("OptionalGetWithoutIsPresent") var binding =
                bindings.stream().findFirst().get();
        assertEquals(1, binding.BOUND_CODEPOINTS.length);
        assertEquals(KEY, binding.BOUND_CODEPOINTS[0]);
        verify(MOCK_GET_ACTION, once()).apply(ON_KEY_PRESS_ID);
        assertSame(MOCK_ON_KEY_PRESS, binding.ON_PRESS);
        verify(MOCK_GET_ACTION, once()).apply(ON_KEY_RELEASE_ID);
        assertSame(MOCK_ON_KEY_RELEASE, binding.ON_RELEASE);
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
