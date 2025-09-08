package inaugural.soliloquy.io.test.integration.display.fullsuite;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomColor;
import static java.util.UUID.randomUUID;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static soliloquy.specs.common.entities.Action.action;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class CombinationTest extends DisplayTest {
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

    protected final static String SPRITE_ID = "spriteId";
    protected final static String RPG_WEAPONS_RELATIVE_LOCATION =
            "./src/test/resources/images/items/RPG_Weapons.png";
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
                "Combination display test",
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
                () -> DisplayTest.runThenClose("Combination", 4000),
                CombinationTest::populateTopLevelComponent
        );
    }

    protected static void populateTopLevelComponent(IOModule ioModule,
                                                    Component topLevelComponent) {
        var rasterizedLineSegmentFactory =
                ioModule.provide(RasterizedLineSegmentRenderableFactory.class);
        rasterizedLineSegmentFactory.make(
                staticProvider(vertexOf(.1f, .4f)),
                staticProvider(vertexOf(.9f, .4f)),
                staticProvider(6f),
                (short) 0xAAAA,
                (short) 16,
                staticProvider(Color.RED),
                -1,
                randomUUID(),
                topLevelComponent
        );

        var antialiasedLineSegmentFactory =
                ioModule.provide(AntialiasedLineSegmentRenderableFactory.class);
        antialiasedLineSegmentFactory.make(
                staticProvider(vertexOf(.1f, .69f)),
                staticProvider(vertexOf(.9f, .71f)),
                staticProvider(Color.RED),
                staticProvider(.005f),
                staticProvider(0.75f),
                staticProvider(0.01f),
                -1,
                randomUUID(),
                topLevelComponent
        );

        var graphics = ioModule.provide(Graphics.class);

        var rectDimensProvider = staticProvider(floatBoxOf(0.1f, 0.1f, 0.3f, 0.3f));
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

        var staticProviderFactory = ioModule.provide(StaticProviderFactory.class);

        var sprite = graphics.getSprite(SPRITE_ID);
        var dimensProvider = staticProviderFactory.make(randomUUID(),
                floatBoxOf(0.7f, 0.1f, 0.9f, 0.3f));
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
                1,
                randomUUID(),
                topLevelComponent
        );
    }
}
