package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.AnimationDefinitionDTO;
import inaugural.soliloquy.graphics.api.dto.AnimationFrameDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.AnimationPreloaderTask;
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

class AnimationPreloaderTaskTests {
    private final java.util.Map<String, Image> IMAGES = new HashMap<>();
    private final FakeAnimationFactory FACTORY = new FakeAnimationFactory();
    private final Collection<AnimationDefinitionDTO> ANIMATION_DEFINITION_DTOS = new ArrayList<>();
    private final FakeRegistry<Animation> REGISTRY = new FakeRegistry<>();

    private AnimationPreloaderTask _animationPreloaderTask;

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

        AnimationFrameDefinitionDTO animation1Frame1 =
                new AnimationFrameDefinitionDTO(relativeLocation1, 0,
                        11, 22, 33, 44, 0.1f, 0.2f);
        AnimationFrameDefinitionDTO animation1Frame2 =
                new AnimationFrameDefinitionDTO(relativeLocation2, 222,
                        55, 66, 77, 88, 0.3f, 0.4f);
        AnimationFrameDefinitionDTO animation2Frame1 =
                new AnimationFrameDefinitionDTO(relativeLocation3, 0,
                        12, 34, 56, 78, 0.5f, 0.6f);
        AnimationFrameDefinitionDTO animation2Frame2 =
                new AnimationFrameDefinitionDTO(relativeLocation4, 444,
                        87, 65, 43, 21, 0.7f, 0.8f);

        AnimationDefinitionDTO animation1DTO = new AnimationDefinitionDTO("animation1", 555,
                new AnimationFrameDefinitionDTO[] {
                        animation1Frame1, animation1Frame2
                });
        AnimationDefinitionDTO animation2DTO = new AnimationDefinitionDTO("animation2", 666,
                new AnimationFrameDefinitionDTO[] {
                        animation2Frame1, animation2Frame2
                });

        ANIMATION_DEFINITION_DTOS.add(animation1DTO);
        ANIMATION_DEFINITION_DTOS.add(animation2DTO);

        _animationPreloaderTask = new AnimationPreloaderTask(IMAGES::get,
                ANIMATION_DEFINITION_DTOS, FACTORY, REGISTRY::add);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(null, ANIMATION_DEFINITION_DTOS, FACTORY,
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, ANIMATION_DEFINITION_DTOS, null,
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, null, FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, new ArrayList<>(), FACTORY,
                        REGISTRY::add));

        String animationId = "animationId";
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";
        AnimationFrameDefinitionDTO animation1Frame1 =
                new AnimationFrameDefinitionDTO(relativeLocation1, 0,
                        11, 22, 33, 44, 0.1f, 0.2f);
        AnimationFrameDefinitionDTO animation1Frame2 =
                new AnimationFrameDefinitionDTO(relativeLocation2, 222,
                        55, 66, 77, 88, 0.3f, 0.4f);

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(null);
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(null, 123,
                                    new AnimationFrameDefinitionDTO[]{
                                            animation1Frame1
                                    }));
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO("", 123,
                                    new AnimationFrameDefinitionDTO[]{
                                            animation1Frame1
                                    }));
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 0,
                                    new AnimationFrameDefinitionDTO[]{
                                            animation1Frame1
                                    }));
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, animation1Frame2.ms - 1,
                                    new AnimationFrameDefinitionDTO[]{
                                            animation1Frame1,
                                            animation1Frame2
                                    }));
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 123,
                                    null));
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 123,
                                    new AnimationFrameDefinitionDTO[]{
                                    }));
                        }},
                        FACTORY,
                        REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        new ArrayList<AnimationDefinitionDTO>() {{
                            add(new AnimationDefinitionDTO(animationId, 123,
                                    new AnimationFrameDefinitionDTO[]{
                                            new AnimationFrameDefinitionDTO(
                                                    relativeLocation1, 1,
                                                    11, 22, 33, 44, 0.1f, 0.2f)
                                    }));
                        }},
                        FACTORY,
                        REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, ANIMATION_DEFINITION_DTOS, FACTORY,null));
    }

    @Test
    void testRun() {
        _animationPreloaderTask.run();

        assertEquals(ANIMATION_DEFINITION_DTOS.size(), REGISTRY.size());
        ANIMATION_DEFINITION_DTOS.forEach(dto -> {
            AnimationDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(dto.frames.length, createdDefinition.frameSnippetDefinitions().size());
            ANIMATION_DEFINITION_DTOS.forEach(animationDefinitionDTO -> {
                AnimationDefinition inputDefinition =
                        FACTORY.INPUTS.get(animationDefinitionDTO.id);
                assertNotNull(inputDefinition);
                assertEquals(animationDefinitionDTO.msDur, inputDefinition.msDuration());
                assertEquals(animationDefinitionDTO.frames.length,
                        inputDefinition.frameSnippetDefinitions().size());
                for(AnimationFrameDefinitionDTO frameDTO :
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
