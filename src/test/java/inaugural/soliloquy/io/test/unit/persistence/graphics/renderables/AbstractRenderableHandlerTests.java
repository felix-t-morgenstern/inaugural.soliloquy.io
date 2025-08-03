package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

abstract class AbstractRenderableHandlerTests {
    protected final int Z = randomInt();
    protected final UUID UUID = randomUUID();

    @SuppressWarnings("rawtypes") @org.mockito.Mock protected TypeHandler<ProviderAtTime>
            mockProviderHandler;

    protected void setUpMockRenderable(Renderable mockRenderable) {
        when(mockRenderable.getZ()).thenReturn(Z);
        when(mockRenderable.uuid()).thenReturn(UUID);
    }

    protected void verifyMockRenderableWritten(Renderable mockRenderable) {
        verify(mockRenderable, once()).getZ();
        verify(mockRenderable, once()).uuid();
    }
}
