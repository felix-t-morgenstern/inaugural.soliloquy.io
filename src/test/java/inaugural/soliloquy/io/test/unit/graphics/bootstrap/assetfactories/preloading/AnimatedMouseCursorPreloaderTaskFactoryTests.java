package inaugural.soliloquy.io.test.unit.graphics.bootstrap.assetfactories.preloading;

import inaugural.soliloquy.io.api.dto.AnimatedMouseCursorDefinitionDTO;
import inaugural.soliloquy.io.bootstrap.assetfactories.preloading.AnimatedMouseCursorPreloaderTaskFactory;
import inaugural.soliloquy.io.bootstrap.tasks.AnimatedMouseCursorPreloaderTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.bootstrap.assetfactories.AssetPreloaderTaskFactory;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;


import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AnimatedMouseCursorPreloaderTaskFactoryTests {
    @Mock private Function<AnimatedMouseCursorProviderDefinition, AnimatedMouseCursorProvider>
            mockAnimatedMouseCursorProviderFactory;

    private AssetPreloaderTaskFactory<AnimatedMouseCursorDefinitionDTO,
            AnimatedMouseCursorPreloaderTask> animatedMouseCursorPreloaderTaskFactory;

    @BeforeEach
    public void setUp() {
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
        var task = animatedMouseCursorPreloaderTaskFactory.make(listOf());

        assertNotNull(task);
    }
}
