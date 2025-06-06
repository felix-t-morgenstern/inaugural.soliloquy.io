package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.graphics.rendering.RenderableStackImpl;
import inaugural.soliloquy.graphics.rendering.factories.RenderableStackFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.factories.RenderableStackFactory;

import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RenderableStackFactoryImplTests {
    private final UUID UUID = java.util.UUID.randomUUID();
    private final int Z = randomInt();
    @SuppressWarnings("unchecked")
    @Mock private ProviderAtTime<FloatBox> mockRenderingBoundariesProvider =
            mock(ProviderAtTime.class);
    @Mock private RenderableStack mockContainingStack = mock(RenderableStack.class);

    private RenderableStackFactory renderableStackFactory;

    @BeforeEach
    public void setUp() {
        renderableStackFactory = new RenderableStackFactoryImpl();
    }

    @Test
    public void testMakeTopLevelStack() {
        var topLevelStack = renderableStackFactory.makeTopLevelStack();

        assertNotNull(topLevelStack);
        assertInstanceOf(RenderableStackImpl.class, topLevelStack);
        assertNull(topLevelStack.uuid());
        assertEquals(WHOLE_SCREEN,
                topLevelStack.getRenderingBoundariesProvider().provide(randomLong()));
        assertNull(topLevelStack.containingStack());
    }

    @Test
    public void testMakeContainedStack() {
        var containedStack = renderableStackFactory.makeContainedStack(UUID, Z,
                mockRenderingBoundariesProvider, mockContainingStack);

        assertNotNull(containedStack);
        assertInstanceOf(RenderableStackImpl.class, containedStack);
        assertEquals(UUID, containedStack.uuid());
        assertEquals(Z, containedStack.getZ());
        assertSame(mockRenderingBoundariesProvider,
                containedStack.getRenderingBoundariesProvider());
        assertSame(mockContainingStack, containedStack.containingStack());
    }
}
