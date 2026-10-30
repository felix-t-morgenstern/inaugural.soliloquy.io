package inaugural.soliloquy.io.test.display.mouse;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import inaugural.soliloquy.io.test.display.DisplayTest;
import inaugural.soliloquy.io.test.display.sprite.SpriteSimpleDisplayTest;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.UUID;
import java.util.function.BiFunction;

import static inaugural.soliloquy.io.api.Constants.LEFT_MOUSE_BUTTON;
import static inaugural.soliloquy.io.api.Constants.STATIC_PROVIDER_FACTORY;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static java.util.UUID.randomUUID;
import static soliloquy.specs.common.entities.Consumer.consumer;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class MouseEventDisplayTest extends SpriteSimpleDisplayTest {
    private static final String ON_MOUSE_PRESS_CONSUMER_ID = "onMousePress";
    private static final String ON_MOUSE_RELEASE_CONSUMER_ID = "onMouseRelease";
    private static final String ON_WHEEL_SCROLL_CONSUMER_ID = "onWheelScroll";
    private static final String ON_MOUSE_OVER_CONSUMER_ID = "onMouseOver";
    private static final String ON_MOUSE_LEAVE_CONSUMER_ID = "onMouseLeave";

    private final static Consumer<EventInputs> ON_MOUSE_PRESS_CONSUMER =
            consumer(ON_MOUSE_PRESS_CONSUMER_ID, _ -> System.out.println("MOUSE PRESS"));
    private final static Consumer<EventInputs> ON_MOUSE_RELEASE_CONSUMER =
            consumer(ON_MOUSE_RELEASE_CONSUMER_ID, _ -> System.out.println("MOUSE RELEASE"));
    private final static Consumer<EventInputs> ON_WHEEL_SCROLL_CONSUMER =
            consumer(ON_WHEEL_SCROLL_CONSUMER_ID, e -> System.out.println("WHEEL SCROLL OFFSET = " + e.scrollWheelOffset));
    private final static Consumer<EventInputs> ON_MOUSE_OVER_CONSUMER =
            consumer(ON_MOUSE_OVER_CONSUMER_ID, _ -> System.out.println("MOUSE OVER"));
    private final static Consumer<EventInputs> ON_MOUSE_LEAVE_CONSUMER =
            consumer(ON_MOUSE_LEAVE_CONSUMER_ID, _ -> System.out.println("MOUSE LEAVE"));

    public static void main(String[] args) {
        var displayTest = new DisplayTest(
                setOf(
                        consumer(ON_MOUSE_OVER_CONSUMER_ID, _ -> System.out.println("MOUSE OVER")),
                        consumer(ON_MOUSE_LEAVE_CONSUMER_ID, _ -> System.out.println("MOUSE LEAVE")),
                        consumer(ON_MOUSE_PRESS_CONSUMER_ID, _ -> System.out.println("MOUSE PRESS")),
                        consumer(ON_MOUSE_RELEASE_CONSUMER_ID, _ -> System.out.println("MOUSE RELEASE"))
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
                () -> DisplayTest.runThenClose("Mouse event", 8000),
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
                staticProvider(0f),
                staticProvider(1f),
                staticProvider(0f),
                mapOf(),
                mapOf(),
                null,
                null,
                null,
                rectDimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
        rectRenderer.setCapturesMouseButtonEvents(true);
        topLevelComponent.add(rectRenderer);

        @SuppressWarnings("rawtypes") BiFunction<UUID, Object, ProviderAtTime>
                staticProviderFactory = ioModule.provide(STATIC_PROVIDER_FACTORY);

        var sprite = graphics.getSprite(SPRITE_ID);
        var dimensProvider = staticProviderFactory.apply(randomUUID(),
                floatBoxOf(0.375f, 0.3125f, 0.625f, 0.6875f));
        var spriteRenderableFactory = ioModule.provide(SpriteRenderableFactory.class);
        //noinspection unchecked
        var spriteRenderable = spriteRenderableFactory.make(
                sprite,
                staticProviderFactory.apply(randomUUID(), null),
                staticProviderFactory.apply(randomUUID(), null),
                mapOf(
                        LEFT_MOUSE_BUTTON,
                        ON_MOUSE_PRESS_CONSUMER
                ),
                mapOf(
                        LEFT_MOUSE_BUTTON,
                        ON_MOUSE_RELEASE_CONSUMER
                ),
                ON_WHEEL_SCROLL_CONSUMER,
                ON_MOUSE_OVER_CONSUMER,
                ON_MOUSE_LEAVE_CONSUMER,
                listOf(),
                dimensProvider,
                1,
                randomUUID(),
                topLevelComponent
        );
        spriteRenderable.setCapturesMouseButtonEvents(true);

        topLevelComponent.add(spriteRenderable);
        var vector1 = staticProvider(vertexOf(0.05f, 0.05f));
        var vector2 = staticProvider(vertexOf(0.2f, 0.05f));
        var vector3 = staticProvider(vertexOf(0.05f, 0.95f));
        var triangleRenderableFactory = ioModule.provide(TriangleRenderableFactory.class);
        topLevelComponent.add(triangleRenderableFactory.make(
                vector1,
                staticProvider(Color.ORANGE),
                vector2,
                staticProvider(Color.ORANGE),
                vector3,
                staticProvider(Color.ORANGE),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(
                        LEFT_MOUSE_BUTTON,
                        ON_MOUSE_PRESS_CONSUMER
                ),
                mapOf(
                        LEFT_MOUSE_BUTTON,
                        ON_MOUSE_RELEASE_CONSUMER
                ),
                ON_WHEEL_SCROLL_CONSUMER,
                ON_MOUSE_OVER_CONSUMER,
                ON_MOUSE_LEAVE_CONSUMER,
                randomInt(),
                randomUUID(),
                topLevelComponent
        ));
    }
}
