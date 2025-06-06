package inaugural.soliloquy.graphics.renderables.providers;

import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;

import java.awt.*;

import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

class Interpolate {
    protected static float floats(float value1, float weight1, float value2, float weight2) {
        return (value1 * weight1) + (value2 * weight2);
    }

    protected static FloatBox floatBoxes(FloatBox value1, float weight1,
                                         FloatBox value2, float weight2) {
        float value1WeightedLeftX = value1.LEFT_X * weight1;
        float value1WeightedTopY = value1.TOP_Y * weight1;
        float value1WeightedRightX = value1.RIGHT_X * weight1;
        float value1WeightedBottomY = value1.BOTTOM_Y * weight1;

        float value2WeightedLeftX = value2.LEFT_X * weight2;
        float value2WeightedTopY = value2.TOP_Y * weight2;
        float value2WeightedRightX = value2.RIGHT_X * weight2;
        float value2WeightedBottomY = value2.BOTTOM_Y * weight2;

        return floatBoxOf(
                value1WeightedLeftX + value2WeightedLeftX,
                value1WeightedTopY + value2WeightedTopY,
                value1WeightedRightX + value2WeightedRightX,
                value1WeightedBottomY + value2WeightedBottomY
        );
    }

    protected static Vertex vertices(Vertex value1, float weight1, Vertex value2, float weight2) {
        return vertexOf(
                Interpolate.floats(value1.X, weight1, value2.X, weight2),
                Interpolate.floats(value1.Y, weight1, value2.Y, weight2)
        );
    }

    protected static Color colors(Color value1, float weight1, Color value2, float weight2,
                                  boolean isClockwise) {
        float[] value1Hsb =
                Color.RGBtoHSB(value1.getRed(), value1.getGreen(), value1.getBlue(), null);
        float[] value2Hsb =
                Color.RGBtoHSB(value2.getRed(), value2.getGreen(), value2.getBlue(), null);

        float hue;
        float startHue = value1Hsb[0];
        float endHue = value2Hsb[0];
        float distance;
        if (isClockwise) {
            distance = endHue - startHue;
            while (distance < 0f) {
                distance += 1f;
            }
            hue = startHue + (distance * weight2);
        }
        else {
            distance = startHue - endHue;
            if (distance < 0) {
                distance += 1;
            }
            hue = startHue - (distance * weight2);
        }
        if (hue < 0f) {
            hue += 1f;
        }
        else if (hue > 1f) {
            hue -= 1f;
        }

        float saturation = (weight1 * value1Hsb[1]) + (weight2 * value2Hsb[1]);
        float brightness = (weight1 * value1Hsb[2]) + (weight2 * value2Hsb[2]);

        Color rgb = Color.getHSBColor(hue, saturation, brightness);

        int alpha = (int) ((weight1 * value1.getAlpha()) + (weight2 * value2.getAlpha()));

        return new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);
    }
}
