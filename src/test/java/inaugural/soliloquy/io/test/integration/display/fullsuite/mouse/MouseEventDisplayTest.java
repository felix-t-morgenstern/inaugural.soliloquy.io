package inaugural.soliloquy.io.test.integration.display.fullsuite.mouse;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import inaugural.soliloquy.io.test.integration.display.fullsuite.sprite.SpriteSimpleDisplayTest;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;
import soliloquy.specs.ui.EventInputs;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static java.util.UUID.randomUUID;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static soliloquy.specs.common.entities.Action.action;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class MouseEventDisplayTest extends SpriteSimpleDisplayTest {
    private static final String ON_MOUSE_OVER_ACTION_ID = "onMouseOver";
    private static final String ON_MOUSE_LEAVE_ACTION_ID = "onMouseLeave";
    private static final String ON_MOUSE_PRESS_ACTION_ID = "onMousePress";
    private static final String ON_MOUSE_RELEASE_ACTION_ID = "onMouseRelease";

    private final static Action<EventInputs> ON_MOUSE_OVER_ACTION =
            action(ON_MOUSE_OVER_ACTION_ID, _ -> System.out.println("MOUSE OVER"));
    private final static Action<EventInputs> ON_MOUSE_LEAVE_ACTION =
            action(ON_MOUSE_LEAVE_ACTION_ID, _ -> System.out.println("MOUSE LEAVE"));
    private final static Action<EventInputs> ON_MOUSE_PRESS_ACTION =
            action(ON_MOUSE_PRESS_ACTION_ID, _ -> System.out.println("MOUSE PRESS"));
    private final static Action<EventInputs> ON_MOUSE_RELEASE_ACTION =
            action(ON_MOUSE_RELEASE_ACTION_ID, _ -> System.out.println("MOUSE RELEASE"));

    protected final static AssetDefinitionsDTO ASSET_DTOS = new AssetDefinitionsDTO(
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
    );

    public static void main(String[] args) {
        var displayTest = new DisplayTest(
                setOf(
                        ON_MOUSE_OVER_ACTION,
                        ON_MOUSE_LEAVE_ACTION,
                        ON_MOUSE_PRESS_ACTION,
                        ON_MOUSE_RELEASE_ACTION
                )
        );
        displayTest.runTest(
                "Simple Sprite display test",
                ASSET_DTOS,
                () -> DisplayTest.runThenClose("Simple Sprite", 40000000),
                MouseEventDisplayTest::populateTopLevelComponent
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
                mapOf(
                        GLFW_MOUSE_BUTTON_LEFT,
                        ON_MOUSE_PRESS_ACTION
                ),
                mapOf(
                        GLFW_MOUSE_BUTTON_LEFT,
                        ON_MOUSE_RELEASE_ACTION
                ),
                ON_MOUSE_OVER_ACTION,
                ON_MOUSE_LEAVE_ACTION,
                listOf(),
                dimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
    }
}
