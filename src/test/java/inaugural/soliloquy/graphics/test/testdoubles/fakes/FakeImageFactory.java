package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.ImageDefinition;

import java.util.ArrayList;
import java.util.List;

public class FakeImageFactory implements ImageFactory {
    public List<String> RelativeLocations = new ArrayList<>();
    public List<Boolean> SupportsEventCapturingValues = new ArrayList<>();

    public List<FakeImage> Outputs = new ArrayList<>();

    @Override
    public Image make(ImageDefinition imageDefinition)
            throws IllegalArgumentException {
        RelativeLocations.add(imageDefinition.relativeLocation());
        SupportsEventCapturingValues.add(imageDefinition.supportsMouseEventCapturing());
        FakeImage output = new FakeImage(imageDefinition.relativeLocation());
        Outputs.add(output);
        return output;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
