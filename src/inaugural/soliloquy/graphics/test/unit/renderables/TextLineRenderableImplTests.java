package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class TextLineRenderableImplTests {
    private final FakeFont FONT = new FakeFont();
    private final String LINE_TEXT = "lineText";
    private final float LINE_HEIGHT = 0.123f;
    private final float PADDING_BETWEEN_GLYPHS = 0.456f;
    private final HashMap<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES = new HashMap<>();
    private final ArrayList<Integer> ITALIC_INDICES = new ArrayList<>();
    private final ArrayList<Integer> BOLD_INDICES = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER =
            renderable -> _removeFromContainerInput = renderable;

    private static Renderable _removeFromContainerInput;

    private TextLineRenderable _textLineRenderable;

    @BeforeEach
    void setUp() {
        _textLineRenderable = new TextLineRenderableImpl(FONT, LINE_TEXT, LINE_HEIGHT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                RENDERING_AREA_PROVIDER, Z, UUID, REMOVE_FROM_CONTAINER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                null, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, null, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, 0f, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, null, Z, UUID,
                REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_AREA_PROVIDER, Z, null,
                REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_AREA_PROVIDER, Z, UUID,
                null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TextLineRenderable.class.getCanonicalName(),
                _textLineRenderable.getInterfaceName());
    }

    @Test
    void testFont() {
        assertSame(FONT, _textLineRenderable.font());
    }

    @Test
    void testLineText() {
        assertEquals(LINE_TEXT, _textLineRenderable.lineText());
    }

    @Test
    void testLineHeight() {
        assertEquals(LINE_HEIGHT, _textLineRenderable.lineHeight());
    }

    @Test
    void testPaddingBetweenGlyphs() {
        assertEquals(PADDING_BETWEEN_GLYPHS, _textLineRenderable.paddingBetweenGlyphs());
    }

    @Test
    void testColorProviderIndices() {
        assertSame(COLOR_PROVIDER_INDICES, _textLineRenderable.colorProviderIndices());
    }

    @Test
    void testItalicIndices() {
        assertSame(ITALIC_INDICES, _textLineRenderable.italicIndices());
    }

    @Test
    void testBoldIndices() {
        assertSame(BOLD_INDICES, _textLineRenderable.boldIndices());
    }

    @Test
    void testRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER, _textLineRenderable.renderingAreaProvider());
    }

    @Test
    void testZ() {
        assertEquals(Z, _textLineRenderable.z());
    }

    @Test
    void testUuid() {
        assertSame(UUID, _textLineRenderable.uuid());
    }

    @Test
    void testDelete() {
        _textLineRenderable.delete();

        assertSame(_textLineRenderable, _removeFromContainerInput);
    }
}
