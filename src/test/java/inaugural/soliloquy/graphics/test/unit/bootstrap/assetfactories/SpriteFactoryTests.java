package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.SpriteFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;

public class SpriteFactoryTests {
    private final String RELATIVE_LOC = randomString();
    private final int IMAGE_WIDTH = 123;
    private final int IMAGE_HEIGHT = 456;
    private final Image IMAGE = new FakeImage(RELATIVE_LOC, IMAGE_WIDTH, IMAGE_HEIGHT);
    private final int LEFT_X = 12;
    private final int TOP_Y = 34;
    private final int RIGHT_X = 56;
    private final int BOTTOM_Y = 78;
    private final String ID = randomString();

    private AssetFactory<SpriteDefinition, Sprite> spriteFactory;

    @BeforeEach
    public void setUp() {
        spriteFactory = new SpriteFactory();
    }

    @Test
    public void testCreate() {
        var definition = new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y);

        var createdSprite = spriteFactory.make(definition);

        assertNotNull(createdSprite);
        assertEquals(ID, createdSprite.id());
        assertSame(IMAGE, createdSprite.image());
        assertEquals(RELATIVE_LOC, createdSprite.image().relativeLocation());
        assertEquals(LEFT_X, createdSprite.leftX());
        assertEquals(TOP_Y, createdSprite.topY());
        assertEquals(RIGHT_X, createdSprite.rightX());
        assertEquals(BOTTOM_Y, createdSprite.bottomY());
    }

    @Test
    public void testCreateWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(null));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(null, IMAGE, LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition("", IMAGE, LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, null, LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, new FakeImage(null, IMAGE_WIDTH, IMAGE_HEIGHT), LEFT_X,
                        TOP_Y, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, new FakeImage("", IMAGE_WIDTH, IMAGE_HEIGHT), LEFT_X,
                        TOP_Y, RIGHT_X, BOTTOM_Y)
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, new FakeImage(RELATIVE_LOC, 0, IMAGE_HEIGHT),
                        LEFT_X, TOP_Y, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, new FakeImage(RELATIVE_LOC, IMAGE_WIDTH, 0), LEFT_X,
                        TOP_Y, RIGHT_X, BOTTOM_Y)
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, -1, TOP_Y, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, -1, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, -1, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, RIGHT_X, -1)
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, LEFT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, RIGHT_X, TOP_Y)
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, IMAGE_WIDTH + 1, TOP_Y, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, IMAGE_HEIGHT + 1, RIGHT_X, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, IMAGE_WIDTH + 1, BOTTOM_Y)
        ));
        assertThrows(IllegalArgumentException.class, () -> spriteFactory.make(
                new SpriteDefinition(ID, IMAGE, LEFT_X, TOP_Y, RIGHT_X, IMAGE_HEIGHT + 1)
        ));
    }
}
