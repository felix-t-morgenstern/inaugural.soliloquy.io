package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.sinusoid.floatf;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteSinusoidMovingFloatProvider;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FiniteSinusoidMovingFloatProviderTest extends FiniteLinearMovingProviderTest {
    protected static FiniteSinusoidMovingFloatProvider FiniteSinusoidMovingFloatProvider;

    protected static final Color BORDER_COLOR = Color.getHSBColor(0.75f, 1f, 1f);
    protected static final Float BORDER_THICKNESS = 0.025f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        HashMap<Long, Float> borderThicknessValues = new HashMap<>();
        borderThicknessValues.put(timestamp, 0f);
        borderThicknessValues.put(timestamp + 4000, BORDER_THICKNESS);
        borderThicknessValues.put(timestamp + 8000, 0f);
        SpriteRenderable.BorderThicknessProvider =
                FiniteSinusoidMovingFloatProvider = new FiniteSinusoidMovingFloatProvider(
                        java.util.UUID.randomUUID(),
                        borderThicknessValues,
                        new ArrayList<Float>() {{
                            add(1f);
                            add(0.5f);
                        }},
                        null,
                        null
                );
        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
