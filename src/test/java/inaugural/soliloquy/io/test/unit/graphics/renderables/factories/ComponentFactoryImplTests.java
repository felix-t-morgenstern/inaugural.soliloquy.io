package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.ComponentFactoryImpl;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.BiConsumer;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;

import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.LookupAndEntitiesWithId;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComponentFactoryImplTests {
    private final String DATA_KEY = randomString();
    private final int DATA_VAL = randomInt();
    private final Map<String, Object> DATA = mapOf(DATA_KEY, DATA_VAL);

    private final String PRERENDER_HOOK_ID = randomString();
    private final String ADD_HOOK_ID = randomString();
    @SuppressWarnings("rawtypes") private final LookupAndEntitiesWithId<BiConsumer>
            MOCK_BICONSUMER_AND_LOOKUP =
            generateMockLookupFunctionWithId(BiConsumer.class, PRERENDER_HOOK_ID, ADD_HOOK_ID);
    @SuppressWarnings("unchecked") private final BiConsumer<Component, Long>
            MOCK_PRERENDER_HOOK = MOCK_BICONSUMER_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("unchecked") private final BiConsumer<Component, Component.Addend>
            MOCK_ADD_HOOK = MOCK_BICONSUMER_AND_LOOKUP.entities.get(1);
    @SuppressWarnings("rawtypes") private final Function<String, BiConsumer> MOCK_GET_BICONSUMER =
            MOCK_BICONSUMER_AND_LOOKUP.lookup;

    @Mock private ProviderAtTime<FloatBox> mockDimensions;
    @Mock private ProviderAtTime<FloatBox> mockRenderingBoundaries;
    @Mock private Consumer<Component> mockRegisterComponent;
    @Mock private Consumer<Component> mockDeregisterComponent;
    @Mock private BiConsumer<Component, Integer> mockAddToKeyCapturing;
    @Mock private Consumer<Component> mockRemoveFromKeyCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockAddToMouseCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockRemoveFromMouseCapturing;
    @Mock private Component mockComponent;

    private ComponentFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new ComponentFactoryImpl(mockRegisterComponent, mockDeregisterComponent,
                mockAddToKeyCapturing, mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                mockRemoveFromMouseCapturing, MOCK_GET_BICONSUMER);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(null, mockDeregisterComponent, mockAddToKeyCapturing,
                        mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, MOCK_GET_BICONSUMER));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockRegisterComponent, null, mockAddToKeyCapturing,
                        mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, MOCK_GET_BICONSUMER));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockRegisterComponent, mockDeregisterComponent, null,
                        mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, MOCK_GET_BICONSUMER));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockRegisterComponent, mockDeregisterComponent,
                        mockAddToKeyCapturing, null, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, MOCK_GET_BICONSUMER));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockRegisterComponent, mockDeregisterComponent,
                        mockAddToKeyCapturing, mockRemoveFromKeyCapturing, null,
                        mockRemoveFromMouseCapturing, MOCK_GET_BICONSUMER));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockRegisterComponent, mockDeregisterComponent,
                        mockAddToKeyCapturing, mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        null, MOCK_GET_BICONSUMER));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockRegisterComponent, mockDeregisterComponent,
                        mockAddToKeyCapturing, mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, null));
    }

    @Test
    public void testMake() {
        var uuid = randomUUID();
        var z = randomInt();
        var bindings = Collections.<KeyBinding>setOf();
        var overrides = randomBoolean();
        var keyEventPriority = randomInt();
        var tier = randomInt();
        when(mockComponent.tier()).thenReturn(tier);
        var mockRenderableWithMouseEvents = mock(RenderableWithMouseEvents.class);

        var output = factory.make(uuid, z, bindings, overrides, keyEventPriority,
                mockDimensions, mockRenderingBoundaries, PRERENDER_HOOK_ID, ADD_HOOK_ID,
                mockComponent, DATA);
        when(mockRenderableWithMouseEvents.getContainingComponent()).thenReturn(output);
        output.add(mockRenderableWithMouseEvents);

        var addendCaptor = ArgumentCaptor.forClass(Component.Addend.class);
        verify(MOCK_ADD_HOOK, once()).accept(same(output), addendCaptor.capture());
        var addend = addendCaptor.getValue();
        assertSame(mockRenderableWithMouseEvents, addend.content());
        assertNull(addend.data());

        var timestamp = randomLong();
        ((ComponentImpl) output).prerenderHook(timestamp);
        verify(MOCK_PRERENDER_HOOK, once()).accept(output, timestamp);

        when(mockRenderableWithMouseEvents.getContainingComponent()).thenReturn(output);
        output.remove(mockRenderableWithMouseEvents);
        verify(mockRegisterComponent, once()).accept(output);

        assertNotNull(output);
        assertSame(bindings, output.keyBindings());
        assertEquals(overrides, output.blocksLowerKeyBindings());
        assertEquals(uuid, output.uuid());
        assertEquals(z, output.getZ());
        assertEquals(tier + 1, output.tier());
        assertSame(mockComponent, output.getContainingComponent());
        assertSame(mockDimensions, output.getDimensionsProvider());
        assertSame(mockRenderingBoundaries, output.getRenderingBoundariesProvider());
        assertEquals(DATA, output.data());
        assertNotSame(DATA, output.data());
        verify(mockAddToMouseCapturing, once()).accept(mockRenderableWithMouseEvents);
        verify(mockRemoveFromMouseCapturing, once()).accept(mockRenderableWithMouseEvents);
        verify(mockAddToKeyCapturing, once()).accept(output, keyEventPriority);
        verify(MOCK_GET_BICONSUMER, once()).apply(ADD_HOOK_ID);
        verify(MOCK_GET_BICONSUMER, once()).apply(PRERENDER_HOOK_ID);

        output.delete();

        verify(mockDeregisterComponent, once()).accept(output);
        verify(mockRemoveFromKeyCapturing, once()).accept(output);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, randomInt(), setOf(), randomBoolean(), randomInt(),
                        mockDimensions, mockRenderingBoundaries, PRERENDER_HOOK_ID, ADD_HOOK_ID,
                        mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), null, randomBoolean(), randomInt(),
                        mockDimensions, mockRenderingBoundaries, PRERENDER_HOOK_ID, ADD_HOOK_ID,
                        mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), setOf(), randomBoolean(), randomInt(),
                        mockDimensions, null, PRERENDER_HOOK_ID, ADD_HOOK_ID, mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), setOf(), randomBoolean(), randomInt(),
                        null, mockRenderingBoundaries, PRERENDER_HOOK_ID, ADD_HOOK_ID,
                        mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), setOf(), randomBoolean(), randomInt(),
                        mockDimensions, mockRenderingBoundaries, PRERENDER_HOOK_ID, ADD_HOOK_ID,
                        mockComponent, null));
    }
}
