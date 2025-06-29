package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.util.Stack;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.valueobjects.FloatBox.intersection;

public class RenderingBoundariesImpl implements RenderingBoundaries {
    private final Stack<FloatBox> CURRENT_BOUNDARIES_FROM_STACK;

    public RenderingBoundariesImpl() {
        CURRENT_BOUNDARIES_FROM_STACK = new Stack<>();
    }

    @Override
    public FloatBox currentBoundaries() {
        if (CURRENT_BOUNDARIES_FROM_STACK.empty()) {
            return WHOLE_SCREEN;
        }
        else {
            return CURRENT_BOUNDARIES_FROM_STACK.peek();
        }
    }

    @Override
    public void pushNewBoundaries(FloatBox floatBox) {
        Check.ifValid(floatBox, "floatBox");

        if (CURRENT_BOUNDARIES_FROM_STACK.empty()) {
            CURRENT_BOUNDARIES_FROM_STACK.push(floatBox);
        }
        else {
            CURRENT_BOUNDARIES_FROM_STACK.push(
                    intersection(CURRENT_BOUNDARIES_FROM_STACK.peek(), floatBox));
        }
    }

    @Override
    public void popMostRecentBoundaries() {
        CURRENT_BOUNDARIES_FROM_STACK.pop();
    }

    @Override
    public void clearAllBoundaries() {
        CURRENT_BOUNDARIES_FROM_STACK.clear();
    }
}
