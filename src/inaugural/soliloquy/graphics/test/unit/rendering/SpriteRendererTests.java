package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.SpriteRenderer;
import inaugural.soliloquy.graphics.test.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpriteRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

    private Renderer<SpriteRenderable> _spriteRenderer;

    @BeforeEach
    void setUp() {
        _spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderer(null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderer(RENDERING_BOUNDARIES, null));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.setMesh(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        Sprite sprite = new FakeSprite();
        List<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = -0.44f;

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(null));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(null, colorShifts,
                        new FakeFloatBox(leftX, topY, rightX, bottomY))
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeFloatBox(leftX, topY, rightX, bottomY))
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeFloatBox(leftX, topY, leftX, bottomY))
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeFloatBox(leftX, topY, leftX, topY))
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                SpriteRenderable.class.getCanonicalName() + ">",
                _spriteRenderer.getInterfaceName());
    }
}
