package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.api.dto.SpriteDTO;
import inaugural.soliloquy.graphics.bootstrap.SpritePreloaderWorker;
import inaugural.soliloquy.graphics.test.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.fakes.FakeRegistry;
import inaugural.soliloquy.graphics.test.fakes.FakeSpriteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpritePreloaderWorkerTests {
    private final java.util.Map<String, Image> IMAGES = new HashMap<>();
    private final FakeSpriteFactory FACTORY = new FakeSpriteFactory();
    private final Registry<Sprite> REGISTRY = new FakeRegistry<>();

    private SpritePreloaderWorker _spritePreloaderWorker;

    @BeforeEach
    void setUp() {
        _spritePreloaderWorker = new SpritePreloaderWorker(IMAGES, FACTORY, REGISTRY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpritePreloaderWorker(null, FACTORY, REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new SpritePreloaderWorker(IMAGES, null, REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new SpritePreloaderWorker(IMAGES, FACTORY, null));
    }

    @Test
    void testLoad() {
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";
        String relativeLocation3 = "relativeLocation3";

        IMAGES.put(relativeLocation1, new FakeImage(relativeLocation1));
        IMAGES.put(relativeLocation2, new FakeImage(relativeLocation2));
        IMAGES.put(relativeLocation3, new FakeImage(relativeLocation3));

        SpriteDTO spriteDTO1 = new SpriteDTO("sprite1Id", relativeLocation1, 12, 34, 56, 78);
        SpriteDTO spriteDTO2 = new SpriteDTO("sprite2Id", relativeLocation2, 21, 43, 65, 87);
        SpriteDTO spriteDTO3 = new SpriteDTO("sprite3Id", relativeLocation3, 11, 22, 33, 44);

        List<SpriteDTO> spriteDTOs = new ArrayList<>(Arrays.asList(
                spriteDTO1, spriteDTO2, spriteDTO3
        ));

        _spritePreloaderWorker.load(spriteDTOs);

        assertEquals(spriteDTOs.size(), REGISTRY.size());
        spriteDTOs.forEach(dto -> {
            SpriteDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(SpriteDefinition.class.getCanonicalName(),
                    createdDefinition.getInterfaceName());
            assertSame(IMAGES.get(dto.imgLoc), createdDefinition.image());
            assertEquals(dto.leftX, createdDefinition.leftX());
            assertEquals(dto.topY, createdDefinition.topY());
            assertEquals(dto.rightX, createdDefinition.rightX());
            assertEquals(dto.bottomY, createdDefinition.bottomY());
            assertEquals(SpriteDefinition.class.getCanonicalName(),
                    createdDefinition.getInterfaceName());

            Sprite sprite = REGISTRY.get(createdDefinition.assetId());
            assertTrue(FACTORY.OUTPUTS.contains(sprite));
        });
    }

    @Test
    void testLoadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spritePreloaderWorker.load(null));
    }
}
