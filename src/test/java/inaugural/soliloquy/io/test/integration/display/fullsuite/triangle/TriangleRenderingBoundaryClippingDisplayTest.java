package inaugural.soliloquy.io.test.integration.display.fullsuite.triangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.awt.*;

import static inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest.WHOLE_SCREEN_PROVIDER;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TriangleRenderingBoundaryClippingDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Triangle clipping display test",
                new AssetDefinitionsDTO(
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf()
                ),
                () -> DisplayTest.runThenClose("Triangle clipping", 8000),
                TriangleRenderingBoundaryClippingDisplayTest::populateTopLevelComponent
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
                staticProvider(0f),
                staticProvider(1f),
                staticProvider(0f),
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

        var verticesProviders = listOf(
                vertexOf(0f,0f),
                vertexOf(0.2f, 0.1f),
                vertexOf(0.08f, 0.2f)
        ).stream().map(v -> finiteLinearMovingProviderFactory.make(
                randomUUID(),
                mapOf(
                        pairOf(
                                now,
                                vertexOf(v.X + 0.1f,v.Y + 0.1f)
                        ),
                        pairOf(
                                now + 4000,
                                vertexOf(v.X + 0.8f,v.Y + 0.4f)
                        ),
                        pairOf(
                                now + 8000,
                                vertexOf(v.X + 0.1f,v.Y + 0.9f)
                        )
                ),
                null
        )).toList();

        var triangleRenderableFactory = ioModule.provide(TriangleRenderableFactory.class);
        componentWithRenderingBoundaries.add(triangleRenderableFactory.make(
                verticesProviders.get(0),
                staticProvider(Color.RED),
                verticesProviders.get(1),
                staticProvider(Color.GREEN),
                verticesProviders.get(2),
                staticProvider(Color.BLUE),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(),
                mapOf(),
                null,
                null,
                0,
                randomUUID(),
                componentWithRenderingBoundaries
        ));
    }
}
