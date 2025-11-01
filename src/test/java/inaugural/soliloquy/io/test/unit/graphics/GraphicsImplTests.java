package inaugural.soliloquy.io.test.unit.graphics;

import inaugural.soliloquy.io.graphics.GraphicsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.Graphics;
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.renderables.Component;

import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class GraphicsImplTests {
    private final String IMAGE_ID = randomString();

    private final String SPRITE_ID = randomString();
    private final LookupAndEntitiesWithId<Sprite> MOCK_SPRITE_AND_LOOKUP = generateMockLookupFunctionWithId(Sprite.class, SPRITE_ID);
    private final Sprite MOCK_SPRITE = MOCK_SPRITE_AND_LOOKUP.entities.getFirst();
    private final Function<String, Sprite> MOCK_GET_SPRITE = MOCK_SPRITE_AND_LOOKUP.lookup;

    private final String ANIMATION_ID = randomString();
    private final LookupAndEntitiesWithId<Animation> MOCK_ANIMATION_AND_LOOKUP = generateMockLookupFunctionWithId(Animation.class, ANIMATION_ID);
    private final Animation MOCK_ANIMATION = MOCK_ANIMATION_AND_LOOKUP.entities.getFirst();
    private final Function<String, Animation> MOCK_GET_ANIMATION = MOCK_ANIMATION_AND_LOOKUP.lookup;

    private final String GLOBAL_LOOPING_ANIMATION_ID = randomString();
    private final LookupAndEntitiesWithId<GlobalLoopingAnimation> MOCK_GLOBAL_LOOPING_ANIMATION_AND_LOOKUP = generateMockLookupFunctionWithId(GlobalLoopingAnimation.class, GLOBAL_LOOPING_ANIMATION_ID);
    private final GlobalLoopingAnimation MOCK_GLOBAL_LOOPING_ANIMATION = MOCK_GLOBAL_LOOPING_ANIMATION_AND_LOOKUP.entities.getFirst();
    private final Function<String, GlobalLoopingAnimation> MOCK_GET_GLOBAL_LOOPING_ANIMATION = MOCK_GLOBAL_LOOPING_ANIMATION_AND_LOOKUP.lookup;

    private final String IMAGE_ASSET_SET_ID = randomString();
    private final LookupAndEntitiesWithId<ImageAssetSet> MOCK_IMAGE_ASSET_SET_AND_LOOKUP = generateMockLookupFunctionWithId(ImageAssetSet.class, IMAGE_ASSET_SET_ID);
    private final ImageAssetSet MOCK_IMAGE_ASSET_SET = MOCK_IMAGE_ASSET_SET_AND_LOOKUP.entities.getFirst();
    private final Function<String, ImageAssetSet> MOCK_GET_IMAGE_ASSET_SET = MOCK_IMAGE_ASSET_SET_AND_LOOKUP.lookup;

    private final String FONT_ID = randomString();
    private final LookupAndEntitiesWithId<Font> MOCK_FONT_AND_LOOKUP = generateMockLookupFunctionWithId(Font.class, FONT_ID);
    private final Font MOCK_FONT = MOCK_FONT_AND_LOOKUP.entities.getFirst();
    private final Function<String, Font> MOCK_GET_FONT = MOCK_FONT_AND_LOOKUP.lookup;

    private final UUID COMPONENT_ID = randomUUID();
    private final LookupAndEntitiesWithUuid<Component> MOCK_COMPONENT_AND_LOOKUP =
            generateMockLookupFunctionWithUuid(Component.class, COMPONENT_ID);
    private final Component MOCK_COMPONENT = MOCK_COMPONENT_AND_LOOKUP.entities.getFirst();
    private final Function<UUID, Component> MOCK_GET_COMPONENT = MOCK_COMPONENT_AND_LOOKUP.lookup;

    @Mock private Image mockImage;
    private Function<String, Image> mockGetImage;

    private Graphics graphics;

    @BeforeEach
    public void setUp() {
        mockGetImage = generateMockLookupFunction(pairOf(IMAGE_ID, mockImage));

        graphics = new GraphicsImpl(
                mockGetImage,
                MOCK_GET_SPRITE,
                MOCK_GET_ANIMATION,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                MOCK_GET_IMAGE_ASSET_SET,
                MOCK_GET_FONT,
                MOCK_GET_COMPONENT
        );
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                mockGetImage,
                null,
                MOCK_GET_ANIMATION,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                MOCK_GET_IMAGE_ASSET_SET,
                MOCK_GET_FONT,
                MOCK_GET_COMPONENT
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                null,
                MOCK_GET_SPRITE,
                MOCK_GET_ANIMATION,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                MOCK_GET_IMAGE_ASSET_SET,
                MOCK_GET_FONT,
                MOCK_GET_COMPONENT
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                mockGetImage,
                MOCK_GET_SPRITE,
                null,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                MOCK_GET_IMAGE_ASSET_SET,
                MOCK_GET_FONT,
                MOCK_GET_COMPONENT
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                mockGetImage,
                MOCK_GET_SPRITE,
                MOCK_GET_ANIMATION,
                null,
                MOCK_GET_IMAGE_ASSET_SET,
                MOCK_GET_FONT,
                MOCK_GET_COMPONENT
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                mockGetImage,
                MOCK_GET_SPRITE,
                MOCK_GET_ANIMATION,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                null,
                MOCK_GET_FONT,
                MOCK_GET_COMPONENT
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                mockGetImage,
                MOCK_GET_SPRITE,
                MOCK_GET_ANIMATION,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                MOCK_GET_IMAGE_ASSET_SET,
                null,
                MOCK_GET_COMPONENT
        ));
        assertThrows(IllegalArgumentException.class, () -> new GraphicsImpl(
                mockGetImage,
                MOCK_GET_SPRITE,
                MOCK_GET_ANIMATION,
                MOCK_GET_GLOBAL_LOOPING_ANIMATION,
                MOCK_GET_IMAGE_ASSET_SET,
                MOCK_GET_FONT,
                null
        ));
    }

    @Test
    public void testGetSprite() {
        var sprite = graphics.getSprite(SPRITE_ID);
        var resultFromNull = graphics.getSprite(null);
        var resultFromEmpty = graphics.getSprite("");

        assertSame(MOCK_SPRITE, sprite);
        assertNull(resultFromNull);
        assertNull(resultFromEmpty);
        verify(MOCK_GET_SPRITE, once()).apply(SPRITE_ID);
    }

    @Test
    public void testGetAnimation() {
        var animation = graphics.getAnimation(ANIMATION_ID);
        var resultFromNull = graphics.getAnimation(null);
        var resultFromEmpty = graphics.getAnimation("");

        assertSame(MOCK_ANIMATION, animation);
        assertNull(resultFromNull);
        assertNull(resultFromEmpty);
        verify(MOCK_GET_ANIMATION, once()).apply(ANIMATION_ID);
    }

    @Test
    public void testGetGlobalLoopingAnimation() {
        var globalLoopingAnimation = graphics.getGlobalLoopingAnimation(GLOBAL_LOOPING_ANIMATION_ID);
        var resultFromNull = graphics.getGlobalLoopingAnimation(null);
        var resultFromEmpty = graphics.getGlobalLoopingAnimation("");

        assertSame(MOCK_GLOBAL_LOOPING_ANIMATION, globalLoopingAnimation);
        assertNull(resultFromNull);
        assertNull(resultFromEmpty);
        verify(MOCK_GET_GLOBAL_LOOPING_ANIMATION, once()).apply(GLOBAL_LOOPING_ANIMATION_ID);
    }

    @Test
    public void testGetImageAssetSet() {
        var imageAssetSet = graphics.getImageAssetSet(IMAGE_ASSET_SET_ID);
        var resultFromNull = graphics.getImageAssetSet(null);
        var resultFromEmpty = graphics.getImageAssetSet("");

        assertSame(MOCK_IMAGE_ASSET_SET, imageAssetSet);
        assertNull(resultFromNull);
        assertNull(resultFromEmpty);
        verify(MOCK_GET_IMAGE_ASSET_SET, once()).apply(IMAGE_ASSET_SET_ID);
    }

    @Test
    public void testGetFont() {
        var font = graphics.getFont(FONT_ID);
        var resultFromNull = graphics.getFont(null);
        var resultFromEmpty = graphics.getFont("");

        assertSame(MOCK_FONT, font);
        assertNull(resultFromNull);
        assertNull(resultFromEmpty);
        verify(MOCK_GET_FONT, once()).apply(FONT_ID);
    }

    @Test
    public void testGetComponent() {
        var component = graphics.getComponent(COMPONENT_ID);
        var resultFromNull = graphics.getComponent(null);

        assertSame(MOCK_COMPONENT, component);
        assertNull(resultFromNull);
        verify(MOCK_GET_COMPONENT, once()).apply(COMPONENT_ID);
    }
}
