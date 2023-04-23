package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.sinusoid.floatbox;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteSinusoidMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FiniteSinusoidMovingFloatBoxProviderTest extends FiniteLinearMovingProviderTest {
    protected static FiniteSinusoidMovingFloatBoxProvider FiniteSinusoidMovingFloatBoxProvider;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();

        var startingLocation = new FloatBoxImpl(0f, 0f, 0.25f, 0.375f);
        var middleLocation = new FloatBoxImpl(0.25f, 0.125f, 0.75f, 0.875f);
        var endingLocation = new FloatBoxImpl(0.75f, 0.625f, 1f, 1f);

        Map<Long, FloatBox> renderingDimensionsAtTimes = mapOf(
                Pair.of(timestamp, startingLocation),
                Pair.of(timestamp + 3000, middleLocation),
                Pair.of(timestamp + 6000, endingLocation)
        );

        SpriteRenderable.setRenderingDimensionsProvider(
                FiniteSinusoidMovingFloatBoxProvider = new FiniteSinusoidMovingFloatBoxProvider(
                        java.util.UUID.randomUUID(),
                        renderingDimensionsAtTimes,
                        listOf(1f, 0.75f),
                        null,
                        null,
                        FLOAT_BOX_FACTORY
                ));

        TopLevelStack.add(SpriteRenderable);
        Renderers.registerRenderer(SpriteRenderable.class.getCanonicalName(), SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
