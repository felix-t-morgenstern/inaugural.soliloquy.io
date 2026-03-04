package inaugural.soliloquy.io.test.integration.display.fullsuite.antialiasedlinesegment;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.awt.*;

import static inaugural.soliloquy.io.test.integration.display.mockedsetup.DisplayTest.WHOLE_SCREEN_PROVIDER;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomHighSaturationColor;
import static inaugural.soliloquy.tools.valueobjects.Vertex.translateVertex;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class AntialiasedLineSegmentRenderingBoundaryClippingTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Antialiased line segment clipping display test",
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
                () -> DisplayTest.runThenClose("Antialiased line segment clipping", 8000),
                AntialiasedLineSegmentRenderingBoundaryClippingTest::populateTopLevelComponent
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
        var v1Start = vertexOf(0f, 0f);
        var v2Start = vertexOf(.2f, .1f);

        var antialiasedLineSegmentFactory =
                ioModule.provide(AntialiasedLineSegmentRenderableFactory.class);
        componentWithRenderingBoundaries.add(antialiasedLineSegmentFactory.make(
                makeVertexProvider(finiteLinearMovingProviderFactory, now, v1Start),
                makeVertexProvider(finiteLinearMovingProviderFactory, now, v2Start),
                staticProvider(randomHighSaturationColor()),
                staticProvider(.005f),
                staticProvider(0.75f),
                staticProvider(0.01f),
                0,
                randomUUID(),
                componentWithRenderingBoundaries
        ));
    }

    private static ProviderAtTime<Vertex> makeVertexProvider(FiniteLinearMovingProviderFactory factory,
                                                      long now,
                                                      Vertex start) {
        return factory.make(
                randomUUID(),
                mapOf(
                        pairOf(
                                now,
                                start
                        ),
                        pairOf(
                                now + 4000,
                                translateVertex(start, 0.8f, 0.45f)
                        ),
                        pairOf(
                                now + 8000,
                                translateVertex(start, 0f, 0.9f)
                        )
                ),
                null
        );
    }
}