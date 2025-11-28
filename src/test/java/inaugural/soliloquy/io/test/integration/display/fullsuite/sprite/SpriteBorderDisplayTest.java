package inaugural.soliloquy.io.test.integration.display.fullsuite.sprite;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomColor;
import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class SpriteBorderDisplayTest extends SpriteSimpleDisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Border Sprite display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Border Sprite", 4000),
                SpriteBorderDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var sprite = graphics.getSprite(SPRITE_ID);
        var dimensProvider = staticProvider(floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f));
        var spriteRenderableFactory = ioModule.provide(SpriteRenderableFactory.class);
        topLevelComponent.add(spriteRenderableFactory.make(
                sprite,
                staticProvider(randomFloatInRange(0.01f, 0.1f)),
                staticProvider(randomColor()),
                listOf(),
                dimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        ));
    }
}
