package inaugural.soliloquy.io.test.integration.display.fullsuite;

import inaugural.soliloquy.io.api.dto.*;

public class SimpleDisplayTest {
    public static void main(String[] args) {
        var displayTest = new DisplayTest();
        displayTest.runTest(
                "Simple display test",
                new AssetDefinitionsDTO(
                        new ImageDefinitionDTO[]{},
                        new FontDefinitionDTO[]{},
                        new SpriteDefinitionDTO[]{},
                        new AnimationDefinitionDTO[]{},
                        new GlobalLoopingAnimationDefinitionDTO[]{},
                        new ImageAssetSetDefinitionDTO[]{},
                        new MouseCursorImageDefinitionDTO[]{},
                        new AnimatedMouseCursorDefinitionDTO[]{},
                        new StaticMouseCursorDefinitionDTO[]{}
                ),
                () -> DisplayTest.runThenClose("Simple", 4000),
                null
        );
    }
}
