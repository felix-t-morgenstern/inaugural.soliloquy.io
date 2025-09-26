package inaugural.soliloquy.io.test.integration.display.fullsuite.audio;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTestMethods;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;

import static inaugural.soliloquy.io.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomColor;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.entities.Action.action;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class PlaySoundOnMouseEventsDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Play sound on mouse events display test",
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
                () -> DisplayTest.runThenClose("Play sound on mouse events", 4000),
                PlaySoundOnMouseEventsDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var rectDimensProvider = staticProvider(floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f));
        var rectangleRenderableFactory = ioModule.provide(RectangleRenderableFactory.class);

        var rectRenderable = rectangleRenderableFactory.make(
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(
                        pairOf(
                                LEFT_MOUSE_BUTTON,
                                action(
                                        "playMousePressSound",
                                        DisplayTestMethods::playMousePressSound
                                )
                        )
                ),
                mapOf(
                        pairOf(
                                LEFT_MOUSE_BUTTON,
                                action(
                                        "playMouseReleaseSound",
                                        DisplayTestMethods::playMouseReleaseSound
                                )
                        )
                ),
                null,
                null,
                rectDimensProvider,
                randomInt(),
                randomUUID(),
                topLevelComponent
        );
        rectRenderable.setCapturesMouseEvents(true);
    }
}
