package inaugural.soliloquy.io.test.integration.display.fullsuite.rectangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class RectangleTextureDisplayTest extends DisplayTest {
    private final static String TILE_LOCATION_RELATIVE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Texture Rectangle display test",
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
                () -> DisplayTest.runThenClose("Texture Rectangle", 4000),
                RectangleTextureDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var backgroundTex = graphics.getImage(TILE_LOCATION_RELATIVE_LOCATION).textureId();
        var dimensProvider = staticProvider(floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f));
        var rectangleRenderableFactory = ioModule.provide(RectangleRenderableFactory.class);
        topLevelComponent.add(rectangleRenderableFactory.make(
                staticProvider(randomHighSaturationColor()),
                staticProvider(randomHighSaturationColor()),
                staticProvider(randomHighSaturationColor()),
                staticProvider(randomHighSaturationColor()),
                staticProvider(backgroundTex),
                staticProvider(0.25f),
                staticProvider(0f),
                staticProvider(0.25f),
                staticProvider(0f),
                mapOf(),
                mapOf(),
                null,
                null,
                dimensProvider,
                randomInt(),
                randomUUID(),
                topLevelComponent
        ));
    }
}
