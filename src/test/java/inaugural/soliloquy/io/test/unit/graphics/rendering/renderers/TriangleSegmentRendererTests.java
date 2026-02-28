package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;

import static inaugural.soliloquy.tools.random.Random.randomValidFloatBox;

@ExtendWith(MockitoExtension.class)
public class TriangleSegmentRendererTests {
    private final FloatBox RENDERING_BOUNDARIES = randomValidFloatBox();
}
