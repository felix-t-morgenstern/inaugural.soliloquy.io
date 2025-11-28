package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ProviderHandlerTests {
    @SuppressWarnings("rawtypes") @Mock private ProviderAtTime mockProvider;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<ProviderAtTime> mockSubhandler;

    private String writtenValue = null;
    private String writtenValueWithExtraneousFields = null;
    private String mockProviderClassName;

    private ProviderHandler providerHandler;

    @BeforeEach
    public void setUp() {
        mockProviderClassName = mockProvider.getClass().getCanonicalName();

        writtenValue = String.format("{\"type\":\"%s\"}", mockProviderClassName);
        writtenValueWithExtraneousFields = String.format("{\"type\":\"%s\",\"%s\":\"%s\"}",
                mockProviderClassName, randomString(), randomString());

        lenient().when(mockSubhandler.write(any())).thenReturn(writtenValue);
        lenient().when(mockSubhandler.read(anyString())).thenReturn(mockProvider);

        providerHandler = new ProviderHandler();
    }

    @Test
    public void testAddAndWrite() {
        providerHandler.add(mockProviderClassName, mockSubhandler);
        var output = providerHandler.write(mockProvider);

        assertEquals(writtenValue, output);
    }

    @Test
    public void testAddAndRead() {
        providerHandler.add(mockProviderClassName, mockSubhandler);
        var readProvider = providerHandler.read(writtenValueWithExtraneousFields);

        assertSame(mockProvider, readProvider);
    }
}
