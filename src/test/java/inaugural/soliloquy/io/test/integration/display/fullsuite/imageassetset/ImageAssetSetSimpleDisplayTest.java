package inaugural.soliloquy.io.test.integration.display.fullsuite.imageassetset;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.AssetDefinitionsDTO;
import inaugural.soliloquy.io.api.dto.ImageDefinitionDTO;
import inaugural.soliloquy.io.api.dto.SpriteDefinitionDTO;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;

import static inaugural.soliloquy.tools.CheckedExceptionWrapper.sleep;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class ImageAssetSetSimpleDisplayTest extends DisplayTest {
    private static ImageAssetSetRenderable Renderable;

    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Image asset set renderer display test",
                new AssetDefinitionsDTO(
                        arrayOf(
                                new ImageDefinitionDTO(RPG_WEAPONS_RELATIVE_LOCATION, true),
                                new ImageDefinitionDTO(EXPLOSION_RELATIVE_LOCATION, true)
                        ),
                        arrayOf(),
                        arrayOf(
                                new SpriteDefinitionDTO(SPRITE_ID, RPG_WEAPONS_RELATIVE_LOCATION,
                                        266, 271, 313, 343)
                        ),
                        arrayOf(
                                ANIMATION_DEF
                        ),
                        arrayOf(),
                        arrayOf(
                                IMAGE_ASSET_SET_DEF
                        ),
                        arrayOf(),
                        arrayOf(),
                        arrayOf()
                ),
                ImageAssetSetSimpleDisplayTest::runThenClose,
                ImageAssetSetSimpleDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var imageAssetSet = graphics.getImageAssetSet(IMAGE_ASSET_SET_ID);

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

        var renderableFactory = ioModule.provide(ImageAssetSetRenderableFactory.class);

        Renderable = renderableFactory.make(
                imageAssetSet,
                mapOf(
                        IMAGE_ASSET_SET_DISPLAY_PARAM_KEY,
                        IMAGE_ASSET_SET_DISPLAY_PARAM_VAL_SPRITE
                ),
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

    protected static void runThenClose() {
        System.out.println("Image asset set display test started");

        sleep(2000);

        Renderable.setAnimationStart(timestamp());
        Renderable.displayParams().put(
                IMAGE_ASSET_SET_DISPLAY_PARAM_KEY,
                IMAGE_ASSET_SET_DISPLAY_PARAM_VAL_ANIM
        );

        sleep(2000);

        System.out.println("Image asset set display test ended");
    }
}
