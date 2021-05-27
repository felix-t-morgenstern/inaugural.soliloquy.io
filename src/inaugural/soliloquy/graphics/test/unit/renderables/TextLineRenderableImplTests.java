package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

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
    private final FakeProviderAtTime<Pair<Float,Float>> RENDERING_LOCATION_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER =
            renderable -> _removeFromContainerInput = renderable;
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> _updateZIndexInContainerInput = renderable;

    private static Renderable _updateZIndexInContainerInput;
    private static Renderable _removeFromContainerInput;

    private TextLineRenderable _textLineRenderable;

    @BeforeEach
    void setUp() {
        _textLineRenderable = new TextLineRenderableImpl(FONT, LINE_TEXT, LINE_HEIGHT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                RENDERING_LOCATION_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                null, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, null, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, 0f, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                null, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, null, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, null, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, null,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, null));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, LINE_TEXT, LINE_HEIGHT, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, RENDERING_LOCATION_PROVIDER, Z, UUID,
                null, REMOVE_FROM_CONTAINER));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TextLineRenderable.class.getCanonicalName(),
                _textLineRenderable.getInterfaceName());
    }

    @Test
    void testGetAndSetFont() {
        assertSame(FONT, _textLineRenderable.getFont());

        FakeFont newFont = new FakeFont();

        _textLineRenderable.setFont(newFont);

        assertSame(newFont, _textLineRenderable.getFont());
    }

    @Test
    void testSetFontWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderable.setFont(null));
    }

    @Test
    void testGetAndSetLineText() {
        assertEquals(LINE_TEXT, _textLineRenderable.getLineText());

        String newLineText = "newLineText";

        _textLineRenderable.setLineText(newLineText);

        assertEquals(newLineText, _textLineRenderable.getLineText());
    }

    @Test
    void testSetLineTextWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderable.setLineText(null));
    }

    @Test
    void testGetAndSetLineHeight() {
        assertEquals(LINE_HEIGHT, _textLineRenderable.getLineHeight());

        float newLineHeight = 0.456f;

        _textLineRenderable.setLineHeight(newLineHeight);

        assertEquals(newLineHeight, _textLineRenderable.getLineHeight());
    }

    @Test
    void testSetLineHeightWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderable.setLineHeight(0f));
    }

    @Test
    void testGetAndSetPaddingBetweenGlyphs() {
        assertEquals(PADDING_BETWEEN_GLYPHS, _textLineRenderable.getPaddingBetweenGlyphs());

        float newPaddingBetweenGlyphs = 0.789f;

        _textLineRenderable.setPaddingBetweenGlyphs(newPaddingBetweenGlyphs);

        assertEquals(newPaddingBetweenGlyphs, _textLineRenderable.getPaddingBetweenGlyphs());
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
    void testGetAndSetRenderingLocationProvider() {
        assertSame(RENDERING_LOCATION_PROVIDER,
                _textLineRenderable.getRenderingLocationProvider());

        FakeProviderAtTime<Pair<Float,Float>> newRenderingLocationProvider = new FakeProviderAtTime<>();

        _textLineRenderable.setRenderingLocationProvider(newRenderingLocationProvider);

        assertSame(newRenderingLocationProvider,
                _textLineRenderable.getRenderingLocationProvider());
    }

    @Test
    void testSetRenderingLocationProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _textLineRenderable.setRenderingLocationProvider(null));
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, _textLineRenderable.getZ());

        int newZ = 456;

        _textLineRenderable.setZ(newZ);

        assertEquals(newZ, _textLineRenderable.getZ());
        assertSame(_textLineRenderable, _updateZIndexInContainerInput);
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
