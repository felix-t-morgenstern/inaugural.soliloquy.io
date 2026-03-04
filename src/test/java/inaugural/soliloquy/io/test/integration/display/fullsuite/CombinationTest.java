package inaugural.soliloquy.io.test.integration.display.fullsuite;

import inaugural.soliloquy.io.IOModule;
import inaugural.soliloquy.io.api.dto.*;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
import soliloquy.specs.io.graphics.renderables.factories.*;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.UUID;
import java.util.function.BiFunction;

import static inaugural.soliloquy.io.api.Constants.STATIC_PROVIDER_FACTORY;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.randomColor;
import static inaugural.soliloquy.tools.random.Random.randomHighSaturationColor;
import static java.util.UUID.randomUUID;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static soliloquy.specs.common.entities.Consumer.consumer;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class CombinationTest extends DisplayTest {
    private static final String ON_MOUSE_OVER_CONSUMER_ID = "onMouseOver";
    private static final String ON_MOUSE_LEAVE_CONSUMER_ID = "onMouseLeave";
    private static final String ON_MOUSE_PRESS_CONSUMER_ID = "onMousePress";
    private static final String ON_MOUSE_RELEASE_CONSUMER_ID = "onMouseRelease";

    private final static Consumer<EventInputs> ON_MOUSE_OVER_CONSUMER =
            consumer(ON_MOUSE_OVER_CONSUMER_ID, _ -> System.out.println("MOUSE OVER"));
    private final static Consumer<EventInputs> ON_MOUSE_LEAVE_CONSUMER =
            consumer(ON_MOUSE_LEAVE_CONSUMER_ID, _ -> System.out.println("MOUSE LEAVE"));
    private final static Consumer<EventInputs> ON_MOUSE_PRESS_CONSUMER =
            consumer(ON_MOUSE_PRESS_CONSUMER_ID, _ -> System.out.println("MOUSE PRESS"));
    private final static Consumer<EventInputs> ON_MOUSE_RELEASE_CONSUMER =
            consumer(ON_MOUSE_RELEASE_CONSUMER_ID, _ -> System.out.println("MOUSE RELEASE"));

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
                "Combination display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{
                                new ImageDefinitionDTO(RPG_WEAPONS_RELATIVE_LOCATION, true),
                                new ImageDefinitionDTO(TILE_LOCATION_RELATIVE_LOCATION, false)
                        },
                        new FontDefinitionDTO[]{
                                CINZEL_DEF
                        },
                        new SpriteDefinitionDTO[]{
                                new SpriteDefinitionDTO(SPRITE_ID, RPG_WEAPONS_RELATIVE_LOCATION,
                                        266, 271, 313, 343)
                        },
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf(),
                        arrayOf()
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
        topLevelComponent.add(antialiasedLineSegmentFactory.make(
                staticProvider(vertexOf(.1f, .69f)),
                staticProvider(vertexOf(.9f, .71f)),
                staticProvider(Color.RED),
                staticProvider(.005f),
                staticProvider(0.75f),
                staticProvider(0.01f),
                -1,
                randomUUID(),
                topLevelComponent
        ));

        var graphics = ioModule.provide(Graphics.class);

        var rectDimensProvider = staticProvider(floatBoxOf(0.1f, 0.1f, 0.3f, 0.3f));
        var backgroundTex = graphics.getImage(TILE_LOCATION_RELATIVE_LOCATION).textureId();
        var rectangleRenderableFactory = ioModule.provide(RectangleRenderableFactory.class);
        var rectRendererWithTex = rectangleRenderableFactory.make(
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
                rectDimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
        topLevelComponent.add(rectRendererWithTex);
        rectangleRenderableFactory.make(
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(randomColor()),
                staticProvider(null),
                staticProvider(1f),
                staticProvider(0f),
                staticProvider(1f),
                staticProvider(0f),
                mapOf(),
                mapOf(),
                null,
                null,
                rectDimensProvider,
                0,
                randomUUID(),
                topLevelComponent
        );
        rectRendererWithTex.setCapturesMouseEvents(true);
        topLevelComponent.add(rectRendererWithTex);

        var triangleRenderableFactory = ioModule.provide(TriangleRenderableFactory.class);
        topLevelComponent.add(triangleRenderableFactory.make(
                staticProvider(vertexOf(0.01f, 0.95f)),
                staticProvider(randomHighSaturationColor()),
                staticProvider(vertexOf(0.99f, 0.95f)),
                staticProvider(randomHighSaturationColor()),
                staticProvider(vertexOf(0.5f, 0.975f)),
                staticProvider(randomHighSaturationColor()),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                mapOf(),
                mapOf(),
                null,
                null,
                0,
                randomUUID(),
                topLevelComponent
        ));

        @SuppressWarnings("rawtypes") BiFunction<UUID, Object, ProviderAtTime>
                staticProviderFactory = ioModule.provide(STATIC_PROVIDER_FACTORY);

        var sprite = graphics.getSprite(SPRITE_ID);
        var spriteDimensProvider = staticProviderFactory.apply(randomUUID(),
                floatBoxOf(0.7f, 0.1f, 0.9f, 0.3f));
        var spriteRenderableFactory = ioModule.provide(SpriteRenderableFactory.class);
        //noinspection unchecked
        topLevelComponent.add(spriteRenderableFactory.make(
                sprite,
                staticProviderFactory.apply(randomUUID(), null),
                staticProviderFactory.apply(randomUUID(), null),
                mapOf(
                        GLFW_MOUSE_BUTTON_LEFT,
                        ON_MOUSE_PRESS_CONSUMER
                ),
                mapOf(
                        GLFW_MOUSE_BUTTON_LEFT,
                        ON_MOUSE_RELEASE_CONSUMER
                ),
                ON_MOUSE_OVER_CONSUMER,
                ON_MOUSE_LEAVE_CONSUMER,
                listOf(),
                spriteDimensProvider,
                1,
                randomUUID(),
                topLevelComponent
        ));

        var cinzel = graphics.getFont(CINZEL_ID);
        var textLineRenderableFactory = ioModule.provide(TextLineRenderableFactory.class);
        topLevelComponent.add(textLineRenderableFactory.make(
                cinzel,
                staticProvider("Hello world!"),
                staticProvider(vertexOf(0.5f, 0.8f)),
                staticProvider(0.08f),
                HorizontalAlignment.LEFT,
                0f,
                mapOf(0, staticProvider(Color.MAGENTA)),
                listOf(),
                listOf(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                nullProvider(),
                0,
                randomUUID(),
                topLevelComponent
        ));
    }
}
