package inaugural.soliloquy.graphics.test;

import soliloquy.specs.graphics.rendering.FloatBox;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mocks {
    public static FloatBox mockFloatBox(float leftX, float topY, float rightX, float bottomY) {
        var mockFloatBox = mock(FloatBox.class);

        when(mockFloatBox.leftX()).thenReturn(leftX);
        when(mockFloatBox.topY()).thenReturn(topY);
        when(mockFloatBox.rightX()).thenReturn(rightX);
        when(mockFloatBox.bottomY()).thenReturn(bottomY);

        return mockFloatBox;
    }
}
