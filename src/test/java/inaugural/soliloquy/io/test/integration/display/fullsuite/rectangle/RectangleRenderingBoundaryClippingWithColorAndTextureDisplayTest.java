package inaugural.soliloquy.io.test.integration.display.fullsuite.rectangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.awt.*;

import static inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest.WHOLE_SCREEN_PROVIDER;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class RectangleRenderingBoundaryClippingWithColorAndTextureDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Rectangle clipping with color and texture display test",
                new AssetDefinitionsDTO(
                        arrayOf(
                                new ImageDefinitionDTO(TILE_LOCATION_RELATIVE_LOCATION, false)
                        ),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf()
                ),
                () -> DisplayTest.runThenClose("Rectangle clipping with color and texture", 8000),
                RectangleRenderingBoundaryClippingWithColorAndTextureDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var componentFactory = ioModule.provide(ComponentFactory.class);
        var renderingBoundaries =
                staticProvider(floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f));
        var componentWithRenderingBoundaries = componentFactory.make(
                randomUUID(),
                0,
                setOf(),
                false,
                0,
                renderingBoundaries,
                renderingBoundaries,
                null,
                null,
                topLevelComponent,
                mapOf()
        );
        topLevelComponent.add(componentWithRenderingBoundaries);

        var clock = ioModule.provide(GlobalClock.class);
        var now = clock.globalTimestamp();

        var rectangleRenderableFactory = ioModule.provide(RectangleRenderableFactory.class);

        var bgGrey = new Color(64,64,64);
        componentWithRenderingBoundaries.add(rectangleRenderableFactory.make(
                staticProvider(bgGrey),
                staticProvider(bgGrey),
                staticProvider(bgGrey),
                staticProvider(bgGrey),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(),
                mapOf(),
                null,
                null,
                WHOLE_SCREEN_PROVIDER,
                -1,
                randomUUID(),
                componentWithRenderingBoundaries
        ));

        var finiteLinearMovingProviderFactory = ioModule.provide(FiniteLinearMovingProviderFactory.class);
        var squareLength = 0.3f;
        var dimensProvider = finiteLinearMovingProviderFactory.make(
                randomUUID(),
                mapOf(
                        pairOf(
                                now,
                                floatBoxOf(
                                        vertexOf(0.05f,0.05f),
                                        squareLength, squareLength
                                )
                        ),
                        pairOf(
                                now + 4000,
                                floatBoxOf(
                                        vertexOf(0.95f - squareLength,0.5f - (squareLength/2f)),
                                        squareLength, squareLength
                                )
                        ),
                        pairOf(
                                now + 8000,
                                floatBoxOf(
                                        vertexOf(0.05f,0.95f - squareLength),
                                        squareLength, squareLength
                                )
                        )
                ),
                null
        );
        var graphics = ioModule.provide(Graphics.class);
        var backgroundTex = graphics.getImage(TILE_LOCATION_RELATIVE_LOCATION).textureId();
        componentWithRenderingBoundaries.add(rectangleRenderableFactory.make(
                staticProvider(Color.RED),
                staticProvider(Color.GREEN),
                staticProvider(Color.BLUE),
                staticProvider(Color.WHITE),
                staticProvider(backgroundTex),
                staticProvider(3f),
                staticProvider(0.5f),
                staticProvider(3f),
                staticProvider(0.5f),
                mapOf(),
                mapOf(),
                null,
                null,
                dimensProvider,
                0,
                randomUUID(),
                componentWithRenderingBoundaries
        ));
    }
}
