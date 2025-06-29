package inaugural.soliloquy.io.test.unit.graphics.bootstrap.assetfactories.preloading;

import inaugural.soliloquy.io.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.preloading.AnimatedMouseCursorPreloaderTaskFactory;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.AnimatedMouseCursorPreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.AssetPreloaderTaskFactory;
import soliloquy.specs.io.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;


import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AnimatedMouseCursorPreloaderTaskFactoryTests {
    @Mock
    private AnimatedMouseCursorProviderFactory mockAnimatedMouseCursorProviderFactory;

    private AssetPreloaderTaskFactory<AnimatedMouseCursorDefinitionDTO,
            AnimatedMouseCursorPreloaderTask> animatedMouseCursorPreloaderTaskFactory;

    @BeforeEach
    public void setUp() {
        mockAnimatedMouseCursorProviderFactory = mock(AnimatedMouseCursorProviderFactory.class);

        animatedMouseCursorPreloaderTaskFactory = new AnimatedMouseCursorPreloaderTaskFactory(
                _ -> 0L, mockAnimatedMouseCursorProviderFactory, _ -> {}
        );
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTaskFactory(
                        null, mockAnimatedMouseCursorProviderFactory, result -> {}
                ));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTaskFactory(
                        id -> 0L, null, result -> {}
                ));
        assertThrows(IllegalArgumentException.class, () ->
                new AnimatedMouseCursorPreloaderTaskFactory(
                        id -> 0L, mockAnimatedMouseCursorProviderFactory, null
                ));
    }

    @Test
    public void testMake() {
        AnimatedMouseCursorPreloaderTask task =
                animatedMouseCursorPreloaderTaskFactory.make(listOf());

        assertNotNull(task);
    }
}
