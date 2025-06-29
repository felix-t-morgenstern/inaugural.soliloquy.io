package inaugural.soliloquy.io.test.unit.audio.entities;

import inaugural.soliloquy.io.audio.entities.SoundTypeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.audio.entities.SoundType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SoundTypeImplTests {
    private final String ID = "SoundTypeId";
    private final String FILENAME = "SoundTypeFilename";
    private final Integer DEFAULT_LOOPING_STOP_MS = 456456;
    private final Integer DEFAULT_LOOPING_RESTART_MS = 123123;

    private SoundType soundType;

    @BeforeEach
    void setUp() {
        soundType = new SoundTypeImpl(ID, FILENAME, DEFAULT_LOOPING_STOP_MS,
                DEFAULT_LOOPING_RESTART_MS);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SoundTypeImpl(null, FILENAME, DEFAULT_LOOPING_STOP_MS,
                        DEFAULT_LOOPING_RESTART_MS));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundTypeImpl(ID, null, DEFAULT_LOOPING_STOP_MS,
                        DEFAULT_LOOPING_RESTART_MS));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundTypeImpl(ID, "", DEFAULT_LOOPING_STOP_MS,
                        DEFAULT_LOOPING_RESTART_MS));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundTypeImpl(ID, FILENAME, -DEFAULT_LOOPING_STOP_MS,
                        DEFAULT_LOOPING_RESTART_MS));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundTypeImpl(ID, FILENAME, DEFAULT_LOOPING_STOP_MS,
                        -DEFAULT_LOOPING_RESTART_MS));
        assertThrows(IllegalArgumentException.class,
                () -> new SoundTypeImpl(ID, FILENAME, DEFAULT_LOOPING_RESTART_MS,
                        DEFAULT_LOOPING_STOP_MS));
    }

    @Test
    public void testId() {
        assertEquals(ID, soundType.id());
    }

    @Test
    public void testRelativePath() {
        assertEquals(FILENAME, soundType.relativePath());
    }

    @Test
    public void testDefaultLoopingStopMs() {
        assertEquals(DEFAULT_LOOPING_STOP_MS, soundType.defaultLoopingStopMs());
    }

    @Test
    public void testDefaultLoopingRestartMs() {
        assertEquals(DEFAULT_LOOPING_RESTART_MS, soundType.defaultLoopingRestartMs());
    }
}
