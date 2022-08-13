package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.GlobalLoopingAnimationFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.GlobalLoopingAnimationDefinition;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoopingAnimationFactoryImplTests {
    private final String ID = "globalLoopingAnimationId";
    private final int MS_DURATION = 456;
    private final String ANIMATION_ID = "animationId";
    private final FakeAnimation ANIMATION = new FakeAnimation(ANIMATION_ID, MS_DURATION);
    private final int PERIOD_MODULO_OFFSET = 123;

    private GlobalLoopingAnimationFactory _globalLoopingAnimationFactory;

    @BeforeEach
    void setUp() {
        _globalLoopingAnimationFactory = new GlobalLoopingAnimationFactoryImpl();
    }

    @Test
    void testMake() {
        long pauseTimestamp = 456456L;
        GlobalLoopingAnimation globalLoopingAnimation =
                _globalLoopingAnimationFactory.make(new GlobalLoopingAnimationDefinition(
                        ID, ANIMATION, PERIOD_MODULO_OFFSET, pauseTimestamp));

        assertNotNull(globalLoopingAnimation);
        assertTrue(globalLoopingAnimation instanceof GlobalLoopingAnimationImpl);
        assertSame(ID, globalLoopingAnimation.id());
        assertSame(ANIMATION_ID, globalLoopingAnimation.animationId());
        assertEquals(PERIOD_MODULO_OFFSET, globalLoopingAnimation.periodModuloOffset());
        assertEquals(pauseTimestamp, (long) globalLoopingAnimation.pausedTimestamp());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(new GlobalLoopingAnimationDefinition(
                        null, ANIMATION, PERIOD_MODULO_OFFSET, null)));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(new GlobalLoopingAnimationDefinition(
                        "", ANIMATION, PERIOD_MODULO_OFFSET, null)));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(new GlobalLoopingAnimationDefinition(
                        ID, null, PERIOD_MODULO_OFFSET, null)));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(new GlobalLoopingAnimationDefinition(
                        ID, ANIMATION, -1, null)));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(new GlobalLoopingAnimationDefinition(
                        ID, ANIMATION, MS_DURATION, null)));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalLoopingAnimationFactory.class.getCanonicalName(),
                _globalLoopingAnimationFactory.getInterfaceName());
    }
}
