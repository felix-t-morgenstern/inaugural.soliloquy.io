package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageAssetSetDefinition;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.mockito.Mockito.*;

public class FakeImageAssetSetFactory
        implements AssetFactory<ImageAssetSetDefinition, ImageAssetSet> {
    public final Map<String, ImageAssetSetDefinition> INPUTS = mapOf();
    public final List<ImageAssetSet> OUTPUTS = listOf();

    @Override
    public ImageAssetSet make(ImageAssetSetDefinition imageAssetSetDefinition)
            throws IllegalArgumentException {
        INPUTS.put(imageAssetSetDefinition.id(), imageAssetSetDefinition);
        var createdImageAssetSet = mock(ImageAssetSet.class);
        lenient().when(createdImageAssetSet.id()).thenReturn(imageAssetSetDefinition.id());
        OUTPUTS.add(createdImageAssetSet);
        return createdImageAssetSet;
    }
}
