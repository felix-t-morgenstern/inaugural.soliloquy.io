package inaugural.soliloquy.graphics.test.unit.bootstrap.tasks;

import inaugural.soliloquy.graphics.api.dto.ImageAssetSetAssetDefinitionDTO;
import inaugural.soliloquy.graphics.api.dto.ImageAssetSetDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.ImageAssetSetPreloaderTask;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetPreloaderTaskTests {
    private final FakeImageAssetSetFactory FACTORY = new FakeImageAssetSetFactory();
    private final Collection<ImageAssetSetDefinitionDTO> IMAGE_ASSET_SET_DEFINITION_DTOS =
            new ArrayList<>();
    private final FakeRegistry<ImageAssetSet> IMAGE_ASSET_SET_REGISTRY = new FakeRegistry<>();
    private final Map<String, Map<String, ImageAssetSetAssetDefinitionDTO>>
            ASSETS = new HashMap<>();

    private ImageAssetSetPreloaderTask _imageAssetSetPreloaderTask;

    @BeforeEach
    void setUp() {
        String type1 = "type1";
        String type2 = "type2";
        String direction1 = "direction1";
        String direction2 = "direction2";

        String assetId1 = "assetId1";
        String assetId2 = "assetId2";
        String assetId3 = "assetId3";
        String assetId4 = "assetId4";

        ImageAssetSetAssetDefinitionDTO imageAssetSet1Asset1DTO =
                new ImageAssetSetAssetDefinitionDTO(type1, null, 1, assetId1);
        ImageAssetSetAssetDefinitionDTO imageAssetSet1Asset2DTO =
                new ImageAssetSetAssetDefinitionDTO(null, direction1, 2,
                        assetId2);
        ImageAssetSetAssetDefinitionDTO imageAssetSet2Asset1DTO =
                new ImageAssetSetAssetDefinitionDTO(type2, null, 2, assetId3);
        ImageAssetSetAssetDefinitionDTO imageAssetSet2Asset2DTO =
                new ImageAssetSetAssetDefinitionDTO(null, direction2, 1,
                        assetId4);

        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet1Asset1DTO);
        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet1Asset2DTO);
        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet2Asset1DTO);
        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet2Asset2DTO);

        ImageAssetSetDefinitionDTO imageAssetSet1DTO = new ImageAssetSetDefinitionDTO(
                "imageAssetSet1",
                new ImageAssetSetAssetDefinitionDTO[]{
                        imageAssetSet1Asset1DTO, imageAssetSet1Asset2DTO
                });

        ImageAssetSetDefinitionDTO imageAssetSet2DTO = new ImageAssetSetDefinitionDTO(
                "imageAssetSet2",
                new ImageAssetSetAssetDefinitionDTO[]{
                        imageAssetSet2Asset1DTO, imageAssetSet2Asset2DTO
                });

        IMAGE_ASSET_SET_DEFINITION_DTOS.add(imageAssetSet1DTO);
        IMAGE_ASSET_SET_DEFINITION_DTOS.add(imageAssetSet2DTO);

        _imageAssetSetPreloaderTask = new ImageAssetSetPreloaderTask(
                IMAGE_ASSET_SET_DEFINITION_DTOS, FACTORY, IMAGE_ASSET_SET_REGISTRY::add);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(IMAGE_ASSET_SET_DEFINITION_DTOS, null,
                        IMAGE_ASSET_SET_REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(null, FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(new ArrayList<>(), FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{ add(null); }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    null,
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                            new ImageAssetSetAssetDefinitionDTO(
                                                    "type", "direction", 1, "assetId")
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "",
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                            new ImageAssetSetAssetDefinitionDTO(
                                                    "type", "direction", 1, "assetId")
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "imageAssetSet1",
                                    null));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "imageAssetSet1",
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "imageAssetSet1",
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                            new ImageAssetSetAssetDefinitionDTO(
                                                    "type", "direction", 0, "assetId")
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "imageAssetSet1",
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                            new ImageAssetSetAssetDefinitionDTO(
                                                    "type", "direction", 4, "assetId")
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "imageAssetSet1",
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                            new ImageAssetSetAssetDefinitionDTO(
                                                    "type", "direction", 1, null)
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        new ArrayList<ImageAssetSetDefinitionDTO>() {{
                            add(new ImageAssetSetDefinitionDTO(
                                    "imageAssetSet1",
                                    new ImageAssetSetAssetDefinitionDTO[]{
                                            new ImageAssetSetAssetDefinitionDTO(
                                                    "type", "direction", 1, "")
                                    }));
                        }},
                        FACTORY,
                        IMAGE_ASSET_SET_REGISTRY::add));

        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(IMAGE_ASSET_SET_DEFINITION_DTOS, FACTORY, null));
    }

    @Test
    void testRun() {
        _imageAssetSetPreloaderTask.run();

        assertEquals(IMAGE_ASSET_SET_DEFINITION_DTOS.size(), IMAGE_ASSET_SET_REGISTRY.size());
        IMAGE_ASSET_SET_DEFINITION_DTOS.forEach(dto -> {
            ImageAssetSetDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(dto.assets.length, createdDefinition.assetDefinitions().size());
            createdDefinition.assetDefinitions().forEach(assetDefinition -> {
                ImageAssetSetAssetDefinitionDTO assetDTO =
                        ASSETS.get(assetDefinition.type()).get(assetDefinition.direction());
                assertEquals(ImageAsset.ImageAssetType.getFromValue(assetDTO.assetType),
                        assetDefinition.assetType());
                assertEquals(assetDTO.assetId, assetDefinition.assetId());
            });

            ImageAssetSet imageAssetSet =
                    IMAGE_ASSET_SET_REGISTRY.get(createdDefinition.id());
            assertTrue(FACTORY.OUTPUTS.contains(imageAssetSet));
        });
    }

    private void addImageAssetSetAssetDTOToMap(Map<String,Map<String, 
            ImageAssetSetAssetDefinitionDTO>> map, 
                                               ImageAssetSetAssetDefinitionDTO 
                                                       imageAssetSetAssetDefinitionDTO) {
        if (!map.containsKey(imageAssetSetAssetDefinitionDTO.type)) {
            map.put(imageAssetSetAssetDefinitionDTO.type, new HashMap<>());
        }
        map.get(imageAssetSetAssetDefinitionDTO.type)
                .put(imageAssetSetAssetDefinitionDTO.direction, imageAssetSetAssetDefinitionDTO);
    }
}
