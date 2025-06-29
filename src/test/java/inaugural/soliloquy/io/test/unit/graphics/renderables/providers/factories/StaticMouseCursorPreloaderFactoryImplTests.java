package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticMouseCursorProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.StaticMouseCursorPreloaderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.StaticMouseCursorProviderDefinition;
import soliloquy.specs.io.graphics.renderables.providers.StaticMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticMouseCursorProviderFactory;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;

public class StaticMouseCursorPreloaderFactoryImplTests {
    private final String ID = randomString();
    private final Long PROVIDED_VALUE = randomLong();

    private StaticMouseCursorProviderFactory staticMouseCursorProviderFactory;

    @BeforeEach
    public void setUp() {
        staticMouseCursorProviderFactory = new StaticMouseCursorPreloaderFactoryImpl();
    }

    @Test
    public void make() {
        StaticMouseCursorProvider staticMouseCursorProvider = staticMouseCursorProviderFactory
                .make(new StaticMouseCursorProviderDefinition(ID, PROVIDED_VALUE));

        assertNotNull(staticMouseCursorProvider);
        assertInstanceOf(StaticMouseCursorProviderImpl.class, staticMouseCursorProvider);
        assertEquals(ID, staticMouseCursorProvider.id());
        assertEquals(PROVIDED_VALUE, staticMouseCursorProvider.provide(randomLong()));
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> staticMouseCursorProviderFactory
                .make(new StaticMouseCursorProviderDefinition(null, PROVIDED_VALUE)));
        assertThrows(IllegalArgumentException.class, () -> staticMouseCursorProviderFactory
                .make(new StaticMouseCursorProviderDefinition("", PROVIDED_VALUE)));
    }
}
