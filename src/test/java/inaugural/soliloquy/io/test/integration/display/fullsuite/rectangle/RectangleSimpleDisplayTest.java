package inaugural.soliloquy.io.test.integration.display.fullsuite.rectangle;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomColor;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class RectangleSimpleDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple Sprite display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{},
                        new FontDefinitionDTO[]{},
                        new SpriteDefinitionDTO[]{},
                        new AnimationDefinitionDTO[]{},
                        new GlobalLoopingAnimationDefinitionDTO[]{},
                        new ImageAssetSetDefinitionDTO[]{},
                        new MouseCursorImageDefinitionDTO[]{},
                        new AnimatedMouseCursorDefinitionDTO[]{},
                        new StaticMouseCursorDefinitionDTO[]{}
                ),
                () -> DisplayTest.runThenClose("Simple Sprite", 4000),
                RectangleSimpleDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var dimensProvider = staticProvider(floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f));
        var rectangleRenderableFactory = ioModule.provide(RectangleRenderableFactory.class);
        rectangleRenderableFactory.make(
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(),
                mapOf(),
                null,
                null,
                dimensProvider,
                randomInt(),
                randomUUID(),
                topLevelComponent
        );
    }
}
