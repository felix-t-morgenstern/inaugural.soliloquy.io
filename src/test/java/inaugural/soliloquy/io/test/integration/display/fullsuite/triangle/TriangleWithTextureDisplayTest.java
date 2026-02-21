package inaugural.soliloquy.io.test.integration.display.fullsuite.triangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.arrayOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TriangleWithTextureDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple Triangle display test",
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
                () -> DisplayTest.runThenClose("Simple Triangle", 4000),
                TriangleWithTextureDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var backgroundTex = graphics.getImage(TILE_LOCATION_RELATIVE_LOCATION).textureId();
        var vector1 = staticProvider(vertexOf(0.2f, 0.2f));
        var vector2 = staticProvider(vertexOf(0.8f, 0.4f));
        var vector3 = staticProvider(vertexOf(0.5f, 0.8f));
        var triangleRenderableFactory = ioModule.provide(TriangleRenderableFactory.class);
        topLevelComponent.add(triangleRenderableFactory.make(
                vector1,
                staticProvider(null),
                vector2,
                staticProvider(null),
                vector3,
                staticProvider(null),
                staticProvider(backgroundTex),
                staticProvider(0.3f),
                staticProvider(0.3f),
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
