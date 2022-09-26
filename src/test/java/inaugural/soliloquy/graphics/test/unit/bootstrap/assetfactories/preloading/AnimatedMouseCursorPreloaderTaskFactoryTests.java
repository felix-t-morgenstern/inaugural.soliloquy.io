package inaugural.soliloquy.graphics.test.unit.bootstrap.assetfactories.preloading;

import inaugural.soliloquy.graphics.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.preloading.AnimatedMouseCursorPreloaderTaskFactory;
import inaugural.soliloquy.graphics.bootstrap.tasks.AnimatedMouseCursorPreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.bootstrap.assetfactories.AssetPreloaderTaskFactory;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AnimatedMouseCursorPreloaderTaskFactoryTests {
    @Mock
    private AnimatedMouseCursorProviderFactory _mockAnimatedMouseCursorProviderFactory;

    private AssetPreloaderTaskFactory<AnimatedMouseCursorDefinitionDTO,
            AnimatedMouseCursorPreloaderTask> _animatedMouseCursorPreloaderTaskFactory;

    @BeforeEach
    void setUp() {
        _mockAnimatedMouseCursorProviderFactory = mock(AnimatedMouseCursorProviderFactory.class);

        _animatedMouseCursorPreloaderTaskFactory = new AnimatedMouseCursorPreloaderTaskFactory(
                id -> 0L, _mockAnimatedMouseCursorProviderFactory, result -> {}
        );
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTaskFactory(
                        null, _mockAnimatedMouseCursorProviderFactory, result -> {}
                ));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTaskFactory(
                        id -> 0L, null, result -> {}
                ));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTaskFactory(
                        id -> 0L, _mockAnimatedMouseCursorProviderFactory, null
                ));
    }

    @Test
    void testMake() {
        AnimatedMouseCursorPreloaderTask task =
                _animatedMouseCursorPreloaderTaskFactory.make(new ArrayList<>());

        assertNotNull(task);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AssetPreloaderTaskFactory.class.getCanonicalName() + "<" +
                        AnimatedMouseCursorDefinitionDTO.class.getCanonicalName() + "," +
                        AnimatedMouseCursorPreloaderTask.class.getCanonicalName() + ">",
                _animatedMouseCursorPreloaderTaskFactory.getInterfaceName());
    }
}
