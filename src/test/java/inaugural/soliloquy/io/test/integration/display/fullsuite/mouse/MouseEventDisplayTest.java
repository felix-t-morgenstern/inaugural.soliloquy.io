package inaugural.soliloquy.io.test.integration.display.fullsuite.mouse;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.integration.display.fullsuite.DisplayTest;
import inaugural.soliloquy.io.test.integration.display.fullsuite.sprite.SpriteSimpleDisplayTest;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.util.UUID;
import java.util.function.BiFunction;

import static inaugural.soliloquy.io.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.io.api.Constants.STATIC_PROVIDER_FACTORY;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomColor;
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

    private final static String TILE_LOCATION_RELATIVE_LOCATION =
            "./src/test/resources/images/tiles/sergey-shmidt-koy6FlCCy5s-unsplash.jpg";

    public static void main(String[] args) {
        var displayTest = new DisplayTest(
                setOf(
                        action(ON_MOUSE_OVER_ACTION_ID, _ -> System.out.println("MOUSE OVER")),
                        action(ON_MOUSE_LEAVE_ACTION_ID, _ -> System.out.println("MOUSE LEAVE")),
                        action(ON_MOUSE_PRESS_ACTION_ID, _ -> System.out.println("MOUSE PRESS")),
                        action(ON_MOUSE_RELEASE_ACTION_ID, _ -> System.out.println("MOUSE RELEASE"))
                )
        );
        displayTest.runTest(
                "Mouse event display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{
                                new ImageDefinitionDTO(RPG_WEAPONS_RELATIVE_LOCATION, true),
                                new ImageDefinitionDTO(TILE_LOCATION_RELATIVE_LOCATION, false)
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
                () -> DisplayTest.runThenClose("Mouse event", 4000),
                MouseEventDisplayTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var graphics = ioModule.provide(Graphics.class);

        var rectDimensProvider = staticProvider(floatBoxOf(0f, 0f, 1f, 1f));
        var backgroundTex = graphics.getImage(TILE_LOCATION_RELATIVE_LOCATION).textureId();
        var rectangleRenderableFactory = ioModule.provide(RectangleRenderableFactory.class);
        var rectRenderer = rectangleRenderableFactory.make(
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(backgroundTex),
                staticProvider(1f),
                staticProvider(1f),
                mapOf(),
                mapOf(),
                null,
                null,
                rectDimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
        rectRenderer.setCapturesMouseEvents(true);

        @SuppressWarnings("rawtypes") BiFunction<UUID, Object, ProviderAtTime>
                staticProviderFactory = ioModule.provide(STATIC_PROVIDER_FACTORY);

        var sprite = graphics.getSprite(SPRITE_ID);
        var dimensProvider = staticProviderFactory.apply(randomUUID(),
                floatBoxOf(0.25f, 0.125f, 0.75f, 0.875f));
        var spriteRenderableFactory = ioModule.provide(SpriteRenderableFactory.class);
        //noinspection unchecked
        var spriteRenderable = spriteRenderableFactory.make(
                sprite,
                staticProviderFactory.apply(randomUUID(), null),
                staticProviderFactory.apply(randomUUID(), null),
                mapOf(
                        LEFT_MOUSE_BUTTON,
                        ON_MOUSE_PRESS_ACTION
                ),
                mapOf(
                        LEFT_MOUSE_BUTTON,
                        ON_MOUSE_RELEASE_ACTION
                ),
                ON_MOUSE_OVER_ACTION,
                ON_MOUSE_LEAVE_ACTION,
                listOf(),
                dimensProvider,
                1,
                randomUUID(),
                topLevelComponent
        );
        spriteRenderable.setCapturesMouseEvents(true);
    }
}
