package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.factories.ComponentFactoryImpl;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComponentFactoryImplTests {
    private final String DATA_KEY = randomString();
    private final int DATA_VAL = randomInt();
    private final Map<String, Object> DATA = mapOf(DATA_KEY, DATA_VAL);

    @Mock private ProviderAtTime<FloatBox> mockRenderingBoundaries;
    @Mock private BiConsumer<Component, Integer> mockAddToKeyCapturing;
    @Mock private Consumer<Component> mockRemoveFromKeyCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockAddToMouseCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockRemoveFromMouseCapturing;
    @Mock private Component mockComponent;

    private ComponentFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new ComponentFactoryImpl(mockAddToKeyCapturing, mockRemoveFromKeyCapturing,
                mockAddToMouseCapturing, mockRemoveFromMouseCapturing);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(null, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockAddToKeyCapturing, null, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockAddToKeyCapturing, mockRemoveFromKeyCapturing,
                        null, mockRemoveFromMouseCapturing));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockAddToKeyCapturing, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, null));
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
                mockRenderingBoundaries, mockComponent, DATA);
        when(mockRenderableWithMouseEvents.containingComponent()).thenReturn(output);
        output.add(mockRenderableWithMouseEvents);
        when(mockRenderableWithMouseEvents.containingComponent()).thenReturn(output);
        output.remove(mockRenderableWithMouseEvents);

        assertNotNull(output);
        assertSame(bindings, output.keyBindings());
        assertEquals(overrides, output.blocksLowerKeyBindings());
        assertEquals(uuid, output.uuid());
        assertEquals(z, output.getZ());
        assertEquals(tier + 1, output.tier());
        assertSame(mockComponent, output.containingComponent());
        assertSame(mockRenderingBoundaries, output.getRenderingBoundariesProvider());
        assertEquals(DATA, output.data());
        assertNotSame(DATA, output.data());
        verify(mockAddToMouseCapturing, once()).accept(mockRenderableWithMouseEvents);
        verify(mockRemoveFromMouseCapturing, once()).accept(mockRenderableWithMouseEvents);
        verify(mockAddToKeyCapturing, once()).accept(output, keyEventPriority);

        output.delete();

        verify(mockRemoveFromKeyCapturing, once()).accept(output);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, randomInt(), setOf(), randomBoolean(), randomInt(),
                        mockRenderingBoundaries, mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), null, randomBoolean(), randomInt(),
                        mockRenderingBoundaries, mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), setOf(), randomBoolean(), randomInt(),
                        null, mockComponent, DATA));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), setOf(), randomBoolean(), randomInt(),
                        mockRenderingBoundaries, mockComponent, null));
    }
}
