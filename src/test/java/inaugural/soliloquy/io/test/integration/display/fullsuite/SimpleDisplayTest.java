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
                () -> {
                    try {
                        System.out.println("Display test started");
                        Thread.sleep(4000);
                        System.out.println("Display test ended");
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                },
                null
        );
    }
}
