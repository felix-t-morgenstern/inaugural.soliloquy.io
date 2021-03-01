package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.graphics.rendering.factories.FloatBoxFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FloatBoxFactoryImplTests {
    private final float LEFT_X = -0.11f;
    private final float TOP_Y = 0.22f;
    private final float RIGHT_X = 0.33f;
    private final float BOTTOM_Y = -0.44f;

    private FloatBoxFactory _floatBoxFactory;

    @BeforeEach
    void setUp() {
        _floatBoxFactory = new FloatBoxFactoryImpl();
    }

    @Test
    void testCreate() {
        FloatBox floatBox = _floatBoxFactory.make(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y);

        assertEquals(LEFT_X, floatBox.leftX());
        assertEquals(TOP_Y, floatBox.topY());
        assertEquals(RIGHT_X, floatBox.rightX());
        assertEquals(BOTTOM_Y, floatBox.bottomY());
        assertEquals(FloatBox.class.getCanonicalName(), floatBox.getInterfaceName());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FloatBoxFactory.class.getCanonicalName(),
                _floatBoxFactory.getInterfaceName());
    }
}
