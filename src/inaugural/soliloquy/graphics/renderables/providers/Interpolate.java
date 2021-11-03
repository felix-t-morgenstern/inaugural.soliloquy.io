package inaugural.soliloquy.graphics.renderables.providers;

class Interpolate {
    protected static float floats(float value1, float weight1, float value2, float weight2) {
        return (value1 * weight1) + (value2 * weight2);
    }
}
