package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFont;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
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
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TextLineRenderableImplTests {
    private final FakeFont FONT = new FakeFont();
    private final float LINE_HEIGHT = 0.123f;
    private final ProviderAtTime<Float> LINE_HEIGHT_PROVIDER =
            generateMockStaticProvider(LINE_HEIGHT);
    private final HorizontalAlignment ALIGNMENT = HorizontalAlignment.LEFT;
    private final float PADDING_BETWEEN_GLYPHS = 0.456f;
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES = mapOf();
    private final List<Integer> ITALIC_INDICES = listOf();
    private final List<Integer> BOLD_INDICES = listOf();
    private final int Z = randomInt();

    @Mock private ProviderAtTime<String> mockLineTextProvider;
    @Mock private ProviderAtTime<String> mockLineTextProvider2;
    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private ProviderAtTime<Vertex> mockRenderingProvider;
    @Mock private ProviderAtTime<Float> mockDropShadowSizeProvider;
    @Mock private ProviderAtTime<Vertex> mockDropShadowOffsetProvider;
    @Mock private ProviderAtTime<Color> mockDropShadowColorProvider;
    @Mock private Component mockContainingComponent;
    private final UUID UUID = java.util.UUID.randomUUID();

    private TextLineRenderable renderable;

    @BeforeEach
    public void setUp() {
        renderable = new TextLineRenderableImpl(FONT, mockLineTextProvider,
                LINE_HEIGHT_PROVIDER, ALIGNMENT, PADDING_BETWEEN_GLYPHS,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, mockBorderThicknessProvider,
                mockBorderColorProvider, mockRenderingProvider, mockDropShadowSizeProvider,
                mockDropShadowOffsetProvider, mockDropShadowColorProvider, Z, UUID,
                mockContainingComponent);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                null, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, null, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, null, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, null,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, null, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, null,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, null, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        // NB: These should not throw any exceptions
        new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                null, null, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent);
        new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                null, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent);
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, null,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                null, mockDropShadowOffsetProvider, mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, null, mockDropShadowColorProvider,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider, null,
                Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new TextLineRenderableImpl(
                FONT, mockLineTextProvider, LINE_HEIGHT_PROVIDER, ALIGNMENT,
                PADDING_BETWEEN_GLYPHS, COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                mockBorderThicknessProvider, mockBorderColorProvider, mockRenderingProvider,
                mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                mockDropShadowColorProvider,
                Z, null, mockContainingComponent));
    }

    @Test
    public void testConstructorDoesNotAddSelfToContainingComponent() {
        verify(mockContainingComponent, never()).add(renderable);
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

        var newLineHeightProvider = generateMockStaticProvider(0.456f);

        renderable.setLineHeightProvider(newLineHeightProvider);

        assertEquals(newLineHeightProvider, renderable.lineHeightProvider());
    }

    @Test
    public void testGetAndSetAlignment() {
        renderable.setAlignment(HorizontalAlignment.CENTER);

        assertEquals(HorizontalAlignment.CENTER, renderable.getAlignment());
    }

    @Test
    public void testSetAlignmentWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setAlignment(null));
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
        assertSame(mockBorderThicknessProvider, renderable.getBorderThicknessProvider());

        @SuppressWarnings("unchecked") var newBorderThicknessProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider, renderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        renderable.setBorderThicknessProvider(null);
        renderable.setBorderColorProvider(null);

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBorderThicknessProvider(mockBorderThicknessProvider));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBorderThicknessProvider(mockBorderThicknessProvider));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(mockBorderColorProvider, renderable.getBorderColorProvider());

        @SuppressWarnings("unchecked") var newBorderColorProvider =
                (ProviderAtTime<Color>) mock(ProviderAtTime.class);

        renderable.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider, renderable.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        renderable.setBorderThicknessProvider(mockBorderThicknessProvider);

        assertThrows(IllegalArgumentException.class,
                () -> renderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetRenderingLocationProvider() {
        assertSame(mockRenderingProvider,
                renderable.getRenderingLocationProvider());

        @SuppressWarnings("unchecked") var newRenderingLocationProvider =
                (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);

        renderable.setRenderingLocationProvider(newRenderingLocationProvider);

        assertSame(newRenderingLocationProvider,
                renderable.getRenderingLocationProvider());
    }

    @Test
    public void testGetAndSetDropShadowSizeProvider() {
        assertSame(mockDropShadowSizeProvider, renderable.dropShadowSizeProvider());

        @SuppressWarnings("unchecked") var newDropShadowSizeProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setDropShadowSizeProvider(newDropShadowSizeProvider);

        assertSame(newDropShadowSizeProvider, renderable.dropShadowSizeProvider());
    }

    @Test
    public void testGetAndSetDropShadowOffsetProvider() {
        assertSame(mockDropShadowOffsetProvider, renderable.dropShadowOffsetProvider());

        @SuppressWarnings("unchecked") var newDropShadowOffsetProvider =
                (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);

        renderable.setDropShadowOffsetProvider(newDropShadowOffsetProvider);

        assertSame(newDropShadowOffsetProvider, renderable.dropShadowOffsetProvider());
    }

    @Test
    public void testGetAndSetDropShadowColorProvider() {
        assertSame(mockDropShadowColorProvider, renderable.dropShadowColorProvider());

        @SuppressWarnings("unchecked") var newDropShadowColorProvider =
                (ProviderAtTime<Color>) mock(ProviderAtTime.class);

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

        var newZ = randomInt();

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertTrue(renderable.isDeleted());
        verify(mockContainingComponent, once()).remove(renderable);
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.getContainingComponent());
    }

    @Test
    public void testSetComponent() {
        ((TextLineRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.getContainingComponent());
    }
}
