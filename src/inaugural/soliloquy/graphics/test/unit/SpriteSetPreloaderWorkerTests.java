package inaugural.soliloquy.graphics.test.unit;

import inaugural.soliloquy.graphics.api.dto.SpriteSetDTO;
import inaugural.soliloquy.graphics.bootstrap.SpriteSetPreloaderWorker;
import inaugural.soliloquy.graphics.test.fakes.FakeImage;
import inaugural.soliloquy.graphics.test.fakes.FakeRegistry;
import inaugural.soliloquy.graphics.test.fakes.FakeSpriteSetFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.SpriteSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.SpriteSetSnippetDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpriteSetPreloaderWorkerTests {
    private final java.util.Map<String, Image> IMAGES = new HashMap<>();
    private final FakeSpriteSetFactory FACTORY = new FakeSpriteSetFactory();
    private final Registry<SpriteSet> REGISTRY = new FakeRegistry<>();

    private SpriteSetPreloaderWorker _spriteSetPreloaderWorker;

    @BeforeEach
    void setUp() {
        _spriteSetPreloaderWorker = new SpriteSetPreloaderWorker(IMAGES, FACTORY, REGISTRY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSetPreloaderWorker(null, FACTORY, REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSetPreloaderWorker(IMAGES, null, REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteSetPreloaderWorker(IMAGES, FACTORY, null));
    }

    @Test
    void testLoad() {
        String relativeLocation1 = "relativeLocation1";
        String relativeLocation2 = "relativeLocation2";

        IMAGES.put(relativeLocation1, new FakeImage(relativeLocation1));
        IMAGES.put(relativeLocation2, new FakeImage(relativeLocation2));

        String type1 = "type1";
        String type2 = "type2";
        String direction1 = "direction1";
        String direction2 = "direction2";

        SpriteSetDTO.SpriteSetSnippetDTO spriteSet1Snippet1DTO =
                new SpriteSetDTO.SpriteSetSnippetDTO(relativeLocation1, type1, null,
                        11, 22, 33, 44);
        SpriteSetDTO.SpriteSetSnippetDTO spriteSet1Snippet2DTO =
                new SpriteSetDTO.SpriteSetSnippetDTO(relativeLocation1, null, direction1,
                        55, 66, 77, 88);
        SpriteSetDTO.SpriteSetSnippetDTO spriteSet2Snippet1DTO =
                new SpriteSetDTO.SpriteSetSnippetDTO(relativeLocation2, type2, null,
                        12, 34, 56,78);
        SpriteSetDTO.SpriteSetSnippetDTO spriteSet2Snippet2DTO =
                new SpriteSetDTO.SpriteSetSnippetDTO(relativeLocation2, null, direction2,
                        87, 65, 43, 21);

        Map<String, Map<String, SpriteSetDTO.SpriteSetSnippetDTO>> snippets = new HashMap<>();

        addSpriteSetSnippetDTOToMap(snippets, spriteSet1Snippet1DTO);
        addSpriteSetSnippetDTOToMap(snippets, spriteSet1Snippet2DTO);
        addSpriteSetSnippetDTOToMap(snippets, spriteSet2Snippet1DTO);
        addSpriteSetSnippetDTOToMap(snippets, spriteSet2Snippet2DTO);

        SpriteSetDTO spriteSet1DTO = new SpriteSetDTO("spriteSet1",
                new SpriteSetDTO.SpriteSetSnippetDTO[]{
                        spriteSet1Snippet1DTO, spriteSet1Snippet2DTO
        });

        SpriteSetDTO spriteSet2DTO = new SpriteSetDTO("spriteSet2",
                new SpriteSetDTO.SpriteSetSnippetDTO[]{
                        spriteSet2Snippet1DTO, spriteSet2Snippet2DTO
                });

        List<SpriteSetDTO> spriteSetDTOs = new ArrayList<SpriteSetDTO>() {{
            add(spriteSet1DTO);
            add(spriteSet2DTO);
        }};

        _spriteSetPreloaderWorker.load(spriteSetDTOs);

        assertEquals(spriteSetDTOs.size(), REGISTRY.size());
        spriteSetDTOs.forEach(dto -> {
            SpriteSetDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(SpriteSetDefinition.class.getCanonicalName(),
                    createdDefinition.getInterfaceName());
            assertEquals(dto.snippets.length, createdDefinition.snippetDefinitions().size());
            createdDefinition.snippetDefinitions().forEach(snippetDefinition -> {
                assertEquals(SpriteSetSnippetDefinition.class.getCanonicalName(),
                        snippetDefinition.getInterfaceName());
                SpriteSetDTO.SpriteSetSnippetDTO snippetDTO =
                        snippets.get(snippetDefinition.type()).get(snippetDefinition.direction());
                assertSame(IMAGES.get(snippetDTO.imgLoc), snippetDefinition.image());
                assertEquals(snippetDTO.leftX, snippetDefinition.leftX());
                assertEquals(snippetDTO.topY, snippetDefinition.topY());
                assertEquals(snippetDTO.rightX, snippetDefinition.rightX());
                assertEquals(snippetDTO.bottomY, snippetDefinition.bottomY());
            });

            SpriteSet spriteSet = REGISTRY.get(createdDefinition.assetId());
            assertTrue(FACTORY.OUTPUTS.contains(spriteSet));
        });
    }

    private void addSpriteSetSnippetDTOToMap(Map<String,Map<String,
            SpriteSetDTO.SpriteSetSnippetDTO>> map,
                                             SpriteSetDTO.SpriteSetSnippetDTO spriteSetSnippetDTO) {
        if (!map.containsKey(spriteSetSnippetDTO.type)) {
            map.put(spriteSetSnippetDTO.type, new HashMap<>());
        }
        map.get(spriteSetSnippetDTO.type).put(spriteSetSnippetDTO.direction, spriteSetSnippetDTO);
    }

    @Test
    void testLoadWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteSetPreloaderWorker.load(null));
    }
}
