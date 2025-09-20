package inaugural.soliloquy.io.test.integration.display.fullsuite.sprite;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.arrayOf;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class SpriteSimpleDisplayTest extends DisplayTest {
    protected final static AssetDefinitionsDTO ASSET_DTOS = new AssetDefinitionsDTO(
            new ImageDefinitionDTO[]{
                    new ImageDefinitionDTO(RPG_WEAPONS_RELATIVE_LOCATION, true)
            },
            arrayOf(),
            arrayOf(
                    new SpriteDefinitionDTO(SPRITE_ID, RPG_WEAPONS_RELATIVE_LOCATION,
                            266, 271, 313, 343)
            ),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf(),
            arrayOf()
    );

    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple Sprite display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Simple Sprite", 4000),
                SpriteSimpleDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var sprite = graphics.getSprite(SPRITE_ID);

        var dimensProvider = staticProvider(floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f));

        var spriteRenderableFactory = ioModule.provide(SpriteRenderableFactory.class);
        spriteRenderableFactory.make(
                sprite,
                staticProvider(null),
                staticProvider(null),
                listOf(),
                dimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
    }
}
