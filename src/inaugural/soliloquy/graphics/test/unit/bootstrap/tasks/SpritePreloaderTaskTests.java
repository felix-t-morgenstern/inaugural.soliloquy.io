package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.SpriteDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.SpritePreloaderTask;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SpritePreloaderTaskTests {
    private final java.util.Map<String, Image> IMAGES = new HashMap<>();
    private final FakeSpriteFactory FACTORY = new FakeSpriteFactory();
    private final ArrayList<SpriteDefinitionDTO> SPRITE_DEFINITION_DTOS = new ArrayList<>();
    private final FakeRegistry<Sprite> REGISTRY = new FakeRegistry<Sprite>();

    private SpritePreloaderTask _spritePreloaderTask;

    @BeforeEach
    void setUp() {
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";
        String relativeLocation3 = "relativeLocation3";

        IMAGES.put(relativeLocation1, new FakeImage(relativeLocation1));
        IMAGES.put(relativeLocation2, new FakeImage(relativeLocation2));
        IMAGES.put(relativeLocation3, new FakeImage(relativeLocation3));

        SpriteDefinitionDTO spriteDefinitionDTO1 = new SpriteDefinitionDTO("sprite1Id", relativeLocation1, 12, 34, 56, 78);
        SpriteDefinitionDTO spriteDefinitionDTO2 = new SpriteDefinitionDTO("sprite2Id", relativeLocation2, 21, 43, 65, 87);
        SpriteDefinitionDTO spriteDefinitionDTO3 = new SpriteDefinitionDTO("sprite3Id", relativeLocation3, 11, 22, 33, 44);

        SPRITE_DEFINITION_DTOS.add(spriteDefinitionDTO1);
        SPRITE_DEFINITION_DTOS.add(spriteDefinitionDTO2);
        SPRITE_DEFINITION_DTOS.add(spriteDefinitionDTO3);

        _spritePreloaderTask = new SpritePreloaderTask(IMAGES::get, FACTORY,
                SPRITE_DEFINITION_DTOS, REGISTRY::add);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(null, FACTORY, SPRITE_DEFINITION_DTOS, REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, null, SPRITE_DEFINITION_DTOS,
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        null,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<>(),
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(null);
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO(null, "relativeLocation1",
                                    12, 34, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("", "relativeLocation1",
                                    12, 34, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("sprite1Id", null,
                                    12, 34, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("sprite1Id", "",
                                    12, 34, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    -1, 34, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    12, -1, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    56, 34, 56, 78));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY,
                        new ArrayList<SpriteDefinitionDTO>() {{
                            add(new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    12, 78, 56, 78));
                        }},
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, FACTORY, SPRITE_DEFINITION_DTOS, null));
    }

    @Test
    void testRun() {
        _spritePreloaderTask.run();

        assertEquals(SPRITE_DEFINITION_DTOS.size(), REGISTRY.size());
        SPRITE_DEFINITION_DTOS.forEach(dto -> {
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

            Sprite sprite = REGISTRY.get(createdDefinition.id());
            assertTrue(FACTORY.OUTPUTS.contains(sprite));
        });
    }
}
