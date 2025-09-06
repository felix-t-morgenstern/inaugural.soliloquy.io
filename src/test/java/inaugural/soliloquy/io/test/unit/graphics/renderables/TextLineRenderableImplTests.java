package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
    @Mock private Component mockContainingComponent;
    private final UUID UUID = java.util.UUID.randomUUID();

    private TextLineRenderable renderable;

    @BeforeEach
    public void setUp() {
        renderable = new TextLineRenderableImpl(FONT, mockLineTextProvider,
                LINE_HEIGHT_PROVIDER, JUSTIFICATION, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_PROVIDER, DROP_SHADOW_SIZE_PROVIDER,
                DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER, Z, UUID,
                mockContainingComponent);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                null, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, null, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, null, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, null,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, TextJustification.UNKNOWN,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, null, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, null,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        // NB: These should not throw any exceptions
        new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                null, null, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent);
        new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                null, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent);
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                null, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, null, DROP_SHADOW_COLOR_PROVIDER,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, null,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_PROVIDER,
                DROP_SHADOW_SIZE_PROVIDER, DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER,
                Z, null, mockContainingComponent));
    }

    @Test
    public void testConstructorAddsSelfToContainingComponent() {
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testGetAndSetFont() {
        assertSame(FONT, renderable.getFont());

        var newFont = mock(Font.class);

        renderable.setFont(newFont);

        assertSame(newFont, renderable.getFont());
    }

    @Test
    public void testSetFontWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setFont(null));
    }

    @Test
    public void testGetAndSetLineTextProvider() {
        assertSame(mockLineTextProvider, renderable.getLineTextProvider());

        //noinspection unchecked
        mockLineTextProvider2 = mock(ProviderAtTime.class);

        renderable.setLineTextProvider(mockLineTextProvider2);

        assertSame(mockLineTextProvider2, renderable.getLineTextProvider());
    }

    @Test
    public void testSetLineTextWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setLineTextProvider(null));
    }

    @Test
    public void testGetAndSetLineHeight() {
        assertSame(LINE_HEIGHT_PROVIDER, renderable.lineHeightProvider());

        FakeStaticProvider<Float> newLineHeightProvider =
                new FakeStaticProvider<>(0.456f);

        renderable.setLineHeightProvider(newLineHeightProvider);

        assertEquals(newLineHeightProvider, renderable.lineHeightProvider());
    }

    @Test
    public void testGetAndSetJustification() {
        renderable.setJustification(TextJustification.CENTER);

        assertEquals(TextJustification.CENTER, renderable.getJustification());
    }

    @Test
    public void testSetJustificationWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setJustification(null));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setJustification(TextJustification.UNKNOWN));
    }

    @Test
    public void testSetLineHeightWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setLineHeightProvider(null));
    }

    @Test
    public void testGetAndSetPaddingBetweenGlyphs() {
        assertEquals(PADDING_BETWEEN_GLYPHS, renderable.getPaddingBetweenGlyphs());

        float newPaddingBetweenGlyphs = 0.789f;

        renderable.setPaddingBetweenGlyphs(newPaddingBetweenGlyphs);

        assertEquals(newPaddingBetweenGlyphs, renderable.getPaddingBetweenGlyphs());
    }

    @Test
    public void testColorProviderIndices() {
        assertEqualsAndNotSame(COLOR_PROVIDER_INDICES, renderable.colorProviderIndices());
    }

    @Test
    public void testItalicIndices() {
        assertEqualsAndNotSame(ITALIC_INDICES, renderable.italicIndices());
    }

    @Test
    public void testBoldIndices() {
        assertEqualsAndNotSame(BOLD_INDICES, renderable.boldIndices());
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER, renderable.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        renderable.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider, renderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        renderable.setBorderThicknessProvider(null);
        renderable.setBorderColorProvider(null);

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER, renderable.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        renderable.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider, renderable.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        renderable.setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER);

        assertThrows(IllegalArgumentException.class,
                () -> renderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetRenderingLocationProvider() {
        assertSame(RENDERING_PROVIDER,
                renderable.getRenderingLocationProvider());

        FakeProviderAtTime<Vertex> newRenderingLocationProvider =
                new FakeProviderAtTime<>();

        renderable.setRenderingLocationProvider(newRenderingLocationProvider);

        assertSame(newRenderingLocationProvider,
                renderable.getRenderingLocationProvider());
    }

    @Test
    public void testGetAndSetDropShadowSizeProvider() {
        assertSame(DROP_SHADOW_SIZE_PROVIDER, renderable.dropShadowSizeProvider());

        FakeProviderAtTime<Float> newDropShadowSizeProvider = new FakeProviderAtTime<>();

        renderable.setDropShadowSizeProvider(newDropShadowSizeProvider);

        assertSame(newDropShadowSizeProvider, renderable.dropShadowSizeProvider());
    }

    @Test
    public void testGetAndSetDropShadowOffsetProvider() {
        assertSame(DROP_SHADOW_OFFSET_PROVIDER, renderable.dropShadowOffsetProvider());

        FakeProviderAtTime<Vertex> newDropShadowOffsetProvider =
                new FakeProviderAtTime<>();

        renderable.setDropShadowOffsetProvider(newDropShadowOffsetProvider);

        assertSame(newDropShadowOffsetProvider, renderable.dropShadowOffsetProvider());
    }

    @Test
    public void testGetAndSetDropShadowColorProvider() {
        assertSame(DROP_SHADOW_COLOR_PROVIDER, renderable.dropShadowColorProvider());

        FakeProviderAtTime<Color> newDropShadowColorProvider = new FakeProviderAtTime<>();

        renderable.setDropShadowColorProvider(newDropShadowColorProvider);

        assertSame(newDropShadowColorProvider, renderable.dropShadowColorProvider());
    }

    @Test
    public void testSetRenderingLocationProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setRenderingLocationProvider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        int newZ = 456;

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertNull(renderable.component());
        assertTrue(renderable.isDeleted());
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.component());
    }

    @Test
    public void testSetComponent() {
        ((TextLineRenderableImpl) renderable).setComponent(null);

        assertNull(renderable.component());
    }
}
