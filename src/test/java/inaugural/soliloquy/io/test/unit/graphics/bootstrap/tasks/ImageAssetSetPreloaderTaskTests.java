package inaugural.soliloquy.io.test.unit.graphics.bootstrap.tasks;

import inaugural.soliloquy.io.api.dto.ImageAssetSetAssetDefinitionDTO;
import inaugural.soliloquy.io.api.dto.ImageAssetSetDefinitionDTO;
import inaugural.soliloquy.io.bootstrap.assetfactories.ImageAssetSetFactory;
import inaugural.soliloquy.io.bootstrap.tasks.ImageAssetSetPreloaderTask;
import inaugural.soliloquy.tools.collections.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.io.api.dto.ImageAssetSetAssetDefinitionDTO.DisplayParamDefinitionDTO;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static soliloquy.specs.io.graphics.assets.ImageAsset.ImageAssetType;
import static soliloquy.specs.io.bootstrap.assetfactories.definitions.ImageAssetSetAssetDefinition.DisplayParam;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetPreloaderTaskTests {
    private final Collection<ImageAssetSetDefinitionDTO> IMAGE_ASSET_SET_DEFINITION_DTOS =
            listOf();
    private final Map<Map<String, String>, ImageAssetSetAssetDefinitionDTO> ASSETS = mapOf();

    @Mock private ImageAssetSetFactory mockFactory;
    @Mock private Consumer<ImageAssetSet> mockAcceptOutput;

    private ImageAssetSetPreloaderTask imageAssetSetPreloaderTask;

    @BeforeEach
    public void setUp() {
        var stanceParamName = randomString();
        var stance1 = randomString();
        var stance2 = randomString();

        var directionParamName = randomString();
        var direction1 = randomString();
        var direction2 = randomString();

        var assetId1 = randomString();
        var assetId2 = randomString();
        var assetId3 = randomString();
        var assetId4 = randomString();

        var imageAssetSet1Asset1DTO = new ImageAssetSetAssetDefinitionDTO(1, assetId1,
                new DisplayParamDefinitionDTO(stanceParamName, stance1));
        var imageAssetSet1Asset2DTO =
                new ImageAssetSetAssetDefinitionDTO(2, assetId2,
                        new DisplayParamDefinitionDTO(directionParamName, direction1));
        var imageAssetSet2Asset1DTO = new ImageAssetSetAssetDefinitionDTO(2, assetId3,
                new DisplayParamDefinitionDTO(stanceParamName, stance2));
        var imageAssetSet2Asset2DTO =
                new ImageAssetSetAssetDefinitionDTO(1, assetId4,
                        new DisplayParamDefinitionDTO(directionParamName, direction2));

        ASSETS.put(mapOfDisplayParams(imageAssetSet1Asset1DTO.displayParams), imageAssetSet1Asset1DTO);
        ASSETS.put(mapOfDisplayParams(imageAssetSet1Asset2DTO.displayParams), imageAssetSet1Asset2DTO);
        ASSETS.put(mapOfDisplayParams(imageAssetSet2Asset1DTO.displayParams), imageAssetSet2Asset1DTO);
        ASSETS.put(mapOfDisplayParams(imageAssetSet2Asset2DTO.displayParams), imageAssetSet2Asset2DTO);

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
                IMAGE_ASSET_SET_DEFINITION_DTOS, mockFactory, mockAcceptOutput);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(IMAGE_ASSET_SET_DEFINITION_DTOS, null,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(null, mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(listOf(), mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf((ImageAssetSetDefinitionDTO) null),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(null,
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(1, "assetId")})),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO("",
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(1, "assetId")})),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(), null)),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{})),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(0, "assetId")})),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(4, randomString())})),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(1, null)})),
                        mockFactory,
                        mockAcceptOutput));
        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(
                        listOf(new ImageAssetSetDefinitionDTO(randomString(),
                                new ImageAssetSetAssetDefinitionDTO[]{
                                        new ImageAssetSetAssetDefinitionDTO(1, "")})),
                        mockFactory,
                        mockAcceptOutput));

        assertThrows(IllegalArgumentException.class, () ->
                new ImageAssetSetPreloaderTask(IMAGE_ASSET_SET_DEFINITION_DTOS, mockFactory, null));
    }

    @Test
    public void testRun() {
        var factoryInputs = Collections.<String, ImageAssetSetDefinition>mapOf();
        var factoryOutputs = Collections.<ImageAssetSet>listOf();
        when(mockFactory.apply(any())).thenAnswer(i -> {
            ImageAssetSetDefinition arg = i.getArgument(0);
            factoryInputs.put(arg.id(), arg);
            var mockImageAssetSet = mock(ImageAssetSet.class);
            factoryOutputs.add(mockImageAssetSet);
            return mockImageAssetSet;
        });

        imageAssetSetPreloaderTask.run();

        verify(mockAcceptOutput, times(IMAGE_ASSET_SET_DEFINITION_DTOS.size())).accept(any());
        IMAGE_ASSET_SET_DEFINITION_DTOS.forEach(dto -> {
            var createdDefinition = factoryInputs.get(dto.id);
            assertNotNull(createdDefinition);
            assertEquals(dto.assets.length, createdDefinition.assetDefinitions().size());
            createdDefinition.assetDefinitions().forEach(assetDefinition -> {
                var assetDTO = ASSETS.get(mapOfDisplayParams(assetDefinition.DISPLAY_PARAMS));
                assertEquals(ImageAssetType.getFromValue(assetDTO.assetType), assetDefinition.ASSET_TYPE);
                assertEquals(assetDTO.assetId, assetDefinition.ASSET_ID);
            });
        });

        verify(mockFactory, times(IMAGE_ASSET_SET_DEFINITION_DTOS.size())).apply(any());
        verify(mockAcceptOutput, times(IMAGE_ASSET_SET_DEFINITION_DTOS.size())).accept(any());
        factoryOutputs.forEach(output -> verify(mockAcceptOutput, once()).accept(output));
    }

    private Map<String, String> mapOfDisplayParams(DisplayParamDefinitionDTO[] displayParams) {
        var map = Collections.<String, String>mapOf();
        for (var displayParam : displayParams) {
            map.put(displayParam.name, displayParam.val);
        }
        return map;
    }

    private Map<String, String> mapOfDisplayParams(DisplayParam[] displayParams) {
        var map = Collections.<String, String>mapOf();
        for (var displayParam : displayParams) {
            map.put(displayParam.NAME, displayParam.VAL);
        }
        return map;
    }
}
