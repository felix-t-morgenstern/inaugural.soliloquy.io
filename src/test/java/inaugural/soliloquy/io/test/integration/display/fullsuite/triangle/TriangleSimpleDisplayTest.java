package inaugural.soliloquy.io.test.integration.display.fullsuite.rectangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.arrayOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomHighSaturationColor;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TriangleSimpleDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple Triangle display test",
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
                () -> DisplayTest.runThenClose("Simple Triangle", 4000),
                TriangleSimpleDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var vector1 = staticProvider(vertexOf(0.2f, 0.2f));
        var vector2 = staticProvider(vertexOf(0.8f, 0.4f));
        var vector3 = staticProvider(vertexOf(0.5f, 0.8f));
        var triangleRenderableFactory = ioModule.provide(TriangleRenderableFactory.class);
        topLevelComponent.add(triangleRenderableFactory.make(
                vector1,
                staticProvider(randomHighSaturationColor()),
                vector2,
                staticProvider(randomHighSaturationColor()),
                vector3,
                staticProvider(randomHighSaturationColor()),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(),
                mapOf(),
                null,
                null,
                randomInt(),
                randomUUID(),
                topLevelComponent
        ));
    }
}
