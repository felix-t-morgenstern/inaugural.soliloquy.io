package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;


import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

/** @noinspection FieldCanBeLocal */
public class AnimatedMouseCursorProviderFactoryImplTests {
    private final int MS_1 = 0;
    private final long MOUSE_CURSOR_1 = 123L;
    private final int MS_2 = 111;
    private final long MOUSE_CURSOR_2 = 456L;
    private final int MS_3 = 444;
    private final long MOUSE_CURSOR_3 = 789L;

    private final Map<Integer, Long> CURSORS_AT_MS = mapOf();

    private final String ID = "id";
    private final int MS_DURATION = 777;
    private final int PERIOD_MODULO_OFFSET = 321;
    private final long MOST_RECENT_TIMESTAMP = 12L;

    private AnimatedMouseCursorProviderFactory animatedMouseCursorProviderFactory;

    @BeforeEach
    public void setUp() {
        CURSORS_AT_MS.put(MS_1, MOUSE_CURSOR_1);
        CURSORS_AT_MS.put(MS_2, MOUSE_CURSOR_2);
        CURSORS_AT_MS.put(MS_3, MOUSE_CURSOR_3);

        animatedMouseCursorProviderFactory = new AnimatedMouseCursorProviderFactoryImpl();
    }

    @Test
    public void testMake() {
        ProviderAtTime<Long> animatedMouseCursorProvider = animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));

        assertNotNull(animatedMouseCursorProvider);
        assertInstanceOf(AnimatedMouseCursorProviderImpl.class, animatedMouseCursorProvider);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(null, CURSORS_AT_MS,
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition("", CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, null, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, mapOf(),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory.make(
                new AnimatedMouseCursorProviderDefinition(ID, mapOf(pairOf(null, MOUSE_CURSOR_1)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory.make(
                new AnimatedMouseCursorProviderDefinition(ID, mapOf(pairOf(-1, MOUSE_CURSOR_1)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory.make(
                new AnimatedMouseCursorProviderDefinition(ID, mapOf(pairOf(MS_1, null)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory.make(
                new AnimatedMouseCursorProviderDefinition(ID, mapOf(pairOf(1, MOUSE_CURSOR_1)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, 0,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_3,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        MS_DURATION, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        -1, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP, null)));
    }

}
