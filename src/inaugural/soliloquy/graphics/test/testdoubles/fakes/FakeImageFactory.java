package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.assetfactories.ImageFactory;

import java.util.ArrayList;
import java.util.List;

public class FakeImageFactory implements ImageFactory {
    public List<String> RelativeLocations = new ArrayList<>();
    public List<Boolean> SupportsEventCapturingValues = new ArrayList<>();

    public List<FakeImage> Outputs = new ArrayList<>();

    @Override
    public Image make(String relativeLocation, boolean supportsEventCapturing)
            throws IllegalArgumentException {
        RelativeLocations.add(relativeLocation);
        SupportsEventCapturingValues.add(supportsEventCapturing);
        FakeImage output = new FakeImage(relativeLocation);
        Outputs.add(output);
        return output;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
