package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.floatbox;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.valueobjects.Pair.pairOf;

class FiniteLinearMovingFloatBoxProviderTest extends FiniteLinearMovingProviderTest {
    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();

        var startingLocation = new FloatBoxImpl(0f, 0f, 0.25f, 0.375f);
        var endingLocation = new FloatBoxImpl(0.25f, 0.125f, 0.75f, 0.875f);

        Map<Long, FloatBox> renderingDimensionsAtTimes = mapOf(
                pairOf(timestamp, startingLocation),
                pairOf(timestamp + 3000, endingLocation)
        );

        var finiteLinearMovingFloatBoxProvider =
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY,
                        java.util.UUID.randomUUID(), renderingDimensionsAtTimes, null, null);
        SpriteRenderable.setRenderingDimensionsProvider(finiteLinearMovingFloatBoxProvider);

        FirstChildStack.add(SpriteRenderable);
        Renderers.registerRenderer(SpriteRenderable.class.getCanonicalName(), SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
