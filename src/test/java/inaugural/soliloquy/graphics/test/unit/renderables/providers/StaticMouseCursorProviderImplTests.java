package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.StaticMouseCursorProvider;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;

public class StaticMouseCursorProviderImplTests {
    private final String ID = randomString();
    private final Long PROVIDED_VALUE = randomLong();
    private final Long MOST_RECENT_TIMESTAMP = randomLong();

    private StaticMouseCursorProvider staticMouseCursorProvider;

    @BeforeEach
    public void setUp() {
        staticMouseCursorProvider =
                new StaticMouseCursorProviderImpl(ID, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new StaticMouseCursorProviderImpl(
                null, PROVIDED_VALUE, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new StaticMouseCursorProviderImpl(
                "", PROVIDED_VALUE, MOST_RECENT_TIMESTAMP
        ));
    }

    @Test
    public void testUuid() {
        assertThrows(UnsupportedOperationException.class, staticMouseCursorProvider::uuid);
    }

    @Test
    public void testId() {
        assertEquals(ID, staticMouseCursorProvider.id());
    }

    @Test
    public void testProvide() {
        assertEquals(PROVIDED_VALUE, staticMouseCursorProvider.provide(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testCallsMadeToPriorTimestamps() {
        long timestamp1 = randomLongWithInclusiveFloor(MOST_RECENT_TIMESTAMP + 1);
        long timestamp2 = randomLongWithInclusiveFloor(timestamp1 + 1);
        long timestamp3 = randomLongWithInclusiveFloor(timestamp2 + 1);

        staticMouseCursorProvider.provide(timestamp1);

        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.provide(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.reportPause(timestamp1 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.reportUnpause(timestamp1 - 1));

        staticMouseCursorProvider.reportPause(timestamp2);

        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.provide(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.reportPause(timestamp2 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.reportUnpause(timestamp2 - 1));

        staticMouseCursorProvider.reportUnpause(timestamp3);

        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.provide(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.reportPause(timestamp3 - 1));
        assertThrows(IllegalArgumentException.class,
                () -> staticMouseCursorProvider.reportUnpause(timestamp3 - 1));
    }

    @Test
    public void testPausedTimestamp() {
        assertThrows(UnsupportedOperationException.class,
                staticMouseCursorProvider::pausedTimestamp);
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, staticMouseCursorProvider.mostRecentTimestamp());
    }

    @Test
    public void testRepresentation() {
        assertEquals(PROVIDED_VALUE, staticMouseCursorProvider.representation());
    }
}
