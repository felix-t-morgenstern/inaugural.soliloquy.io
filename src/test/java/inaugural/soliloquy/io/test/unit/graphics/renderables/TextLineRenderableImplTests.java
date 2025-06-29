package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.assertEqualsAndNotSame;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TextLineRenderableImplTests {
    private final FakeFont FONT = new FakeFont();
    private final float LINE_HEIGHT = 0.123f;
    private final FakeStaticProvider<Float> LINE_HEIGHT_PROVIDER =
            new FakeStaticProvider<>(LINE_HEIGHT);
    private final TextJustification JUSTIFICATION = TextJustification.LEFT;
    private final float PADDING_BETWEEN_GLYPHS = 0.456f;
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES = mapOf();
    private final List<Integer> ITALIC_INDICES = listOf();
    private final List<Integer> BOLD_INDICES = listOf();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> RENDERING_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> DROP_SHADOW_SIZE_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> DROP_SHADOW_OFFSET_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> DROP_SHADOW_COLOR_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();

    @Mock private ProviderAtTime<String> mockLineTextProvider;
    @Mock private ProviderAtTime<String> mockLineTextProvider2;
    @Mock private RenderableStack mockContainingStack;
    private final UUID UUID = java.util.UUID.randomUUID();

    private TextLineRenderable textLineRenderable;

    @BeforeEach
    public void setUp() {
        //noinspection unchecked
        mockLineTextProvider = mock(ProviderAtTime.class);
        mockContainingStack = mock(RenderableStack.class);

        textLineRenderable = new TextLineRenderableImpl(FONT, mockLineTextProvider,
                LINE_HEIGHT_PROVIDER, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_PROVIDER, DROP_SHADOW_SIZE_PROVIDER,
                DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER, Z, UUID,
                mockContainingStack);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                null, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, null, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, null, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, null,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, TextJustification.UNKNOWN,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, null, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, null,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        // NB: These should not throw any exceptions
        new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                null, null, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack);
        new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                null, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack);
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                null, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, null, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, null,
                Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, null, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, null));
    }

    @Test
    public void testGetAndSetFont() {
        assertSame(FONT, textLineRenderable.getFont());

        FakeFont newFont = new FakeFont();

        textLineRenderable.setFont(newFont);

        assertSame(newFont, textLineRenderable.getFont());
    }

    @Test
    public void testSetFontWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> textLineRenderable.setFont(null));
    }

    @Test
    public void testGetAndSetLineTextProvider() {
        assertSame(mockLineTextProvider, textLineRenderable.getLineTextProvider());

        //noinspection unchecked
        mockLineTextProvider2 = mock(ProviderAtTime.class);

        textLineRenderable.setLineTextProvider(mockLineTextProvider2);

        assertSame(mockLineTextProvider2, textLineRenderable.getLineTextProvider());
    }

    @Test
    public void testSetLineTextWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderable.setLineTextProvider(null));
    }

    @Test
    public void testGetAndSetLineHeight() {
        assertSame(LINE_HEIGHT_PROVIDER, textLineRenderable.lineHeightProvider());

        FakeStaticProvider<Float> newLineHeightProvider =
                new FakeStaticProvider<>(0.456f);

        textLineRenderable.setLineHeightProvider(newLineHeightProvider);

        assertEquals(newLineHeightProvider, textLineRenderable.lineHeightProvider());
    }

    @Test
    public void testGetAndSetJustification() {
        textLineRenderable.setJustification(TextJustification.CENTER);

        assertEquals(TextJustification.CENTER, textLineRenderable.getJustification());
    }

    @Test
    public void testSetJustificationWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderable.setJustification(null));
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderable.setJustification(TextJustification.UNKNOWN));
    }

    @Test
    public void testSetLineHeightWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderable.setLineHeightProvider(null));
    }

    @Test
    public void testGetAndSetPaddingBetweenGlyphs() {
        assertEquals(PADDING_BETWEEN_GLYPHS, textLineRenderable.getPaddingBetweenGlyphs());

        float newPaddingBetweenGlyphs = 0.789f;

        textLineRenderable.setPaddingBetweenGlyphs(newPaddingBetweenGlyphs);

        assertEquals(newPaddingBetweenGlyphs, textLineRenderable.getPaddingBetweenGlyphs());
    }

    @Test
    public void testColorProviderIndices() {
        assertEqualsAndNotSame(COLOR_PROVIDER_INDICES, textLineRenderable.colorProviderIndices());
    }

    @Test
    public void testItalicIndices() {
        assertEqualsAndNotSame(ITALIC_INDICES, textLineRenderable.italicIndices());
    }

    @Test
    public void testBoldIndices() {
        assertEqualsAndNotSame(BOLD_INDICES, textLineRenderable.boldIndices());
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER, textLineRenderable.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        textLineRenderable.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider, textLineRenderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        textLineRenderable.setBorderThicknessProvider(null);
        textLineRenderable.setBorderColorProvider(null);

        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER));
        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER, textLineRenderable.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        textLineRenderable.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider, textLineRenderable.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        textLineRenderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER);

        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetRenderingLocationProvider() {
        assertSame(RENDERING_PROVIDER,
                textLineRenderable.getRenderingLocationProvider());

        FakeProviderAtTime<Vertex> newRenderingLocationProvider =
                new FakeProviderAtTime<>();

        textLineRenderable.setRenderingLocationProvider(newRenderingLocationProvider);

        assertSame(newRenderingLocationProvider,
                textLineRenderable.getRenderingLocationProvider());
    }

    @Test
    public void testGetAndSetDropShadowSizeProvider() {
        assertSame(DROP_SHADOW_SIZE_PROVIDER, textLineRenderable.dropShadowSizeProvider());

        FakeProviderAtTime<Float> newDropShadowSizeProvider = new FakeProviderAtTime<>();

        textLineRenderable.setDropShadowSizeProvider(newDropShadowSizeProvider);

        assertSame(newDropShadowSizeProvider, textLineRenderable.dropShadowSizeProvider());
    }

    @Test
    public void testGetAndSetDropShadowOffsetProvider() {
        assertSame(DROP_SHADOW_OFFSET_PROVIDER, textLineRenderable.dropShadowOffsetProvider());

        FakeProviderAtTime<Vertex> newDropShadowOffsetProvider =
                new FakeProviderAtTime<>();

        textLineRenderable.setDropShadowOffsetProvider(newDropShadowOffsetProvider);

        assertSame(newDropShadowOffsetProvider, textLineRenderable.dropShadowOffsetProvider());
    }

    @Test
    public void testGetAndSetDropShadowColorProvider() {
        assertSame(DROP_SHADOW_COLOR_PROVIDER, textLineRenderable.dropShadowColorProvider());

        FakeProviderAtTime<Color> newDropShadowColorProvider = new FakeProviderAtTime<>();

        textLineRenderable.setDropShadowColorProvider(newDropShadowColorProvider);

        assertSame(newDropShadowColorProvider, textLineRenderable.dropShadowColorProvider());
    }

    @Test
    public void testSetRenderingLocationProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderable.setRenderingLocationProvider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, textLineRenderable.getZ());

        int newZ = 456;

        textLineRenderable.setZ(newZ);

        assertEquals(newZ, textLineRenderable.getZ());
        verify(mockContainingStack, once()).add(textLineRenderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, textLineRenderable.uuid());
    }

    @Test
    public void testDelete() {
        textLineRenderable.delete();

        verify(mockContainingStack, once()).remove(textLineRenderable);
    }
}
