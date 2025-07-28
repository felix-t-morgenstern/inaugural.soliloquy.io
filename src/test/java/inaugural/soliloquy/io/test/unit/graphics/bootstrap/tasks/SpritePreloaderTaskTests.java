package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.SpriteDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.SpritePreloaderTask;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImage;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.SpriteDefinition;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpritePreloaderTaskTests {
    private final Map<String, Image> IMAGES = mapOf();
    private final List<SpriteDefinitionDTO> SPRITE_DEFINITION_DTOS = listOf();

    @Mock private Function<SpriteDefinition, Sprite> mockFactory;
    @Mock private Consumer<Sprite> mockAcceptOutput;

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
                mockFactory, mockAcceptOutput);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(null, SPRITE_DEFINITION_DTOS, mockFactory, mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, SPRITE_DEFINITION_DTOS, null,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, null, mockFactory, mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, listOf(), mockFactory, mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf((SpriteDefinitionDTO) null),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO(null, "relativeLocation1",
                                    12, 34, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("", "relativeLocation1",
                                    12, 34, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", null,
                                    12, 34, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "",
                                    12, 34, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    -1, 34, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    12, -1, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    56, 34, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get,
                        listOf(
                            new SpriteDefinitionDTO("sprite1Id", "relativeLocation1",
                                    12, 78, 56, 78)
                        ),
                        mockFactory,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new SpritePreloaderTask(IMAGES::get, SPRITE_DEFINITION_DTOS, mockFactory, null));
    }

    @Test
    public void testRun() {
        var factoryInputs = Collections.<String, SpriteDefinition>mapOf();
        var factoryOutputs = Collections.<Sprite>listOf();
        when(mockFactory.apply(any())).thenAnswer(i -> {
            SpriteDefinition arg = i.getArgument(0);
            factoryInputs.put(arg.id(), arg);
            var mockSprite = mock(Sprite.class);
            factoryOutputs.add(mockSprite);
            return mockSprite;
        });

        spritePreloaderTask.run();

        verify(mockAcceptOutput, times(SPRITE_DEFINITION_DTOS.size())).accept(any());
        SPRITE_DEFINITION_DTOS.forEach(dto -> {
            var createdDefinition = factoryInputs.get(dto.id);
            assertNotNull(createdDefinition);
            assertSame(IMAGES.get(dto.imgLoc), createdDefinition.image());
            assertEquals(dto.leftX, createdDefinition.leftX());
            assertEquals(dto.topY, createdDefinition.topY());
            assertEquals(dto.rightX, createdDefinition.rightX());
            assertEquals(dto.bottomY, createdDefinition.bottomY());
        });

        verify(mockFactory, times(SPRITE_DEFINITION_DTOS.size())).apply(any());
        verify(mockAcceptOutput, times(SPRITE_DEFINITION_DTOS.size())).accept(any());
        factoryOutputs.forEach(output -> verify(mockAcceptOutput, once()).accept(output));
    }
}
