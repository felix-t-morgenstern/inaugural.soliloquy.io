package inaugural.soliloquy.io.test.integration.display.mockedsetup.providers.finite.sinusoid.floatf;

import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.FiniteSinusoidMovingFloatProvider;
import inaugural.soliloquy.io.test.integration.display.mockedsetup.providers.finite.linear.FiniteLinearMovingProviderTest;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteSinusoidMovingFloatProviderTest extends FiniteLinearMovingProviderTest {
    protected static FiniteSinusoidMovingFloatProvider FiniteSinusoidMovingFloatProvider;

    protected static final Color BORDER_COLOR = Color.getHSBColor(0.75f, 1f, 1f);
    protected static final Float BORDER_THICKNESS = 0.025f;

    protected static void graphicsPreloaderLoadAction() {
        Sprite.Image = new ImageFactoryImpl(0.5f)
                .make(new ImageDefinition(RPG_WEAPONS_RELATIVE_LOCATION, false));
        var timestamp = GLOBAL_CLOCK.globalTimestamp();
        var borderThicknessValues = mapOf(
                pairOf(timestamp, 0f),
                pairOf(timestamp + 4000, BORDER_THICKNESS),
                pairOf(timestamp + 8000, 0f)
        );
        SpriteRenderable.setBorderThicknessProvider(
                FiniteSinusoidMovingFloatProvider = new FiniteSinusoidMovingFloatProvider(
                        java.util.UUID.randomUUID(),
                        borderThicknessValues,
                        arrayFloats(1f, 0.5f),
                        null,
                        null
                ));

        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(setOf(SpriteRenderable));
        Renderers.put(SpriteRenderableImpl.class, SpriteRenderer);

        FrameTimer.ShouldExecuteNextFrame = true;
    }
}
