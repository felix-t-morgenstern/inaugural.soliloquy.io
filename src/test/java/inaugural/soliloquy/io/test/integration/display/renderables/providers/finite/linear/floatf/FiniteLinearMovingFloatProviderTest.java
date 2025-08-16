package inaugural.soliloquy.io.test.integration.display.renderables.providers.finite.linear.floatf;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingFloatProvider;
import inaugural.soliloquy.io.test.integration.display.renderables.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class FiniteLinearMovingFloatProviderTest extends FiniteLinearMovingProviderTest {
    protected static final Color BORDER_COLOR = Color.getHSBColor(0.75f, 1f, 1f);
    protected static final Float BORDER_THICKNESS = 0.025f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();
        var borderThicknessProvider = mapOf(
                pairOf(timestamp, 0f),
                pairOf(timestamp + 3000, BORDER_THICKNESS)
        );
        SpriteRenderable.setBorderThicknessProvider(
                new FiniteLinearMovingFloatProvider(
                        java.util.UUID.randomUUID(),
                        borderThicknessProvider,
                        null, null
                ));

        when(MockFirstChildComponent.contents()).thenReturn(setOf(SpriteRenderable));
        Renderers.put(SpriteRenderableImpl.class, SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
