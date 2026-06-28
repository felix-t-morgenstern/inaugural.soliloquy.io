package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.mouse.MouseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;
import soliloquy.specs.io.input.mouse.Mouse;

import java.util.function.Function;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.random.Random.randomVertex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MouseImplTests {
    @Mock private ProviderAtTime<Long> mockMouseCursorProvider;
    @Mock private Function<String, ProviderAtTime<Long>> mockGetMouseCursor;
    @Mock private GlobalClock mockClock;

    private Mouse mouse;

    @BeforeEach
    public void setUp() {
        lenient().when(mockGetMouseCursor.apply(any())).thenReturn(mockMouseCursorProvider);

        mouse = new MouseImpl(mockGetMouseCursor, mockClock);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MouseImpl(null, mockClock));
        assertThrows(IllegalArgumentException.class, () -> new MouseImpl(mockGetMouseCursor, null));
    }

    @Test
    public void testSetMouseCursorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> mouse.setMouseCursor(null));
        assertThrows(IllegalArgumentException.class, () -> mouse.setMouseCursor(""));

        when(mockGetMouseCursor.apply(any())).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> mouse.setMouseCursor(randomString()));
    }

    @Test
    public void testSetMostRecentMouseLocation() {
        var loc = randomVertex();

        ((MouseImpl)mouse).setMostRecentMouseLocation(loc);

        assertEquals(loc, mouse.mostRecentMouseLocation());
    }
}
