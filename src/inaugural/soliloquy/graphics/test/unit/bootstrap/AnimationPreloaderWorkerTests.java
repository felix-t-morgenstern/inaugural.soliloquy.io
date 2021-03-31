package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.api.dto.AnimationDTO;
import inaugural.soliloquy.graphics.bootstrap.AnimationPreloaderWorker;
import inaugural.soliloquy.graphics.test.fakes.FakeAnimationFactory;
import inaugural.soliloquy.graphics.test.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.fakes.FakeRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimationDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimationPreloaderWorkerTests {
    private final java.util.Map<String, Image> IMAGES = new HashMap<>();
    private final FakeAnimationFactory FACTORY = new FakeAnimationFactory();
    private final Registry<Animation> REGISTRY = new FakeRegistry<>();

    private AnimationPreloaderWorker _animationPreloaderWorker;

    @BeforeEach
    void setUp() {
        _animationPreloaderWorker = new AnimationPreloaderWorker(IMAGES, FACTORY, REGISTRY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new AnimationPreloaderWorker(null, FACTORY, REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimationPreloaderWorker(IMAGES, null, REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimationPreloaderWorker(IMAGES, FACTORY, null));
    }

    @Test
    void testRun() {
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";

        IMAGES.put(relativeLocation1, new FakeImage(relativeLocation1));
        IMAGES.put(relativeLocation2, new FakeImage(relativeLocation2));

        AnimationDTO.AnimationFrameDTO animation1Frame1 =
                new AnimationDTO.AnimationFrameDTO(relativeLocation1, 111,
                        11, 22, 33, 44, 0.1f, 0.2f);
        AnimationDTO.AnimationFrameDTO animation1Frame2 =
                new AnimationDTO.AnimationFrameDTO(relativeLocation1, 222,
                        55, 66, 77, 88, 0.3f, 0.4f);
        AnimationDTO.AnimationFrameDTO animation2Frame1 =
                new AnimationDTO.AnimationFrameDTO(relativeLocation2, 333,
                        12, 34, 56, 78, 0.5f, 0.6f);
        AnimationDTO.AnimationFrameDTO animation2Frame2 =
                new AnimationDTO.AnimationFrameDTO(relativeLocation2, 444,
                        87, 65, 43, 21, 0.7f, 0.8f);

        Map<Integer, AnimationDTO.AnimationFrameDTO> frameDTOs = new HashMap<>();

        frameDTOs.put(animation1Frame1.ms, animation1Frame1);
        frameDTOs.put(animation1Frame2.ms, animation1Frame2);
        frameDTOs.put(animation2Frame1.ms, animation2Frame1);
        frameDTOs.put(animation2Frame2.ms, animation2Frame2);

        AnimationDTO animation1DTO = new AnimationDTO("animation1", 555,
                new AnimationDTO.AnimationFrameDTO[] {
                        animation1Frame1, animation1Frame2
                });
        AnimationDTO animation2DTO = new AnimationDTO("animation2", 666,
                new AnimationDTO.AnimationFrameDTO[] {
                        animation2Frame1, animation2Frame2
                });

        List<AnimationDTO> animationDTOs = new ArrayList<AnimationDTO>(){{
            add(animation1DTO); add(animation2DTO);
        }};

        _animationPreloaderWorker.run(animationDTOs);

        assertEquals(animationDTOs.size(), REGISTRY.size());
        animationDTOs.forEach(dto -> {
            AnimationDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(AnimationDefinition.class.getCanonicalName(),
                    createdDefinition.getInterfaceName());
            assertEquals(dto.frames.length, createdDefinition.frameSnippetDefinitions().size());
            createdDefinition.frameSnippetDefinitions().forEach((ms, snippetDefinition) -> {
                assertEquals(AnimationFrameSnippet.class.getCanonicalName(),
                        snippetDefinition.getInterfaceName());
                AnimationDTO.AnimationFrameDTO snippetDTO = frameDTOs.get(ms);
                assertSame(IMAGES.get(snippetDTO.imgLoc), snippetDefinition.image());
                assertEquals(snippetDTO.leftX, snippetDefinition.leftX());
                assertEquals(snippetDTO.topY, snippetDefinition.topY());
                assertEquals(snippetDTO.rightX, snippetDefinition.rightX());
                assertEquals(snippetDTO.bottomY, snippetDefinition.bottomY());
                assertEquals(snippetDTO.offsetX, snippetDefinition.offsetX());
                assertEquals(snippetDTO.offsetY, snippetDefinition.offsetY());
            });

            Animation animation = REGISTRY.get(createdDefinition.id());
            assertTrue(FACTORY.OUTPUTS.contains(animation));
        });
    }

    @Test
    void testRunWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _animationPreloaderWorker.run(null));
    }
}
