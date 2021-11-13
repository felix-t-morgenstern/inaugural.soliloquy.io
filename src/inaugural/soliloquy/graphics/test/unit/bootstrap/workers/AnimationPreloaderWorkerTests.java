package inaugural.soliloquy.graphics.test.unit.bootstrap.workers;

import inaugural.soliloquy.graphics.api.dto.AnimationDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.workers.AnimationPreloaderWorker;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AnimationPreloaderWorkerTests {
    private final java.util.Map<String, Image> IMAGES = new HashMap<>();
    private final FakeAnimationFactory FACTORY = new FakeAnimationFactory();
    private final Collection<AnimationDefinitionDTO> ANIMATION_DEFINITION_DTOS = new ArrayList<>();
    private final FakeRegistry<Animation> REGISTRY = new FakeRegistry<Animation>();

    private AnimationPreloaderWorker _animationPreloaderWorker;

    @BeforeEach
    void setUp() {
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";
        String relativeLocation3 = "relativeLocation3";
        String relativeLocation4 = "relativeLocation4";

        IMAGES.put(relativeLocation1, new FakeImage(relativeLocation1));
        IMAGES.put(relativeLocation2, new FakeImage(relativeLocation2));
        IMAGES.put(relativeLocation3, new FakeImage(relativeLocation3));
        IMAGES.put(relativeLocation4, new FakeImage(relativeLocation4));

        AnimationDefinitionDTO.AnimationFrameDTO animation1Frame1 =
                new AnimationDefinitionDTO.AnimationFrameDTO(relativeLocation1, 0,
                        11, 22, 33, 44, 0.1f, 0.2f);
        AnimationDefinitionDTO.AnimationFrameDTO animation1Frame2 =
                new AnimationDefinitionDTO.AnimationFrameDTO(relativeLocation2, 222,
                        55, 66, 77, 88, 0.3f, 0.4f);
        AnimationDefinitionDTO.AnimationFrameDTO animation2Frame1 =
                new AnimationDefinitionDTO.AnimationFrameDTO(relativeLocation3, 0,
                        12, 34, 56, 78, 0.5f, 0.6f);
        AnimationDefinitionDTO.AnimationFrameDTO animation2Frame2 =
                new AnimationDefinitionDTO.AnimationFrameDTO(relativeLocation4, 444,
                        87, 65, 43, 21, 0.7f, 0.8f);

        AnimationDefinitionDTO animation1DTO = new AnimationDefinitionDTO("animation1", 555,
                new AnimationDefinitionDTO.AnimationFrameDTO[] {
                        animation1Frame1, animation1Frame2
                });
        AnimationDefinitionDTO animation2DTO = new AnimationDefinitionDTO("animation2", 666,
                new AnimationDefinitionDTO.AnimationFrameDTO[] {
                        animation2Frame1, animation2Frame2
                });

        ANIMATION_DEFINITION_DTOS.add(animation1DTO);
        ANIMATION_DEFINITION_DTOS.add(animation2DTO);

        _animationPreloaderWorker = new AnimationPreloaderWorker(IMAGES::get, FACTORY, 
                ANIMATION_DEFINITION_DTOS, REGISTRY::add);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(null, FACTORY, ANIMATION_DEFINITION_DTOS, 
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, null, ANIMATION_DEFINITION_DTOS,
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY, null,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY, new ArrayList<>(),
                        REGISTRY::add));

        String animationId = "animationId";
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";
        AnimationDefinitionDTO.AnimationFrameDTO animation1Frame1 =
                new AnimationDefinitionDTO.AnimationFrameDTO(relativeLocation1, 0,
                        11, 22, 33, 44, 0.1f, 0.2f);
        AnimationDefinitionDTO.AnimationFrameDTO animation1Frame2 =
                new AnimationDefinitionDTO.AnimationFrameDTO(relativeLocation2, 222,
                        55, 66, 77, 88, 0.3f, 0.4f);

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(null);
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(null, 123,
                                    new AnimationDefinitionDTO.AnimationFrameDTO[]{
                                            animation1Frame1
                                    }));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO("", 123,
                                    new AnimationDefinitionDTO.AnimationFrameDTO[]{
                                            animation1Frame1
                                    }));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 0,
                                    new AnimationDefinitionDTO.AnimationFrameDTO[]{
                                            animation1Frame1
                                    }));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, animation1Frame2.ms - 1,
                                    new AnimationDefinitionDTO.AnimationFrameDTO[]{
                                            animation1Frame1,
                                            animation1Frame2
                                    }));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 123,
                                    null));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 123,
                                    new AnimationDefinitionDTO.AnimationFrameDTO[]{
                                    }));
                        }},
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 123,
                                    new AnimationDefinitionDTO.AnimationFrameDTO[]{
                                            new AnimationDefinitionDTO.AnimationFrameDTO(
                                                    relativeLocation1, 1,
                                                    11, 22, 33, 44, 0.1f, 0.2f)
                                    }));
                        }},
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderWorker(IMAGES::get, FACTORY, ANIMATION_DEFINITION_DTOS, 
                        null));
    }

    @Test
    void testRun() {
        _animationPreloaderWorker.run();

        assertEquals(ANIMATION_DEFINITION_DTOS.size(), REGISTRY.size());
        ANIMATION_DEFINITION_DTOS.forEach(dto -> {
            AnimationDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(AnimationDefinition.class.getCanonicalName(),
                    createdDefinition.getInterfaceName());
            assertEquals(dto.frames.length, createdDefinition.frameSnippetDefinitions().size());
            ANIMATION_DEFINITION_DTOS.forEach(animationDefinitionDTO -> {
                AnimationDefinition inputDefinition =
                        FACTORY.INPUTS.get(animationDefinitionDTO.id);
                assertNotNull(inputDefinition);
                assertEquals(animationDefinitionDTO.msDur, inputDefinition.msDuration());
                assertEquals(animationDefinitionDTO.frames.length,
                        inputDefinition.frameSnippetDefinitions().size());
                for(AnimationDefinitionDTO.AnimationFrameDTO frameDTO :
                        animationDefinitionDTO.frames) {
                    AnimationFrameSnippet createdSnippet =
                            inputDefinition.frameSnippetDefinitions().get(frameDTO.ms);
                    assertNotNull(createdSnippet);
                    assertSame(IMAGES.get(frameDTO.imgLoc), createdSnippet.image());
                    assertEquals(frameDTO.leftX, createdSnippet.leftX());
                    assertEquals(frameDTO.topY, createdSnippet.topY());
                    assertEquals(frameDTO.rightX, createdSnippet.rightX());
                    assertEquals(frameDTO.bottomY, createdSnippet.bottomY());
                    assertEquals(frameDTO.offsetX, createdSnippet.offsetX());
                    assertEquals(frameDTO.offsetY, createdSnippet.offsetY());
                }
            });

            Animation animation = REGISTRY.get(createdDefinition.id());
            assertTrue(FACTORY.OUTPUTS.contains(animation));
        });
    }
}
