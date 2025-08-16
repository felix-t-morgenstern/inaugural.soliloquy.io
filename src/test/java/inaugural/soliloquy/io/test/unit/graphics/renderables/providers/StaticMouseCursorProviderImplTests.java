package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StaticMouseCursorProviderImplTests {
    private final String ID = randomString();
    private final Long PROVIDED_VALUE = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;

    private StaticMouseCursorProvider provider;

    @BeforeEach
    public void setUp() {
        provider =
                new StaticMouseCursorProviderImpl(ID, PROVIDED_VALUE, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new StaticMouseCursorProviderImpl(null, PROVIDED_VALUE,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticMouseCursorProviderImpl("", PROVIDED_VALUE,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new StaticMouseCursorProviderImpl(ID, PROVIDED_VALUE, null));
    }

    @Test
    public void testUuid() {
        assertThrows(UnsupportedOperationException.class, provider::uuid);
    }

    @Test
    public void testId() {
        assertEquals(ID, provider.id());
    }

    @Test
    public void testProvide() {
        var timestamp = randomLong();

        assertEquals(PROVIDED_VALUE, provider.provide(timestamp));
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(timestamp);
    }

    @Test
    public void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class,
                provider::pausedTimestamp);
    }

    @Test
    public void testRepresentation() {
        assertEquals(PROVIDED_VALUE, provider.representation());
    }
}
