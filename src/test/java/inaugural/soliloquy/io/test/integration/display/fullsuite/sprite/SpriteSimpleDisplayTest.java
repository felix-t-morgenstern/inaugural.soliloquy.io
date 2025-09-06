package inaugural.soliloquy.io.test.integration.display.fullsuite.sprite;

import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

import static inaugural.soliloquy.tools.CheckedExceptionWrapper.sleep;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class SpriteSimpleDisplayTest {
    protected final static String SPRITE_ID = "spriteId";
    protected final static String RPG_WEAPONS_RELATIVE_LOCATION =
            "./src/test/resources/images/items/RPG_Weapons.png";

    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple Sprite display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{
                                new ImageDefinitionDTO(RPG_WEAPONS_RELATIVE_LOCATION, true)
                        },
                        new FontDefinitionDTO[]{},
                        new SpriteDefinitionDTO[]{
                                new SpriteDefinitionDTO(SPRITE_ID, RPG_WEAPONS_RELATIVE_LOCATION,
                                        266, 271, 313, 343)
                        },
                        new AnimationDefinitionDTO[]{},
                        new GlobalLoopingAnimationDefinitionDTO[]{},
                        new ImageAssetSetDefinitionDTO[]{},
                        new MouseCursorImageDefinitionDTO[]{},
                        new AnimatedMouseCursorDefinitionDTO[]{},
                        new StaticMouseCursorDefinitionDTO[]{}
                ),
                () -> {
                        System.out.println("Simple Sprite display test started");
                        sleep(4000);
                        System.out.println("Simple Sprite display test ended");
                },
                (ioModule, topLevelComponent) -> {
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
                            listOf(),
                            dimensProvider,
                            0,
                            randomUUID(),
                            topLevelComponent
                    );
                }
        );
    }
}
