package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ProviderHandlerTests {
    @SuppressWarnings("rawtypes") @Mock private ProviderAtTime mockProvider;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<ProviderAtTime> mockSubhandler;

    private String writtenValue = null;
    private String writtenValueWithExtraneousFields = null;
    @SuppressWarnings("rawtypes") private Map<String, TypeHandler<ProviderAtTime>> subhandlers;

    private ProviderHandler providerHandler;

    @BeforeEach
    public void setUp() {
        var mockProviderClassName = mockProvider.getClass().getCanonicalName();

        writtenValue = String.format("{\"type\":\"%s\"}", mockProviderClassName);
        writtenValueWithExtraneousFields = String.format("{\"type\":\"%s\",\"%s\":\"%s\"}",
                mockProviderClassName, randomString(), randomString());

        lenient().when(mockSubhandler.write(any())).thenReturn(writtenValue);
        lenient().when(mockSubhandler.read(anyString())).thenReturn(mockProvider);

        subhandlers = mapOf(pairOf(mockProviderClassName, mockSubhandler));

        providerHandler = new ProviderHandler(subhandlers);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new ProviderHandler(null));
    }

    @Test
    public void testWrite() {
        var output = providerHandler.write(mockProvider);

        assertEquals(writtenValue, output);
    }

    @Test
    public void testRead() {
        var readProvider = providerHandler.read(writtenValueWithExtraneousFields);

        assertSame(mockProvider, readProvider);
    }

    @Test
    public void testTypeHandled() {
        assertThrows(UnsupportedOperationException.class, providerHandler::typeHandled);
    }

    @Test
    public void testSubhandlersIsCloned() {
        subhandlers.clear();

        testRead();
        testWrite();
    }
}
