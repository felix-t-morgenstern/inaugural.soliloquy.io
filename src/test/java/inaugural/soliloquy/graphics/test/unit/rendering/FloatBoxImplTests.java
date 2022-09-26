package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.FloatBox;

import static org.junit.jupiter.api.Assertions.*;

class FloatBoxImplTests {
    private final float LEFT_X = 0.11f;
    private final float TOP_Y = 0.22f;
    private final float RIGHT_X = 0.33f;
    private final float BOTTOM_Y = 0.44f;

    private FloatBox _floatBox;

    @BeforeEach
    void setUp() {
        _floatBox = new FloatBoxImpl(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y);
    }

    @Test
    void testGetters() {
        assertEquals(LEFT_X, _floatBox.leftX());
        assertEquals(TOP_Y, _floatBox.topY());
        assertEquals(RIGHT_X, _floatBox.rightX());
        assertEquals(BOTTOM_Y, _floatBox.bottomY());
    }

    @Test
    void testWidthAndHeight() {
        assertEquals(RIGHT_X - LEFT_X, _floatBox.width());
        assertEquals(BOTTOM_Y - TOP_Y, _floatBox.height());
    }

    @Test
    void testIntersection() {
        float newLeftX = LEFT_X + 0.01f;
        float newTopY = TOP_Y + 0.01f;
        float newRightX = RIGHT_X - 0.01f;
        float newBottomY = BOTTOM_Y - 0.01f;

        FloatBox intersectand1 = new FloatBoxImpl(newLeftX, TOP_Y, RIGHT_X, BOTTOM_Y);

        FloatBox intersection1 = _floatBox.intersection(intersectand1);

        assertEquals(newLeftX, intersection1.leftX());
        assertEquals(TOP_Y, intersection1.topY());
        assertEquals(RIGHT_X, intersection1.rightX());
        assertEquals(BOTTOM_Y, intersection1.bottomY());

        FloatBox intersectand2 = new FloatBoxImpl(LEFT_X, newTopY, RIGHT_X, BOTTOM_Y);

        FloatBox intersection2 = intersection1.intersection(intersectand2);

        assertEquals(newLeftX, intersection2.leftX());
        assertEquals(newTopY, intersection2.topY());
        assertEquals(RIGHT_X, intersection2.rightX());
        assertEquals(BOTTOM_Y, intersection2.bottomY());

        FloatBox intersectand3 = new FloatBoxImpl(LEFT_X, TOP_Y, newRightX, BOTTOM_Y);

        FloatBox intersection3 = intersection2.intersection(intersectand3);

        assertEquals(newLeftX, intersection3.leftX());
        assertEquals(newTopY, intersection3.topY());
        assertEquals(newRightX, intersection3.rightX());
        assertEquals(BOTTOM_Y, intersection3.bottomY());

        FloatBox intersectand4 = new FloatBoxImpl(LEFT_X, TOP_Y, RIGHT_X, newBottomY);

        FloatBox intersection4 = intersection3.intersection(intersectand4);

        assertEquals(newLeftX, intersection4.leftX());
        assertEquals(newTopY, intersection4.topY());
        assertEquals(newRightX, intersection4.rightX());
        assertEquals(newBottomY, intersection4.bottomY());
    }

    @Test
    void testIntersectionWithNoOverlap() {
        float topLeft = 0.25f;
        float bottomRight = 0.75f;
        FloatBox floatBox = new FloatBoxImpl(topLeft, topLeft, bottomRight, bottomRight);

        assertNotNull(floatBox.intersection(floatBox));

        FloatBox floatBoxToTheLeft = new FloatBoxImpl(0f, topLeft, topLeft, bottomRight);
        assertNull(floatBox.intersection(floatBoxToTheLeft));

        FloatBox floatBoxToTheTop = new FloatBoxImpl(topLeft, 0f, bottomRight, topLeft);
        assertNull(floatBox.intersection(floatBoxToTheTop));

        FloatBox floatBoxToTheRight = new FloatBoxImpl(bottomRight, topLeft, 1f, bottomRight);
        assertNull(floatBox.intersection(floatBoxToTheRight));

        FloatBox floatBoxToTheBottom = new FloatBoxImpl(topLeft, bottomRight, bottomRight, 1f);
        assertNull(floatBox.intersection(floatBoxToTheBottom));
    }

    @Test
    void testIntersectionWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _floatBox.intersection(null));
    }

    @Test
    void testTranslate() {
        float xTranslation = 0.123f;
        float yTranslation = 0.456f;

        FloatBox translation = _floatBox.translate(xTranslation, yTranslation);

        assertNotNull(translation);
        assertNotSame(_floatBox, translation);
        assertEquals(LEFT_X + xTranslation, translation.leftX());
        assertEquals(TOP_Y + yTranslation, translation.topY());
        assertEquals(RIGHT_X + xTranslation, translation.rightX());
        assertEquals(BOTTOM_Y + yTranslation, translation.bottomY());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FloatBox.class.getCanonicalName(), _floatBox.getInterfaceName());
    }
}
