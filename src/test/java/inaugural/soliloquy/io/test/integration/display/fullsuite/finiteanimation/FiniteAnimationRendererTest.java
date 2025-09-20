package inaugural.soliloquy.io.test.integration.display.fullsuite.finiteanimation;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.arrayOf;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class FiniteAnimationRendererTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Finite animation renderer display test",
                new AssetDefinitionsDTO(
                        arrayOf(
                                new ImageDefinitionDTO(EXPLOSION_RELATIVE_LOCATION, false)
                        ),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(
                                ANIMATION_DEF
                        ),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf()
                ),
                () -> DisplayTest.runThenClose("Finite animation renderer",
                        4000),
                FiniteAnimationRendererTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var animation = graphics.getAnimation(EXPLOSION_ANIMATION_ID);

        var height = 0.5f;
        var halfHeight = height / 2f;
        var width = height / DEFAULT_RES.widthToHeightRatio();
        var halfWidth = width / 2f;
        var dimens = staticProvider(floatBoxOf(
                        0.5f - halfWidth,
                        0.5f - halfHeight,
                        0.5f + halfWidth,
                        0.5f + halfHeight
        ));

        var renderableFactory = ioModule.provide(FiniteAnimationRenderableFactory.class);
        renderableFactory.make(
                animation,
                nullProvider(),
                nullProvider(),
                listOf(),
                dimens,
                0,
                randomUUID(),
                topLevelComponent,
                timestamp() + 1000,
                null
        );
    }
}
