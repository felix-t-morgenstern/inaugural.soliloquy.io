package inaugural.soliloquy.io.test.integration.display.fullsuite.globalloopinganimation;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.GlobalLoopingAnimationDefinitionDTO;
import inaugural.soliloquy.io.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class GlobalLoopingAnimationRendererSimpleDisplayTest extends DisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Global looping animation renderer display test",
                new AssetDefinitionsDTO(
                        arrayOf(
                                new ImageDefinitionDTO(TORCH_RELATIVE_LOCATION, true)
                        ),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(
                                TORCH_ANIMATION_DEF
                        ),
                        arrayOf(
                                new GlobalLoopingAnimationDefinitionDTO(
                                        TORCH_GLOBAL_LOOPING_ANIMATION_ID,
                                        TORCH_ANIMATION_ID,
                                        0
                                )
                        ),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf()
                ),
                () -> DisplayTest.runThenClose("Global looping animation renderer",
                        4000),
                GlobalLoopingAnimationRendererSimpleDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var globalLoopingAnimation =
                graphics.getGlobalLoopingAnimation(TORCH_GLOBAL_LOOPING_ANIMATION_ID);

        var height = 0.5f;
        var halfHeight = height / 2f;
        var width = (height / 2f) / DEFAULT_RES.widthToHeightRatio();
        var halfWidth = width / 2f;
        var dimens = staticProvider(floatBoxOf(
                0.5f - halfWidth,
                0.5f - halfHeight,
                0.5f + halfWidth,
                0.5f + halfHeight
        ));

        globalLoopingAnimation.reset(timestamp());

        var renderableFactory = ioModule.provide(GlobalLoopingAnimationRenderableFactory.class);
        renderableFactory.make(
                globalLoopingAnimation,
                nullProvider(),
                nullProvider(),
                mapOf(),
                mapOf(),
                null,
                null,
                listOf(),
                dimens,
                0,
                randomUUID(),
                topLevelComponent
        );
    }
}
