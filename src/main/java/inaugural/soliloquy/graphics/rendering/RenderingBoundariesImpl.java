package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.util.Stack;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;

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
        Check.ifNull(floatBox, "floatBox");

        if (CURRENT_BOUNDARIES_FROM_STACK.empty()) {
            CURRENT_BOUNDARIES_FROM_STACK.push(floatBox);
        }
        else {
            CURRENT_BOUNDARIES_FROM_STACK.push(
                    CURRENT_BOUNDARIES_FROM_STACK.peek().intersection(floatBox));
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

    @Override
    public String getInterfaceName() {
        return RenderingBoundaries.class.getCanonicalName();
    }
}
