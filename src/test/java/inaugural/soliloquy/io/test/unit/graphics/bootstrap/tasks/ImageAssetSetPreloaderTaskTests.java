package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.ImageAssetSetAssetDefinitionDTO;
import inaugural.soliloquy.io.api.dto.ImageAssetSetDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.ImageAssetSetPreloaderTask;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeImageAssetSetFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.io.graphics.assets.ImageAsset;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.shared.Direction.SOUTH;
import static soliloquy.specs.common.shared.Direction.SOUTHWEST;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetPreloaderTaskTests {
    private final FakeImageAssetSetFactory FACTORY = new FakeImageAssetSetFactory();
    private final Collection<ImageAssetSetDefinitionDTO> IMAGE_ASSET_SET_DEFINITION_DTOS =
            listOf();
    private final Map<String, Map<Direction, ImageAssetSetAssetDefinitionDTO>> ASSETS =
            mapOf();

    @Mock private Consumer<ImageAssetSet> addImageAsset;

    private ImageAssetSetPreloaderTask imageAssetSetPreloaderTask;

    @BeforeEach
    public void setUp() {
        var type1 = randomString();
        var type2 = randomString();

        var assetId1 = randomString();
        var assetId2 = randomString();
        var assetId3 = randomString();
        var assetId4 = randomString();

        var imageAssetSet1Asset1DTO = new ImageAssetSetAssetDefinitionDTO(type1, null, 1, assetId1);
        var imageAssetSet1Asset2DTO =
                new ImageAssetSetAssetDefinitionDTO(null, SOUTHWEST.getValue(), 2, assetId2);
        var imageAssetSet2Asset1DTO = new ImageAssetSetAssetDefinitionDTO(type2, null, 2, assetId3);
        var imageAssetSet2Asset2DTO =
                new ImageAssetSetAssetDefinitionDTO(null, SOUTH.getValue(), 1, assetId4);

        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet1Asset1DTO);
        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet1Asset2DTO);
        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet2Asset1DTO);
        addImageAssetSetAssetDTOToMap(ASSETS, imageAssetSet2Asset2DTO);

        var imageAssetSet1DTO = new ImageAssetSetDefinitionDTO(
                randomString(),
                new ImageAssetSetAssetDefinitionDTO[]{
                        imageAssetSet1Asset1DTO, imageAssetSet1Asset2DTO
                });

        var imageAssetSet2DTO = new ImageAssetSetDefinitionDTO(
                randomString(),
                new ImageAssetSetAssetDefinitionDTO[]{
                        imageAssetSet2Asset1DTO, imageAssetSet2Asset2DTO
                });

        IMAGE_ASSET_SET_DEFINITION_DTOS.add(imageAssetSet1DTO);
        IMAGE_ASSET_SET_DEFINITION_DTOS.add(imageAssetSet2DTO);

        imageAssetSetPreloaderTask = new ImageAssetSetPreloaderTask(
                IMAGE_ASSET_SET_DEFINITION_DTOS, FACTORY, addImageAsset);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(IMAGE_ASSET_SET_DEFINITION_DTOS, null,
                        addImageAsset));

        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(null, FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(listOf(), FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf((ImageAssetSetDefinitionDTO) null),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(null,
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(randomString(),
                                                SOUTHWEST.getValue(), 1, "assetId")})),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO("",
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(randomString(),
                                                SOUTHWEST.getValue(), 1, "assetId")})),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(), null)),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{})),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(randomString(),
                                                SOUTHWEST.getValue(), 0, "assetId")})),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(randomString(),
                                                SOUTHWEST.getValue(), 4, randomString())})),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(randomString(),
                                                SOUTHWEST.getValue(), 1, null)})),
                        FACTORY,
                        addImageAsset));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(randomString(),
                                                SOUTHWEST.getValue(), 1, "")})),
                        FACTORY,
                        addImageAsset));

        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(IMAGE_ASSET_SET_DEFINITION_DTOS, FACTORY, null));
    }

    @Test
    public void testRun() {
        imageAssetSetPreloaderTask.run();

        verify(addImageAsset, times(IMAGE_ASSET_SET_DEFINITION_DTOS.size())).accept(any());
        IMAGE_ASSET_SET_DEFINITION_DTOS.forEach(dto -> {
            var createdDefinition = FACTORY.INPUTS.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(dto.assets.length, createdDefinition.assetDefinitions().size());
            createdDefinition.assetDefinitions().forEach(assetDefinition -> {
                var assetDTO = ASSETS.get(assetDefinition.type())
                        .get(Direction.fromValue(assetDefinition.direction()));
                assertEquals(ImageAsset.ImageAssetType.getFromValue(assetDTO.assetType),
                        assetDefinition.assetType());
                assertEquals(assetDTO.assetId, assetDefinition.assetId());
            });

            FACTORY.OUTPUTS.forEach(output -> verify(addImageAsset, once()).accept(output));
        });
    }

    private void addImageAssetSetAssetDTOToMap(
            Map<String, Map<Direction, ImageAssetSetAssetDefinitionDTO>> map,
            ImageAssetSetAssetDefinitionDTO
                    imageAssetSetAssetDefinitionDTO) {
        if (!map.containsKey(imageAssetSetAssetDefinitionDTO.type)) {
            map.put(imageAssetSetAssetDefinitionDTO.type, mapOf());
        }
        map.get(imageAssetSetAssetDefinitionDTO.type)
                .put(Direction.fromValue(imageAssetSetAssetDefinitionDTO.direction),
                        imageAssetSetAssetDefinitionDTO);
    }
}
