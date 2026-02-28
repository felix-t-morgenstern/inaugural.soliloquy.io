package inaugural.soliloquy.io.test.integration.display.fullsuite.antialiasedlinesegment;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.arrayOf;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class AntialiasedLineSegmentRenderingSimpleTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple Antialiased line segment display test",
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
                () -> DisplayTest.runThenClose("Simple Antialiased line segment", 16000),
                AntialiasedLineSegmentRenderingSimpleTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var antialiasedLineSegmentFactory =
                ioModule.provide(AntialiasedLineSegmentRenderableFactory.class);
        topLevelComponent.add(antialiasedLineSegmentFactory.make(
                staticProvider(vertexOf(.1f, .69f)),
                staticProvider(vertexOf(.9f, .71f)),
                staticProvider(Color.RED),
                staticProvider(.005f),
                staticProvider(0.75f),
                staticProvider(0.01f),
                -1,
                randomUUID(),
                topLevelComponent
        ));
    }
}
