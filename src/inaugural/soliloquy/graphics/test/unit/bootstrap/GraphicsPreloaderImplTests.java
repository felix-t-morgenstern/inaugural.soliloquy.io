package inaugural.soliloquy.graphics.test.unit.bootstrap;

import inaugural.soliloquy.graphics.bootstrap.assetfactories.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.fail;

class GraphicsPreloaderImplTests {
    @Mock
    private ImageFactoryImpl _mockImageFactory;
    @Mock
    private FontFactory _mockFontFactory;
    @Mock
    private SpriteFactory _mockSpriteFactory;
    @Mock
    private ImageAssetSetFactory _mockImageAssetSetFactory;
    @Mock
    private AnimationFactory _mockAnimationFactory;

    @Test
    void testAllAssetsLoaded() {
        fail("Implement this");
    }

    @Test
    void testImagesLoadedBeforeImageAssets() {
        fail("Implement this");
    }

    @Test
    void testPercentComplete() {
        fail("Implement this");
    }

    @Test
    void testExceptionThrownInSpawnedThreadPropagatesToMain() {
        fail("Implement this");
    }

    // private
}
