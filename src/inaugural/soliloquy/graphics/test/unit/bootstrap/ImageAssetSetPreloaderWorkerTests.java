package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.api.dto.ImageAssetSetDTO;
import inaugural.soliloquy.graphics.bootstrap.ImageAssetSetPreloaderWorker;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Registry;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetPreloaderWorkerTests {
    private final FakeImageAssetSetFactory FACTORY = new FakeImageAssetSetFactory();
    private final Registry<ImageAssetSet> IMAGE_ASSET_SET_REGISTRY =
            new FakeRegistry<ImageAssetSet>();

    private ImageAssetSetPreloaderWorker _imageAssetSetPreloaderWorker;

    @BeforeEach
    void setUp() {
        _imageAssetSetPreloaderWorker =
                new ImageAssetSetPreloaderWorker(FACTORY, IMAGE_ASSET_SET_REGISTRY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetPreloaderWorker(null, IMAGE_ASSET_SET_REGISTRY));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetPreloaderWorker(FACTORY, null));
    }

    @Test
    void testRun() {
        String type1 = "type1";
        String type2 = "type2";
        String direction1 = "direction1";
        String direction2 = "direction2";

        String assetId1 = "assetId1";
        String assetId2 = "assetId2";
        String assetId3 = "assetId3";
        String assetId4 = "assetId4";

        ImageAssetSetDTO.ImageAssetSetAssetDTO imageAssetSet1Asset1DTO =
                new ImageAssetSetDTO.ImageAssetSetAssetDTO(type1, null, 1, assetId1);
        ImageAssetSetDTO.ImageAssetSetAssetDTO imageAssetSet1Asset2DTO =
                new ImageAssetSetDTO.ImageAssetSetAssetDTO(null, direction1, 2, assetId2);
        ImageAssetSetDTO.ImageAssetSetAssetDTO imageAssetSet2Asset1DTO =
                new ImageAssetSetDTO.ImageAssetSetAssetDTO(type2, null, 2, assetId3);
        ImageAssetSetDTO.ImageAssetSetAssetDTO imageAssetSet2Asset2DTO =
                new ImageAssetSetDTO.ImageAssetSetAssetDTO(null, direction2, 1, assetId4);

        Map<String, Map<String, ImageAssetSetDTO.ImageAssetSetAssetDTO>> assets = new HashMap<>();

        addImageAssetSetAssetDTOToMap(assets, imageAssetSet1Asset1DTO);
        addImageAssetSetAssetDTOToMap(assets, imageAssetSet1Asset2DTO);
        addImageAssetSetAssetDTOToMap(assets, imageAssetSet2Asset1DTO);
        addImageAssetSetAssetDTOToMap(assets, imageAssetSet2Asset2DTO);

        ImageAssetSetDTO imageAssetSet1DTO = new ImageAssetSetDTO("imageAssetSet1",
                new ImageAssetSetDTO.ImageAssetSetAssetDTO[]{
                        imageAssetSet1Asset1DTO, imageAssetSet1Asset2DTO
        });

        ImageAssetSetDTO imageAssetSet2DTO = new ImageAssetSetDTO("imageAssetSet2",
                new ImageAssetSetDTO.ImageAssetSetAssetDTO[]{
                        imageAssetSet2Asset1DTO, imageAssetSet2Asset2DTO
                });

        List<ImageAssetSetDTO> imageAssetSetDTOs = new ArrayList<ImageAssetSetDTO>() {{
            add(imageAssetSet1DTO);
            add(imageAssetSet2DTO);
        }};

        _imageAssetSetPreloaderWorker.run(imageAssetSetDTOs);

        assertEquals(imageAssetSetDTOs.size(), IMAGE_ASSET_SET_REGISTRY.size());
        imageAssetSetDTOs.forEach(dto -> {
            ImageAssetSetDefinition createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(ImageAssetSetDefinition.class.getCanonicalName(),
                    createdDefinition.getInterfaceName());
            assertEquals(dto.assets.length, createdDefinition.assetDefinitions().size());
            createdDefinition.assetDefinitions().forEach(assetDefinition -> {
                assertEquals(ImageAssetSetAssetDefinition.class.getCanonicalName(),
                        assetDefinition.getInterfaceName());
                ImageAssetSetDTO.ImageAssetSetAssetDTO assetDTO =
                        assets.get(assetDefinition.type()).get(assetDefinition.direction());
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
            ImageAssetSetDTO.ImageAssetSetAssetDTO>> map,
                                               ImageAssetSetDTO.ImageAssetSetAssetDTO
                                                       imageAssetSetAssetDTO) {
        if (!map.containsKey(imageAssetSetAssetDTO.type)) {
            map.put(imageAssetSetAssetDTO.type, new HashMap<>());
        }
        map.get(imageAssetSetAssetDTO.type)
                .put(imageAssetSetAssetDTO.direction, imageAssetSetAssetDTO);
    }

    // NB: This test case does not contain any more in-depth tests of the DTO fields, since the
    //     preloader's only job is to pass the DTO on to the factory
    @Test
    void testRunWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _imageAssetSetPreloaderWorker.run(null));
    }
}
