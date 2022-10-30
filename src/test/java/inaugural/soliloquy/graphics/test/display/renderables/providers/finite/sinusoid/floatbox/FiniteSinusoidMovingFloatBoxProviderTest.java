package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.sinusoid.floatbox;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteSinusoidMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.when;

public class FiniteSinusoidMovingFloatBoxProviderTest extends FiniteLinearMovingProviderTest {
    protected static FiniteSinusoidMovingFloatBoxProvider FiniteSinusoidMovingFloatBoxProvider;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        long timestamp = GLOBAL_CLOCK.globalTimestamp();
        HashMap<Long, FloatBox> renderingDimensionsAtTimes = new HashMap<>();

        FloatBoxImpl startingLocation = new FloatBoxImpl(0f, 0f, 0.25f, 0.375f);
        FloatBoxImpl middleLocation = new FloatBoxImpl(0.25f, 0.125f, 0.75f, 0.875f);
        FloatBoxImpl endingLocation = new FloatBoxImpl(0.75f, 0.625f, 1f, 1f);

        renderingDimensionsAtTimes.put(timestamp, startingLocation);
        renderingDimensionsAtTimes.put(timestamp + 3000, middleLocation);
        renderingDimensionsAtTimes.put(timestamp + 6000, endingLocation);

        when(SpriteRenderable.getRenderingDimensionsProvider()).thenReturn(
                FiniteSinusoidMovingFloatBoxProvider = new FiniteSinusoidMovingFloatBoxProvider(
                        java.util.UUID.randomUUID(),
                        renderingDimensionsAtTimes,
                        new ArrayList<Float>() {{
                            add(1f);
                            add(0.75f);
                        }},
                        null,
                        null,
                        FLOAT_BOX_FACTORY
                ));

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
