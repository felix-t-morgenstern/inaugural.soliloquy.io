package inaugural.soliloquy.io.test.integration.display.fullsuite.colorshifting;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import inaugural.soliloquy.io.test.integration.display.fullsuite.sprite.SpriteSimpleDisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorRotationShift.rotationShift;

public class RotationShiftTest extends SpriteSimpleDisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Rotation shift display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Rotation shift", 4000),
                RotationShiftTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);
        var sprite = graphics.getSprite(SPRITE_ID);
        var staticProviderFactory = ioModule.provide(StaticProviderFactory.class);
        var dimensProvider = staticProviderFactory.make(randomUUID(),
                floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f));
        var spriteRenderableFactory = ioModule.provide(SpriteRenderableFactory.class);
        spriteRenderableFactory.make(
                sprite,
                staticProviderFactory.make(randomUUID(), null),
                staticProviderFactory.make(randomUUID(), null),
                listOf(
                        rotationShift(
                                staticProviderFactory.make(randomUUID(), 0.5f),
                                false
                        )
                ),
                dimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
    }
}
