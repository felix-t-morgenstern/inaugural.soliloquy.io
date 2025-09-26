package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.AnimationDefinitionDTO;
import inaugural.soliloquy.io.api.dto.AnimationFrameDefinitionDTO;
import inaugural.soliloquy.io.bootstrap.tasks.AnimationPreloaderTask;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImage;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimationPreloaderTaskTests {
    private final java.util.Map<String, Image> IMAGES = mapOf();
    private final Collection<AnimationDefinitionDTO> ANIMATION_DEFINITION_DTOS = listOf();

    @Mock private Function<AnimationDefinition, Animation> mockFactory;
    @Mock private Consumer<Animation> mockAcceptOutput;

    private AnimationPreloaderTask animationPreloaderTask;

    @BeforeEach
    public void setUp() {
        var relativeLocation1 = "relativeLocation1";
        var relativeLocation2 = "relativeLocation2";
        var relativeLocation3 = "relativeLocation3";
        var relativeLocation4 = "relativeLocation4";

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
                new AnimationFrameDefinitionDTO[]{
                        animation1Frame1, animation1Frame2
                });
        AnimationDefinitionDTO animation2DTO = new AnimationDefinitionDTO("animation2", 666,
                new AnimationFrameDefinitionDTO[]{
                        animation2Frame1, animation2Frame2
                });

        ANIMATION_DEFINITION_DTOS.add(animation1DTO);
        ANIMATION_DEFINITION_DTOS.add(animation2DTO);

        animationPreloaderTask = new AnimationPreloaderTask(IMAGES::get,
                ANIMATION_DEFINITION_DTOS, mockFactory, mockAcceptOutput);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(null, ANIMATION_DEFINITION_DTOS, mockFactory,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, ANIMATION_DEFINITION_DTOS, null,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, null, mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, listOf(), mockFactory,
                        mockAcceptOutput));

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
                        listOf((AnimationDefinitionDTO) null),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO(null, 123,
                                new AnimationFrameDefinitionDTO[]{
                                        animation1Frame1
                                })),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO("", 123,
                                new AnimationFrameDefinitionDTO[]{
                                        animation1Frame1
                                })),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO(animationId, 0,
                                new AnimationFrameDefinitionDTO[]{
                                        animation1Frame1
                                })),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO(animationId, animation1Frame2.ms - 1,
                                new AnimationFrameDefinitionDTO[]{
                                        animation1Frame1,
                                        animation1Frame2
                                })),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO(animationId, 123,
                                null)),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO(animationId, 123,
                                new AnimationFrameDefinitionDTO[]{
                                })),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get,
                        listOf(new AnimationDefinitionDTO(animationId, 123,
                                new AnimationFrameDefinitionDTO[]{
                                        new AnimationFrameDefinitionDTO(
                                                relativeLocation1, 1,
                                                11, 22, 33, 44, 0.1f, 0.2f)
                                })),
                        mockFactory,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new AnimationPreloaderTask(IMAGES::get, ANIMATION_DEFINITION_DTOS, mockFactory, null));
    }

    @Test
    public void testRun() {
        var factoryInputs = Collections.<String, AnimationDefinition>mapOf();
        var factoryOutputs = Collections.<Animation>listOf();
        when(mockFactory.apply(any())).thenAnswer(i -> {
            AnimationDefinition arg = i.getArgument(0);
            factoryInputs.put(arg.id(), arg);
            var mockAnimation = mock(Animation.class);
            factoryOutputs.add(mockAnimation);
            return mockAnimation;
        });

        animationPreloaderTask.run();

        verify(mockAcceptOutput, times(ANIMATION_DEFINITION_DTOS.size())).accept(any());
        ANIMATION_DEFINITION_DTOS.forEach(dto -> {
            var createdDefinition = factoryInputs.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(dto.frames.length, createdDefinition.frameSnippetDefinitions().size());
            ANIMATION_DEFINITION_DTOS.forEach(animationDefinitionDTO -> {
                var inputDefinition = factoryInputs.get(animationDefinitionDTO.id);
                assertNotNull(inputDefinition);
                assertEquals(animationDefinitionDTO.msDur, inputDefinition.msDuration());
                assertEquals(animationDefinitionDTO.frames.length,
                        inputDefinition.frameSnippetDefinitions().size());
                for (AnimationFrameDefinitionDTO frameDTO :
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
        });

        verify(mockFactory, times(ANIMATION_DEFINITION_DTOS.size())).apply(any());
        verify(mockAcceptOutput, times(ANIMATION_DEFINITION_DTOS.size())).accept(any());
        factoryOutputs.forEach(output -> verify(mockAcceptOutput, once()).accept(output));
    }
}
