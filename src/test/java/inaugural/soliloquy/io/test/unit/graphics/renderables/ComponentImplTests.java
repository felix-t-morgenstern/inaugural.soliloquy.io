package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.BiConsumer;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.entities.Function;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;
import soliloquy.specs.ui.definitions.providers.AbstractProviderDefinition;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComponentImplTests {
    private final String DATA_KEY = randomString();
    private final int DATA_VAL = randomInt();
    private final Map<String, Object> DATA = mapOf(DATA_KEY, DATA_VAL);
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int Z = randomInt();
    private final boolean OVERRIDES_LOWER_KEY_BINDINGS = randomBoolean();

    @Mock private AbstractProviderDefinition<FloatBox> mockRenderingBoundariesDefinition;
    @SuppressWarnings("rawtypes")
    @Mock private Function<AbstractProviderDefinition, ProviderAtTime> mockProviderReader;
    @Mock private ProviderAtTime<FloatBox> mockDimensions;
    @Mock private ProviderAtTime<FloatBox> mockRenderingBoundaries;
    @Mock private Consumer<Component> mockRegisterComponent;
    @Mock private Consumer<Component> mockDeregisterComponent;
    @Mock private Consumer<Component> mockRemoveFromKeyCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockAddToMouseCapturing;
    @Mock private Consumer<RenderableWithMouseEvents> mockRemoveFromMouseCapturing;
    @Mock private BiConsumer<Component, Long> mockPrerenderHook;
    @Mock private BiConsumer<Component, Component.Addend> mockAddHook;

    @Mock private Set<KeyBinding> mockBindings;
    @Mock private Renderable mockRenderable;
    @Mock private RenderableWithMouseEvents mockRenderableWithMouseEvents;
    @Mock private Component mockComponent;


    private Component component;

    @BeforeEach
    public void setUp() {
        lenient().when(mockProviderReader.apply(mockRenderingBoundariesDefinition))
                .thenReturn(mockRenderingBoundaries);

        component = new ComponentImpl(UUID, Z, mockBindings, OVERRIDES_LOWER_KEY_BINDINGS, null,
                mockDimensions, mockRenderingBoundaries, DATA,
                mockRegisterComponent, mockDeregisterComponent, mockRemoveFromKeyCapturing,
                mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                mockAddHook);

        lenient().when(mockRenderable.containingComponent()).thenReturn(component);
        lenient().when(mockRenderableWithMouseEvents.containingComponent()).thenReturn(component);
        lenient().when(mockComponent.containingComponent()).thenReturn(component);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(null, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, null, OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, null, DATA, mockRegisterComponent, mockDeregisterComponent,
                        mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, mockPrerenderHook, mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null, null,
                        mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, null, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, null,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent, null,
                        mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, mockPrerenderHook, mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, null, mockAddToMouseCapturing,
                        mockRemoveFromMouseCapturing, mockPrerenderHook, mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing, null,
                        mockRemoveFromMouseCapturing, mockPrerenderHook, mockAddHook));
        assertThrows(IllegalArgumentException.class,
                () -> new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, null, mockPrerenderHook, mockAddHook));
    }

    @Test
    public void testConstructorRegistersSelfAndAddsSelfToContainingComponent() {
        var mockComponent = mock(ComponentImpl.class);

        component = new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, mockComponent,
                mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                mockDeregisterComponent, mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                mockRemoveFromMouseCapturing, mockPrerenderHook, mockAddHook);

        verify(mockComponent, once()).add(component);
        verify(mockRegisterComponent, once()).accept(component);
    }

    @Test
    public void testKeyBindings() {
        assertSame(mockBindings, component.keyBindings());
    }

    @Test
    public void testAddAndContentsRepresentation() {
        component.add(mockRenderable);
        var content = component.contentsRepresentation();

        assertNotNull(content);
        assertEquals(setOf(mockRenderable), content);
    }

    @Test
    public void testAddCallsHook() {
        var key = randomString();
        var val = randomInt();

        component.add(mockRenderable, mapOf(key,val));

        var addendCaptor = ArgumentCaptor.forClass(Component.Addend.class);
        verify(mockAddHook, once()).accept(same(component), addendCaptor.capture());
        var addend = addendCaptor.getValue();
        assertSame(mockRenderable, addend.content());
        assertEquals(mapOf(key, val), addend.data());
    }

    @Test
    public void testAddToSameComponent() {
        when(mockRenderable.containingComponent()).thenReturn(component);

        component.add(mockRenderable);
        var content = component.contentsRepresentation();

        assertNotNull(content);
        assertEquals(setOf(mockRenderable), content);
    }

    @Test
    public void testAddNull() {
        assertThrows(IllegalArgumentException.class, () -> component.add(null));
    }

    @Test
    public void testAddRenderableInDifferentComponent() {
        when(mockRenderable.containingComponent()).thenReturn(mock(ComponentImpl.class));
        assertThrows(IllegalArgumentException.class, () -> component.add(mockRenderable));
    }

    @Test
    public void testAddComponentOfInvalidTier() {
        when(mockComponent.tier()).thenReturn(component.tier());
        assertThrows(IllegalArgumentException.class, () -> component.add(mockComponent));
        when(mockComponent.tier()).thenReturn(component.tier() + 2);
        assertThrows(IllegalArgumentException.class, () -> component.add(mockComponent));
    }

    @Test
    public void testAddRenderableNotInComponent() {
        when(mockRenderable.containingComponent()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> component.add(mockRenderable));
    }

    @Test
    public void testAddPlacesInMouseCapturing() {
        component.add(mockRenderable);
        component.add(mockRenderableWithMouseEvents);

        verify(mockAddToMouseCapturing, once()).accept(any());
        verify(mockAddToMouseCapturing, once()).accept(mockRenderableWithMouseEvents);
    }

    @Test
    public void testRemove() {
        when(mockRenderable.containingComponent()).thenReturn(component);
        component.add(mockRenderable);

        component.remove(mockRenderable);

        assertTrue(component.contentsRepresentation().isEmpty());
    }

    @Test
    public void testClear() {
        var mockRenderable2 = mock(Renderable.class);
        when(mockRenderable2.containingComponent()).thenReturn(component);
        var mockRenderable3 = mock(Renderable.class);
        when(mockRenderable3.containingComponent()).thenReturn(component);
        component.add(mockRenderable);
        component.add(mockRenderable2);
        component.add(mockRenderable3);

        component.clear();

        assertTrue(component.contentsRepresentation().isEmpty());
    }

    @Test
    public void testRemoveRemovesFromMouseCapturing() {
        when(mockRenderable.containingComponent()).thenReturn(component);
        when(mockRenderableWithMouseEvents.containingComponent()).thenReturn(component);
        component.add(mockRenderable);
        component.add(mockRenderableWithMouseEvents);

        component.remove(mockRenderable);
        component.remove(mockRenderableWithMouseEvents);

        verify(mockRemoveFromMouseCapturing, once()).accept(any());
        verify(mockRemoveFromMouseCapturing, once()).accept(mockRenderableWithMouseEvents);
    }

    @Test
    public void testRemoveWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> component.remove(null));
        when(mockRenderable.containingComponent()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> component.remove(mockRenderable));
        when(mockRenderable.containingComponent()).thenReturn(mock(ComponentImpl.class));
        assertThrows(IllegalArgumentException.class, () -> component.remove(mockRenderable));
        when(mockRenderable.isDeleted()).thenReturn(true);
        assertDoesNotThrow(() -> component.remove(mockRenderable));
    }

    @Test
    public void testGetDimensionsProvider() {
        assertSame(mockDimensions, component.getDimensionsProvider());
    }

    @Test
    public void testSetDimensionsProvider() {
        @SuppressWarnings("unchecked") var newDimensions =
                (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);

        component.setDimensionsProvider(newDimensions);

        assertSame(newDimensions, component.getDimensionsProvider());
    }

    @Test
    public void testSetDimensionsProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> component.setDimensionsProvider(null));
    }

    @Test
    public void testTierDefaultValue() {
        assertEquals(0, component.tier());
    }

    @Test
    public void testTierIncrementing() {
        var firstChild = new ComponentImpl(UUID, randomInt(), setOf(), OVERRIDES_LOWER_KEY_BINDINGS,
                component, mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                mockDeregisterComponent, mockRemoveFromKeyCapturing, mockAddToMouseCapturing,
                mockRemoveFromMouseCapturing, mockPrerenderHook, mockAddHook);
        var secondChild =
                new ComponentImpl(UUID, randomInt(), setOf(), OVERRIDES_LOWER_KEY_BINDINGS,
                        firstChild, mockDimensions, mockRenderingBoundaries, DATA,
                        mockRegisterComponent, mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook);

        assertEquals(1, firstChild.tier());
        assertEquals(2, secondChild.tier());
    }
    
    @Test
    public void testSetContainingComponent() {
        var mockContainingComponent = mock(ComponentImpl.class);
        var containingComponentTier = randomInt();
        when(mockContainingComponent.tier()).thenReturn(containingComponentTier);

        ((ComponentImpl) component).setContainingComponent(mockContainingComponent);

        assertSame(mockContainingComponent, component.containingComponent());
        assertEquals(containingComponentTier + 1, component.tier());

        ((ComponentImpl) component).setContainingComponent(null);

        assertNull(component.containingComponent());
        assertEquals(0, component.tier());
    }

    @Test
    public void testData() {
        assertEquals(DATA, component.data());
        assertNotSame(DATA, component.data());
    }

    @Test
    public void testDelete() {
        var containedComponent =
                new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, mockComponent,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        mockAddHook);

        containedComponent.delete();

        assertTrue(containedComponent.isDeleted());
        verify(mockComponent, once()).remove(containedComponent);
        verify(mockDeregisterComponent, once()).accept(containedComponent);
        verify(mockRemoveFromKeyCapturing, once()).accept(containedComponent);
    }

    @Test
    public void testPrerenderHook() {
        var timestamp = randomLong();

        ((ComponentImpl) component).prerenderHook(timestamp);

        verify(mockPrerenderHook, once()).accept(component, timestamp);
    }

    @Test
    public void testPrerenderHookWhenNoHookPresent() {
        var timestamp = randomLong();
        var componentWithNoPrerenderHook =
                new ComponentImpl(UUID, Z, mockBindings, OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, null, mockAddHook);

        componentWithNoPrerenderHook.prerenderHook(timestamp);

        verify(mockPrerenderHook, never()).accept(any(), any());
    }

    @Test
    public void testAddHookId() {
        var addHookId = randomString();
        when(mockAddHook.id()).thenReturn(addHookId);

        assertEquals(addHookId, component.addHookId());
    }

    @Test
    public void testAddHookIdWhenNoHookPresent() {
        var componentWithNoHook =
                new ComponentImpl(UUID, Z, setOf(), OVERRIDES_LOWER_KEY_BINDINGS, null,
                        mockDimensions, mockRenderingBoundaries, DATA, mockRegisterComponent,
                        mockDeregisterComponent, mockRemoveFromKeyCapturing,
                        mockAddToMouseCapturing, mockRemoveFromMouseCapturing, mockPrerenderHook,
                        null);

        assertNull(componentWithNoHook.addHookId());
    }
}
