package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.factories.RenderableStackFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.factories.RenderableStackFactory;

import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RenderableStackFactoryImplTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int Z = randomInt();
    @SuppressWarnings("unchecked")
    @Mock private ProviderAtTime<FloatBox> _mockRenderingBoundariesProvider =
            mock(ProviderAtTime.class);
    @Mock private RenderableStack _mockContainingStack = mock(RenderableStack.class);

    private RenderableStackFactory _renderableStackFactory;

    @BeforeEach
    void setUp() {
        _renderableStackFactory = new RenderableStackFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RenderableStackFactory.class.getCanonicalName(),
                _renderableStackFactory.getInterfaceName());
    }

    @Test
    void testMakeTopLevelStack() {
        RenderableStack topLevelStack = _renderableStackFactory.makeTopLevelStack();

        assertNotNull(topLevelStack);
        assertTrue(topLevelStack instanceof RenderableStackImpl);
        assertNull(topLevelStack.uuid());
        assertEquals(WHOLE_SCREEN,
                topLevelStack.getRenderingBoundariesProvider().provide(randomLong()));
        assertNull(topLevelStack.containingStack());
    }

    @Test
    void testMakeContainedStack() {
        RenderableStack containedStack = _renderableStackFactory.makeContainedStack(UUID, Z,
                _mockRenderingBoundariesProvider, _mockContainingStack);

        assertNotNull(containedStack);
        assertTrue(containedStack instanceof RenderableStackImpl);
        assertEquals(UUID, containedStack.uuid());
        assertEquals(Z, containedStack.getZ());
        assertSame(_mockRenderingBoundariesProvider,
                containedStack.getRenderingBoundariesProvider());
        assertSame(_mockContainingStack, containedStack.containingStack());
    }
}
