package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.TextLineRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class TextLineRenderableFactoryImplTests {
    private final FakeFont FONT = new FakeFont();
    private final String LINE_TEXT = "lineText";
    private final float LINE_HEIGHT = 0.123f;
    private final TextJustification JUSTIFICATION = TextJustification.LEFT;
    private final float PADDING_BETWEEN_GLYPHS = 0.456f;
    private final HashMap<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES = new HashMap<>();
    private final ArrayList<Integer> ITALIC_INDICES = new ArrayList<>();
    private final ArrayList<Integer> BOLD_INDICES = new ArrayList<>();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Pair<Float,Float>> RENDERING_LOCATION_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER = renderable -> {};
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER = renderable -> {};

    private TextLineRenderableFactory _textLineRenderableFactory;

    @BeforeEach
    void setUp() {
        _textLineRenderableFactory = new TextLineRenderableFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TextLineRenderableFactory.class.getCanonicalName(),
                _textLineRenderableFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        TextLineRenderable textLineRenderable = _textLineRenderableFactory.make(FONT, LINE_TEXT,
                LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES,
                ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                RENDERING_LOCATION_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                REMOVE_FROM_CONTAINER);

        assertNotNull(textLineRenderable);
        assertTrue(textLineRenderable instanceof TextLineRenderableImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                null, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, null, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, 0f, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, null, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, TextJustification.UNKNOWN, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, null, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        // NB: These should not throw any exceptions
        _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, null,
                null, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER);
        _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, null,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER);
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, null,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, null));
        assertThrows(IllegalArgumentException.class, () -> _textLineRenderableFactory.make(
                FONT, LINE_TEXT, LINE_HEIGHT, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, Z, UUID,
                null, REMOVE_FROM_CONTAINER));
    }
}
