package inaugural.soliloquy.io.test.integration.display.fullsuite.rectangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.awt.*;

import static inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest.WHOLE_SCREEN_PROVIDER;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class RectangleRenderingBoundaryClippingDisplayTest extends DisplayTest {
    private final static String TILE_LOCATION_RELATIVE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Rectangle clipping display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{
                                new ImageDefinitionDTO(TILE_LOCATION_RELATIVE_LOCATION, false)
                        },
                        new FontDefinitionDTO[]{},
                        new SpriteDefinitionDTO[]{},
                        new AnimationDefinitionDTO[]{},
                        new GlobalLoopingAnimationDefinitionDTO[]{},
                        new ImageAssetSetDefinitionDTO[]{},
                        new MouseCursorImageDefinitionDTO[]{},
                        new AnimatedMouseCursorDefinitionDTO[]{},
                        new StaticMouseCursorDefinitionDTO[]{}
                ),
                () -> DisplayTest.runThenClose("Rectangle clipping", 8000),
                RectangleRenderingBoundaryClippingDisplayTest::populateTopLevelComponent
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
                staticProvider(null),
                staticProvider(1f),
                staticProvider(1f),
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
        var squareLength = 0.2f;
        var dimensProvider = finiteLinearMovingProviderFactory.make(
                randomUUID(),
                mapOf(
                        pairOf(
                                now,
                                floatBoxOf(
                                        vertexOf(0.1f,0.1f),
                                        squareLength, squareLength
                                )
                        ),
                        pairOf(
                                now + 4000,
                                floatBoxOf(
                                        vertexOf(0.9f - squareLength,0.5f - (squareLength/2f)),
                                        squareLength, squareLength
                                )
                        ),
                        pairOf(
                                now + 8000,
                                floatBoxOf(
                                        vertexOf(0.1f,0.9f - squareLength),
                                        squareLength, squareLength
                                )
                        )
                ),
                null
        );
        componentWithRenderingBoundaries.add(rectangleRenderableFactory.make(
                staticProvider(Color.RED),
                staticProvider(Color.GREEN),
                staticProvider(Color.ORANGE),
                staticProvider(Color.BLUE),
                staticProvider(null),
                staticProvider(1f),
                staticProvider(1f),
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
