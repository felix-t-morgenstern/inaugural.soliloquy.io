package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.GlobalLoopingAnimationFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimation;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.factories.GlobalLoopingAnimationFactory;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoopingAnimationFactoryImplTests {
    private final FakeEntityUuid UUID = new FakeEntityUuid();
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
        GlobalLoopingAnimation globalLoopingAnimation =
                _globalLoopingAnimationFactory.make(UUID, ANIMATION, PERIOD_MODULO_OFFSET);

        assertNotNull(globalLoopingAnimation);
        assertTrue(globalLoopingAnimation instanceof GlobalLoopingAnimationImpl);
        assertSame(UUID, globalLoopingAnimation.uuid());
        assertSame(ANIMATION_ID, globalLoopingAnimation.animationId());
        assertEquals(PERIOD_MODULO_OFFSET, globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(null, ANIMATION, PERIOD_MODULO_OFFSET));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(UUID, null, PERIOD_MODULO_OFFSET));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(UUID, ANIMATION, -1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationFactory.make(UUID, ANIMATION, MS_DURATION));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalLoopingAnimationFactory.class.getCanonicalName(),
                _globalLoopingAnimationFactory.getInterfaceName());
    }
}
