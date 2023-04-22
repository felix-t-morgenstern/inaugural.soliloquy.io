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

    private FloatBox floatBox;

    @BeforeEach
    void setUp() {
        floatBox = new FloatBoxImpl(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y);
    }

    @Test
    void testGetters() {
        assertEquals(LEFT_X, floatBox.leftX());
        assertEquals(TOP_Y, floatBox.topY());
        assertEquals(RIGHT_X, floatBox.rightX());
        assertEquals(BOTTOM_Y, floatBox.bottomY());
    }

    @Test
    void testWidthAndHeight() {
        assertEquals(RIGHT_X - LEFT_X, floatBox.width());
        assertEquals(BOTTOM_Y - TOP_Y, floatBox.height());
    }

    @Test
    void testIntersection() {
        var newLeftX = LEFT_X + 0.01f;
        var newTopY = TOP_Y + 0.01f;
        var newRightX = RIGHT_X - 0.01f;
        var newBottomY = BOTTOM_Y - 0.01f;

        var intersectand1 = new FloatBoxImpl(newLeftX, TOP_Y, RIGHT_X, BOTTOM_Y);

        var intersection1 = floatBox.intersection(intersectand1);

        assertEquals(newLeftX, intersection1.leftX());
        assertEquals(TOP_Y, intersection1.topY());
        assertEquals(RIGHT_X, intersection1.rightX());
        assertEquals(BOTTOM_Y, intersection1.bottomY());

        var intersectand2 = new FloatBoxImpl(LEFT_X, newTopY, RIGHT_X, BOTTOM_Y);

        var intersection2 = intersection1.intersection(intersectand2);

        assertEquals(newLeftX, intersection2.leftX());
        assertEquals(newTopY, intersection2.topY());
        assertEquals(RIGHT_X, intersection2.rightX());
        assertEquals(BOTTOM_Y, intersection2.bottomY());

        var intersectand3 = new FloatBoxImpl(LEFT_X, TOP_Y, newRightX, BOTTOM_Y);

        var intersection3 = intersection2.intersection(intersectand3);

        assertEquals(newLeftX, intersection3.leftX());
        assertEquals(newTopY, intersection3.topY());
        assertEquals(newRightX, intersection3.rightX());
        assertEquals(BOTTOM_Y, intersection3.bottomY());

        var intersectand4 = new FloatBoxImpl(LEFT_X, TOP_Y, RIGHT_X, newBottomY);

        var intersection4 = intersection3.intersection(intersectand4);

        assertEquals(newLeftX, intersection4.leftX());
        assertEquals(newTopY, intersection4.topY());
        assertEquals(newRightX, intersection4.rightX());
        assertEquals(newBottomY, intersection4.bottomY());
    }

    @Test
    void testIntersectionWithNoOverlap() {
        var topLeft = 0.25f;
        var bottomRight = 0.75f;
        var floatBox = new FloatBoxImpl(topLeft, topLeft, bottomRight, bottomRight);

        assertNotNull(floatBox.intersection(floatBox));

        var floatBoxToTheLeft = new FloatBoxImpl(0f, topLeft, topLeft, bottomRight);
        assertNull(floatBox.intersection(floatBoxToTheLeft));

        var floatBoxToTheTop = new FloatBoxImpl(topLeft, 0f, bottomRight, topLeft);
        assertNull(floatBox.intersection(floatBoxToTheTop));

        var floatBoxToTheRight = new FloatBoxImpl(bottomRight, topLeft, 1f, bottomRight);
        assertNull(floatBox.intersection(floatBoxToTheRight));

        var floatBoxToTheBottom = new FloatBoxImpl(topLeft, bottomRight, bottomRight, 1f);
        assertNull(floatBox.intersection(floatBoxToTheBottom));
    }

    @Test
    void testIntersectionWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> floatBox.intersection(null));
    }

    @Test
    void testTranslate() {
        var xTranslation = 0.123f;
        var yTranslation = 0.456f;

        var translation = floatBox.translate(xTranslation, yTranslation);

        assertNotNull(translation);
        assertNotSame(floatBox, translation);
        assertEquals(LEFT_X + xTranslation, translation.leftX());
        assertEquals(TOP_Y + yTranslation, translation.topY());
        assertEquals(RIGHT_X + xTranslation, translation.rightX());
        assertEquals(BOTTOM_Y + yTranslation, translation.bottomY());
    }

    @Test
    void testEquals() {
        assertEquals(floatBox, new FloatBoxImpl(LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FloatBox.class.getCanonicalName(), floatBox.getInterfaceName());
    }
}
