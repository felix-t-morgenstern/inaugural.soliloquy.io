package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.factories.ComponentFactoryImpl;
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

import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComponentFactoryImplTests {
    @Mock private ProviderAtTime<FloatBox> mockRenderingBoundaries;
    @Mock private Consumer<RenderableWithMouseEvents> mockAddToCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockRemoveFromCapturing;
    @Mock private Component mockComponent;

    private ComponentFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new ComponentFactoryImpl(mockAddToCapturing, mockRemoveFromCapturing);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(null, mockRemoveFromCapturing));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentFactoryImpl(mockAddToCapturing, null));
    }

    @Test
    public void testMake() {
        var uuid = randomUUID();
        var z = randomInt();
        var tier = randomInt();
        when(mockComponent.tier()).thenReturn(tier);
        var mockRenderableWithMouseEvents = mock(RenderableWithMouseEvents.class);

        var output = factory.make(uuid, z, mockRenderingBoundaries, mockComponent);
        output.add(mockRenderableWithMouseEvents);
        when(mockRenderableWithMouseEvents.component()).thenReturn(output);
        output.remove(mockRenderableWithMouseEvents);

        assertNotNull(output);
        assertNotNull(output.keyBindingContext());
        assertEquals(listOf(), output.keyBindingContext().BINDINGS);
        assertFalse(output.keyBindingContext().BLOCKS_LOWER_BINDINGS);
        assertEquals(uuid, output.uuid());
        assertEquals(z, output.getZ());
        assertEquals(tier + 1, output.tier());
        assertSame(mockComponent, output.component());
        assertSame(mockRenderingBoundaries, output.getRenderingBoundariesProvider());
        verify(mockAddToCapturing, once()).accept(mockRenderableWithMouseEvents);
        verify(mockRemoveFromCapturing, once()).accept(mockRenderableWithMouseEvents);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, randomInt(), mockRenderingBoundaries, mockComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(randomUUID(), randomInt(), null, mockComponent));
    }
}
