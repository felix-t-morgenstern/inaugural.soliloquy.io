package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FakeImageFactory implements ImageFactory {
    public List<String> RelativeLocations = listOf();
    public List<Boolean> SupportsEventCapturingValues = listOf();

    public List<FakeImage> Outputs = listOf();

    @Override
    public Image make(ImageDefinition imageDefinition)
            throws IllegalArgumentException {
        RelativeLocations.add(imageDefinition.relativeLocation());
        SupportsEventCapturingValues.add(imageDefinition.supportsMouseEventCapturing());
        var output = new FakeImage(imageDefinition.relativeLocation());
        Outputs.add(output);
        return output;
    }
}
