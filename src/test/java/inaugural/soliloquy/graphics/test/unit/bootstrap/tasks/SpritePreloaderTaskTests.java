package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.SpriteDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.SpritePreloaderTask;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeSpriteFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;


import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpritePreloaderTaskTests {
    private final Map<String, Image> IMAGES = mapOf();
    private final FakeSpriteFactory FACTORY = new FakeSpriteFactory();
    private final List<SpriteDefinitionDTO> SPRITE_DEFINITION_DTOS = listOf();
    
    @Mock private Consumer<Sprite> addSprite;

    private SpritePreloaderTask spritePreloaderTask;

    @BeforeEach
    public void setUp() {
        var relativeLocation1 = "relativeLocation1";
        var relativeLocation2 = "relativeLocation2";
        var relativeLocation3 = "relativeLocation3";

        IMAGES.put(relativeLocation1, new FakeImage(relativeLocation1));
        IMAGES.put(relativeLocation2, new FakeImage(relativeLocation2));
        IMAGES.put(relativeLocation3, new FakeImage(relativeLocation3));

        var spriteDefinitionDTO1 = new SpriteDefinitionDTO("sprite1Id", relativeLocation1, 12, 34, 56, 78);
        var spriteDefinitionDTO2 = new SpriteDefinitionDTO("sprite2Id", relativeLocation2, 21, 43, 65, 87);
        var spriteDefinitionDTO3 = new SpriteDefinitionDTO("sprite3Id", relativeLocation3, 0, 0, 33, 44);

        SPRITE_DEFINITION_DTOS.add(spriteDefinitionDTO1);
        SPRITE_DEFINITION_DTOS.add(spriteDefinitionDTO2);
        SPRITE_DEFINITION_DTOS.add(spriteDefinitionDTO3);

        spritePreloaderTask = new SpritePreloaderTask(IMAGES::get, SPRITE_DEFINITION_DTOS,
                FACTORY, addSprite);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(null, SPRITE_DEFINITION_DTOS, FACTORY, addSprite));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, SPRITE_DEFINITION_DTOS, null,
                        addSprite));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, null, FACTORY, addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, listOf(), FACTORY, addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf((SpriteDefinitionDTO) null),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO(null, "relativeLocation1",
                                    12, 34, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("", "relativeLocation1",
                                    12, 34, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", null,
                                    12, 34, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "",
                                    12, 34, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    -1, 34, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    12, -1, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    56, 34, 56, 78)
                        ),
                        FACTORY,
                        addSprite));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    12, 78, 56, 78)
                        ),
                        FACTORY,
                        addSprite));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, SPRITE_DEFINITION_DTOS, FACTORY, null));
    }

    @Test
    public void testRun() {
        spritePreloaderTask.run();

        verify(addSprite, times(SPRITE_DEFINITION_DTOS.size())).accept(any());
        SPRITE_DEFINITION_DTOS.forEach(dto -> {
            var createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertSame(IMAGES.get(dto.imgLoc), createdDefinition.image());
            assertEquals(dto.leftX, createdDefinition.leftX());
            assertEquals(dto.topY, createdDefinition.topY());
            assertEquals(dto.rightX, createdDefinition.rightX());
            assertEquals(dto.bottomY, createdDefinition.bottomY());

            FACTORY.OUTPUTS.forEach(output -> verify(addSprite, once()).accept(output));
        });
    }
}
