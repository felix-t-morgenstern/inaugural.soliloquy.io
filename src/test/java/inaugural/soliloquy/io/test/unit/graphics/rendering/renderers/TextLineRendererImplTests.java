package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeFontStyleInfo;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.assets.FontStyleInfo;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.Tools.round;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class TextLineRendererImplTests {
    private final Color DEFAULT_COLOR = Color.BLACK;
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Supplier<Float> mockGetScreenWToHRatio;
    @Mock private TimestampValidator mockTimestampValidator;

    @Mock private Font mockFont;
    @Mock private FontStyleInfo mockFontInfo;
    @Mock private Mesh mockMesh;
    @Mock private Shader mockShader;

    private soliloquy.specs.io.graphics.rendering.renderers.TextLineRenderer renderer;

    @BeforeEach
    public void setUp() {
        lenient().when(mockGetScreenWToHRatio.get()).thenReturn(randomFloat());

        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        lenient().when(mockFont.plain()).thenReturn(mockFontInfo);
        lenient().when(mockFont.bold()).thenReturn(mockFontInfo);
        lenient().when(mockFont.italic()).thenReturn(mockFontInfo);
        lenient().when(mockFont.boldItalic()).thenReturn(mockFontInfo);
        lenient().when(mockFontInfo.getUvCoordinatesForGlyph(anyChar()))
                .thenReturn(randomFloatBox());

        renderer = new TextLineRendererImpl(mockRenderingBoundaries,
                DEFAULT_COLOR, mockGetScreenWToHRatio, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(null, DEFAULT_COLOR,
                        mockGetScreenWToHRatio, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(mockRenderingBoundaries, null,
                        mockGetScreenWToHRatio, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(mockRenderingBoundaries, DEFAULT_COLOR,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(mockRenderingBoundaries, DEFAULT_COLOR,
                        mockGetScreenWToHRatio, null));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        var lineHeightProvider = generateMockStaticProvider(0.25f);
        var textLine = randomString();
        Map<Integer, ProviderAtTime<Color>> colorProviderIndices = mapOf();
        colorProviderIndices.put(4, generateMockStaticProvider(Color.RED));
        var italicIndices = listOf(2, 6);
        var boldIndices = listOf(3, 5);
        var renderingAreaProvider = generateMockStaticProvider(vertexOf(0f, 0f));
        var textLineRenderable = generateMockRenderable(mockFont,
                lineHeightProvider, 0f, textLine, generateMockStaticProvider(1f),
                generateMockStaticProvider(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, randomUUID());
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        when(textLineRenderable.getRenderingLocationProvider()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        when(textLineRenderable.getRenderingLocationProvider()).thenReturn(renderingAreaProvider);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        when(textLineRenderable.getFont()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        when(textLineRenderable.getFont()).thenReturn(mockFont);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        var height = generateMockStaticProvider((Float) null);
        when(textLineRenderable.lineHeightProvider()).thenReturn(height);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        height = generateMockStaticProvider(randomFloat());
        when(textLineRenderable.lineHeightProvider()).thenReturn(height);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        colorProviderIndices.put(null, generateMockStaticProvider(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        colorProviderIndices.remove(null);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        colorProviderIndices.remove(6);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        colorProviderIndices.put(-1, generateMockStaticProvider(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        colorProviderIndices.remove(-1);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        colorProviderIndices.remove(textLine.length());
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object) (null));
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        italicIndices.remove((Object) (-1));
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        italicIndices.remove(2);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        italicIndices.add(textLine.length() + 1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        italicIndices.remove((Object) (textLine.length() + 1));
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        when(textLineRenderable.italicIndices()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        when(textLineRenderable.italicIndices()).thenReturn(italicIndices);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object) (null));
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        boldIndices.remove((Object) (-1));
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        boldIndices.add(textLine.length() + 1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        boldIndices.remove((Object) (textLine.length() + 1));
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        boldIndices.remove(2);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        when(textLineRenderable.boldIndices()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        when(textLineRenderable.boldIndices()).thenReturn(boldIndices);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        when(textLineRenderable.getBorderColorProvider()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        var color = generateMockStaticProvider((Color) null);
        when(textLineRenderable.getBorderColorProvider()).thenReturn(color);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        var thickness = generateMockStaticProvider(-0.0001f);
        when(textLineRenderable.getBorderThicknessProvider()).thenReturn(thickness);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        thickness = generateMockStaticProvider(1f);
        when(textLineRenderable.getBorderThicknessProvider()).thenReturn(thickness);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        when(textLineRenderable.uuid()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        when(textLineRenderable.uuid()).thenReturn(randomUUID());
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));

        when(textLineRenderable.getAlignment()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(textLineRenderable, randomLong()));
        when(textLineRenderable.getAlignment()).thenReturn(HorizontalAlignment.LEFT);
        assertDoesNotThrow(() -> renderer.render(textLineRenderable, randomLong()));
    }

    @Test
    public void testTextLineLengthWithInvalidArgs() {
        var lineHeightProvider = generateMockStaticProvider(0.25f);
        String textLine = "Text line";
        Map<Integer, ProviderAtTime<Color>> colorProviderIndices = mapOf();
        colorProviderIndices.put(4, generateMockStaticProvider(Color.RED));
        List<Integer> italicIndices = listOf();
        italicIndices.add(2);
        italicIndices.add(6);
        List<Integer> boldIndices = listOf();
        boldIndices.add(3);
        boldIndices.add(5);
        ProviderAtTime<Vertex> renderingAreaProvider =
                generateMockStaticProvider(vertexOf(0f, 0f));
        var uuid = randomUUID();
        var textLineRenderable = generateMockRenderable(mockFont,
                lineHeightProvider, 0f, textLine, generateMockStaticProvider(1f),
                generateMockStaticProvider(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);



        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(null, MOST_RECENT_TIMESTAMP));

        when(textLineRenderable.getRenderingLocationProvider()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        when(textLineRenderable.getRenderingLocationProvider()).thenReturn(renderingAreaProvider);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        when(textLineRenderable.getFont()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        when(textLineRenderable.getFont()).thenReturn(mockFont);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        var nullProvider = generateMockStaticProvider((Float) null);
        when(textLineRenderable.lineHeightProvider()).thenReturn(nullProvider);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        var zeroProvider = generateMockStaticProvider(0f);
        when(textLineRenderable.lineHeightProvider()).thenReturn(zeroProvider);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        var heightProvider = generateMockStaticProvider(0.25f);
        when(textLineRenderable.lineHeightProvider()).thenReturn(heightProvider);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        colorProviderIndices.put(null, generateMockStaticProvider(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(null);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(6);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        colorProviderIndices.put(-1, generateMockStaticProvider(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(-1);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(textLine.length());
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object) (null));
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        italicIndices.remove((Object) (-1));
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        italicIndices.remove(2);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        when(textLineRenderable.italicIndices()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        when(textLineRenderable.italicIndices()).thenReturn(italicIndices);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object) (null));
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        boldIndices.remove((Object) (-1));
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        boldIndices.remove(2);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        when(textLineRenderable.boldIndices()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        when(textLineRenderable.boldIndices()).thenReturn(boldIndices);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        when(textLineRenderable.uuid()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        when(textLineRenderable.uuid()).thenReturn(uuid);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));

        when(textLineRenderable.getAlignment()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        when(textLineRenderable.getAlignment()).thenReturn(HorizontalAlignment.LEFT);
        assertDoesNotThrow(
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testTextLineLengthFromRenderable() {
        var plain = new FakeFontStyleInfo();
        var italic = new FakeFontStyleInfo();
        var bold = new FakeFontStyleInfo();
        var boldItalic = new FakeFontStyleInfo();
        var font = new FakeFont(plain, italic, bold, boldItalic);

        var glyphHeight = 0.1f;
        var glyphA = floatBoxOf(0.0f, 0.0f, 0.356659007059121f, glyphHeight);
        var glyphAItalic = floatBoxOf(0.0f, 0.0f, 0.48381785202459f, glyphHeight);
        var glyphABold = floatBoxOf(0.0f, 0.0f, 0.677026478f, glyphHeight);
        var glyphABoldItalic = floatBoxOf(0.0f, 0.0f, 0.24048836420184f, glyphHeight);
        var glyphB = floatBoxOf(0.0f, 0.0f, 0.213723488507345f, glyphHeight);
        var glyphBItalic = floatBoxOf(0.0f, 0.0f, 0.331731488913315f, glyphHeight);
        var glyphBBold = floatBoxOf(0.0f, 0.0f, 0.709300081504505f, glyphHeight);
        var glyphBBoldItalic = floatBoxOf(0.0f, 0.0f, 0.0767894524389122f, glyphHeight);

        plain.Glyphs.put('A', glyphA);
        italic.Glyphs.put('A', glyphAItalic);
        bold.Glyphs.put('A', glyphABold);
        boldItalic.Glyphs.put('A', glyphABoldItalic);
        plain.Glyphs.put('B', glyphB);
        italic.Glyphs.put('B', glyphBItalic);
        bold.Glyphs.put('B', glyphBBold);
        boldItalic.Glyphs.put('B', glyphBBoldItalic);

        var textureWidthToHeightRatio = 0.12f;
        var textureWidthToHeightRatioItalic = 0.34f;
        var textureWidthToHeightRatioBold = 0.56f;
        var textureWidthToHeightRatioBoldItalic = 0.78f;
        plain.TextureWidthToHeightRatio = textureWidthToHeightRatio;
        italic.TextureWidthToHeightRatio = textureWidthToHeightRatioItalic;
        bold.TextureWidthToHeightRatio = textureWidthToHeightRatioBold;
        boldItalic.TextureWidthToHeightRatio = textureWidthToHeightRatioBoldItalic;

        var lineHeight = 0.5f;
        var lineHeightProvider = generateMockStaticProvider(lineHeight);
        @SuppressWarnings("SpellCheckingInspection") String lineText = "AAAAAAAABBBBBBBB";
        List<Integer> italicIndices = listOf(1, 9);
        List<Integer> boldIndices = listOf(6, 14);

        var textLineRenderable = generateMockRenderable(font,
                lineHeightProvider, 0f, lineText, generateMockStaticProvider(null),
                generateMockStaticProvider(null), null, italicIndices, boldIndices,
                generateMockStaticProvider(vertexOf(0f, 0f)),
                randomUUID());

        var textLineLength = renderer.textLineLength(textLineRenderable,
                MOST_RECENT_TIMESTAMP);

        var expectedTextLineLength = ((glyphA.width() * 1 * textureWidthToHeightRatio) +
                (glyphAItalic.width() * 5 * textureWidthToHeightRatioItalic) +
                (glyphABoldItalic.width() * 2 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBoldItalic.width() * 1 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBold.width() * 5 * textureWidthToHeightRatioBold) +
                (glyphB.width() * 2 * textureWidthToHeightRatio)) *
                (lineHeight / glyphHeight);

        // NB: Test is accurate to four significant digits; inaccuracy beyond that point is likely
        //     due to floating point rounding discrepancies
        assertEquals(round(expectedTextLineLength, 4), round(textLineLength, 4));
    }

    @Test
    public void testTextLineLengthFromProps() {
        var plain = new FakeFontStyleInfo();
        var italic = new FakeFontStyleInfo();
        var bold = new FakeFontStyleInfo();
        var boldItalic = new FakeFontStyleInfo();
        var font = new FakeFont(plain, italic, bold, boldItalic);

        var glyphHeight = 0.1f;
        var glyphA = floatBoxOf(0.0f, 0.0f, 0.356659007059121f, glyphHeight);
        var glyphAItalic = floatBoxOf(0.0f, 0.0f, 0.48381785202459f, glyphHeight);
        var glyphABold = floatBoxOf(0.0f, 0.0f, 0.677026478f, glyphHeight);
        var glyphABoldItalic = floatBoxOf(0.0f, 0.0f, 0.24048836420184f, glyphHeight);
        var glyphB = floatBoxOf(0.0f, 0.0f, 0.213723488507345f, glyphHeight);
        var glyphBItalic = floatBoxOf(0.0f, 0.0f, 0.331731488913315f, glyphHeight);
        var glyphBBold = floatBoxOf(0.0f, 0.0f, 0.709300081504505f, glyphHeight);
        var glyphBBoldItalic = floatBoxOf(0.0f, 0.0f, 0.0767894524389122f, glyphHeight);

        plain.Glyphs.put('A', glyphA);
        italic.Glyphs.put('A', glyphAItalic);
        bold.Glyphs.put('A', glyphABold);
        boldItalic.Glyphs.put('A', glyphABoldItalic);
        plain.Glyphs.put('B', glyphB);
        italic.Glyphs.put('B', glyphBItalic);
        bold.Glyphs.put('B', glyphBBold);
        boldItalic.Glyphs.put('B', glyphBBoldItalic);

        var textureWidthToHeightRatio = 0.12f;
        var textureWidthToHeightRatioItalic = 0.34f;
        var textureWidthToHeightRatioBold = 0.56f;
        var textureWidthToHeightRatioBoldItalic = 0.78f;
        plain.TextureWidthToHeightRatio = textureWidthToHeightRatio;
        italic.TextureWidthToHeightRatio = textureWidthToHeightRatioItalic;
        bold.TextureWidthToHeightRatio = textureWidthToHeightRatioBold;
        boldItalic.TextureWidthToHeightRatio = textureWidthToHeightRatioBoldItalic;

        var lineHeight = 0.5f;
        @SuppressWarnings("SpellCheckingInspection") String lineText = "AAAAAAAABBBBBBBB";
        List<Integer> italicIndices = listOf(1, 9);
        List<Integer> boldIndices = listOf(6, 14);

        var textLineLength = renderer.textLineLength(
                lineText,
                font,
                0f,
                italicIndices,
                boldIndices,
                lineHeight
        );

        var expectedTextLineLength = ((glyphA.width() * 1 * textureWidthToHeightRatio) +
                (glyphAItalic.width() * 5 * textureWidthToHeightRatioItalic) +
                (glyphABoldItalic.width() * 2 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBoldItalic.width() * 1 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBold.width() * 5 * textureWidthToHeightRatioBold) +
                (glyphB.width() * 2 * textureWidthToHeightRatio)) *
                (lineHeight / glyphHeight);

        // NB: Test is accurate to four significant digits; inaccuracy beyond that point is likely
        //     due to floating point rounding discrepancies
        assertEquals(round(expectedTextLineLength, 4), round(textLineLength, 4));
    }

    @Test
    public void testGetGlyphWidth() {
        var aChar = randomChar();
        var glyphBox = randomFloatBox();
        when(mockFontInfo.getUvCoordinatesForGlyph(anyChar())).thenReturn(glyphBox);
        var glyphwiseWidthFactor = randomFloat();
        var glyphwiseWidthFactors = mapOf(pairOf(aChar, glyphwiseWidthFactor));
        when(mockFontInfo.glyphwiseWidthFactors()).thenReturn(glyphwiseWidthFactors);
        var textureWidthToHeightRatio = randomFloat();
        when(mockFontInfo.textureWidthToHeightRatio()).thenReturn(textureWidthToHeightRatio);
        var additionalHorizontalTextureSpacing = randomFloat();
        when(mockFontInfo.additionalHorizontalTextureSpacing())
                .thenReturn(additionalHorizontalTextureSpacing);
        var glyphAdditionalHorizontalTextureSpacing = randomFloat();
        var glyphwiseAdditionalHorizontalTextureSpacing =
                mapOf(pairOf(aChar, glyphAdditionalHorizontalTextureSpacing));
        when(mockFontInfo.glyphwiseAdditionalHorizontalTextureSpacing())
                .thenReturn(glyphwiseAdditionalHorizontalTextureSpacing);
        var lineHeight = randomFloat();

        var glyphLength = renderer.getGlyphWidth(aChar, mockFontInfo, lineHeight);

        var expectedBaseLength =
                (glyphBox.width() * glyphwiseWidthFactor) * (lineHeight / glyphBox.height()) *
                        textureWidthToHeightRatio;
        var expectedPaddingAdj = (additionalHorizontalTextureSpacing + glyphAdditionalHorizontalTextureSpacing) * lineHeight;
        var expectedLength = expectedBaseLength - expectedPaddingAdj;
        // NB: I've been seeing some indeterminacy over decimal places
        assertEquals(round(expectedLength, 4), round(glyphLength, 4));
    }

    @Test
    public void testTextLineLengthWithPaddingBetweenGlyphs() {
        var plain = new FakeFontStyleInfo();
        var italic = new FakeFontStyleInfo();
        var bold = new FakeFontStyleInfo();
        var boldItalic = new FakeFontStyleInfo();
        var font = new FakeFont(plain, italic, bold, boldItalic);

        var glyphHeight = 0.1f;
        var glyphA = floatBoxOf(0.0f, 0.0f, 0.356659007059121f, glyphHeight);
        var glyphAItalic = floatBoxOf(0.0f, 0.0f, 0.48381785202459f, glyphHeight);
        var glyphABold = floatBoxOf(0.0f, 0.0f, 0.677026478f, glyphHeight);
        var glyphABoldItalic = floatBoxOf(0.0f, 0.0f, 0.24048836420184f, glyphHeight);
        var glyphB = floatBoxOf(0.0f, 0.0f, 0.213723488507345f, glyphHeight);
        var glyphBItalic = floatBoxOf(0.0f, 0.0f, 0.331731488913315f, glyphHeight);
        var glyphBBold = floatBoxOf(0.0f, 0.0f, 0.709300081504505f, glyphHeight);
        var glyphBBoldItalic = floatBoxOf(0.0f, 0.0f, 0.0767894524389122f, glyphHeight);

        plain.Glyphs.put('A', glyphA);
        italic.Glyphs.put('A', glyphAItalic);
        bold.Glyphs.put('A', glyphABold);
        boldItalic.Glyphs.put('A', glyphABoldItalic);
        plain.Glyphs.put('B', glyphB);
        italic.Glyphs.put('B', glyphBItalic);
        bold.Glyphs.put('B', glyphBBold);
        boldItalic.Glyphs.put('B', glyphBBoldItalic);

        var textureWidthToHeightRatio = 0.12f;
        var textureWidthToHeightRatioItalic = 0.34f;
        var textureWidthToHeightRatioBold = 0.56f;
        var textureWidthToHeightRatioBoldItalic = 0.78f;
        plain.TextureWidthToHeightRatio = textureWidthToHeightRatio;
        italic.TextureWidthToHeightRatio = textureWidthToHeightRatioItalic;
        bold.TextureWidthToHeightRatio = textureWidthToHeightRatioBold;
        boldItalic.TextureWidthToHeightRatio = textureWidthToHeightRatioBoldItalic;

        var lineHeight = 0.5f;
        var lineHeightProvider = generateMockStaticProvider(lineHeight);
        @SuppressWarnings("SpellCheckingInspection") String lineText = "AAAAAAAABBBBBBBB";
        var italicIndices = listOf(1, 9);
        var boldIndices = listOf(6, 14);

        var paddingBetweenGlyphs = 0.123f;

        var textLineRenderable = generateMockRenderable(font,
                lineHeightProvider, paddingBetweenGlyphs, lineText,
                generateMockStaticProvider(null), generateMockStaticProvider(null), null,
                italicIndices, boldIndices, generateMockStaticProvider(vertexOf(0f, 0f)),
                randomUUID());

        var textLineLength =
                renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);

        var expectedTextLineLength = ((glyphA.width() * 1 * textureWidthToHeightRatio) +
                (glyphAItalic.width() * 5 * textureWidthToHeightRatioItalic) +
                (glyphABoldItalic.width() * 2 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBoldItalic.width() * 1 * textureWidthToHeightRatioBoldItalic) +
                (glyphBBold.width() * 5 * textureWidthToHeightRatioBold) +
                (glyphB.width() * 2 * textureWidthToHeightRatio)) *
                (lineHeight / glyphHeight) +
                (lineHeight * paddingBetweenGlyphs * (lineText.length() - 1));

        // NB: Test is accurate to four significant digits; inaccuracy beyond that point is likely
        //     due to floating point rounding discrepancies
        assertEquals(round(expectedTextLineLength, 4), round(textLineLength, 4));
    }

    @Test
    public void testRenderWithNullDropShadowProviders() {
        var font = mock(Font.class);
        var textLineRenderable = generateMockRenderable(font,
                generateMockStaticProvider(0.5f), 0f, "", generateMockStaticProvider(null),
                generateMockStaticProvider(null), null, listOf(), listOf(),
                generateMockStaticProvider(vertexOf(0f, 0f)),
                null,
                generateMockStaticProvider(vertexOf(.456f, .789f)),
                generateMockStaticProvider(Color.WHITE),
                randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        var size = generateMockStaticProvider(randomFloat());
        when(textLineRenderable.dropShadowSizeProvider()).thenReturn(size);
        var offset = generateMockStaticProvider((Vertex) null);
        when(textLineRenderable.dropShadowOffsetProvider()).thenReturn(offset);

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        offset = generateMockStaticProvider(randomVertex());
        when(textLineRenderable.dropShadowOffsetProvider()).thenReturn(offset);
        var color = generateMockStaticProvider((Color) null);
        when(textLineRenderable.dropShadowColorProvider()).thenReturn(color);

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithNegativeDropShadowSize() {
        var font = mock(Font.class);
        var textLineRenderable = generateMockRenderable(font,
                generateMockStaticProvider(0.5f), 0f, "", generateMockStaticProvider(null),
                generateMockStaticProvider(null), null, listOf(), listOf(),
                generateMockStaticProvider(vertexOf(0f, 0f)),
                generateMockStaticProvider(-.123f),
                generateMockStaticProvider(vertexOf(.456f, .789f)),
                generateMockStaticProvider(Color.WHITE),
                randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithPositiveDropShadowSizeAndOtherNullDropShadowValues() {
        var font = mock(Font.class);
        var textLineRenderable = generateMockRenderable(font,
                generateMockStaticProvider(0.5f), 0f, "", generateMockStaticProvider(null),
                generateMockStaticProvider(null), null, listOf(), listOf(),
                generateMockStaticProvider(vertexOf(0f, 0f)),
                generateMockStaticProvider(.123f),
                generateMockStaticProvider(null),
                generateMockStaticProvider(Color.WHITE),
                randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        var offset = generateMockStaticProvider(vertexOf(.456f, .789f));
        when(textLineRenderable.dropShadowOffsetProvider()).thenReturn(offset);
        when(textLineRenderable.dropShadowColorProvider()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var lineHeightProvider = generateMockStaticProvider(0.25f);
        var textLine = randomString();
        Map<Integer, ProviderAtTime<Color>> colorProviderIndices = mapOf();
        colorProviderIndices.put(4, generateMockStaticProvider(Color.RED));
        List<Integer> italicIndices = listOf();
        List<Integer> boldIndices = listOf();
        ProviderAtTime<Vertex> renderingAreaProvider = generateMockStaticProvider(vertexOf(0f, 0f));
        var uuid = randomUUID();
        var textLineRenderable = generateMockRenderable(mockFont,
                lineHeightProvider, 0f, textLine, generateMockStaticProvider(1f),
                generateMockStaticProvider(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);
        var timestamp = randomLong();
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        renderer.render(textLineRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }

    public TextLineRenderable generateMockRenderable(
            Font font,
            ProviderAtTime<Float> lineHeightProvider,
            float paddingBetweenGlyphs,
            String lineText,
            ProviderAtTime<Float> borderThicknessProvider,
            ProviderAtTime<Color> borderColorProvider,
            Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
            List<Integer> italicIndices,
            List<Integer> boldIndices,
            ProviderAtTime<Vertex> renderingLocationProvider,
            ProviderAtTime<Float> dropShadowSizeProvider,
            ProviderAtTime<Vertex> dropShadowOffsetProvider,
            ProviderAtTime<Color> dropShadowColorProvider,
            UUID uuid
    ) {
        var renderable = mock(TextLineRenderable.class);

        lenient().when(renderable.getFont()).thenReturn(font);
        lenient().when(renderable.lineHeightProvider()).thenReturn(lineHeightProvider);
        lenient().when(renderable.getPaddingBetweenGlyphs()).thenReturn(paddingBetweenGlyphs);
        var text = generateMockStaticProvider(lineText);
        lenient().when(renderable.getLineTextProvider()).thenReturn(text);
        lenient().when(renderable.getBorderThicknessProvider()).thenReturn(borderThicknessProvider);
        lenient().when(renderable.getBorderColorProvider()).thenReturn(borderColorProvider);
        lenient().when(renderable.colorProviderIndices()).thenReturn(colorProviderIndices);
        lenient().when(renderable.italicIndices()).thenReturn(italicIndices);
        lenient().when(renderable.boldIndices()).thenReturn(boldIndices);
        lenient().when(renderable.getRenderingLocationProvider())
                .thenReturn(renderingLocationProvider);
        lenient().when(renderable.uuid()).thenReturn(uuid);
        lenient().when(renderable.dropShadowSizeProvider()).thenReturn(dropShadowSizeProvider);
        lenient().when(renderable.dropShadowOffsetProvider()).thenReturn(dropShadowOffsetProvider);
        lenient().when(renderable.dropShadowColorProvider()).thenReturn(dropShadowColorProvider);
        lenient().when(renderable.getAlignment()).thenReturn(HorizontalAlignment.LEFT);

        return renderable;
    }

    private TextLineRenderable generateMockRenderable(
            Font font,
            ProviderAtTime<Float> lineHeightProvider,
            float paddingBetweenGlyphs,
            String lineText,
            ProviderAtTime<Float> borderThicknessProvider,
            ProviderAtTime<Color> borderColorProvider,
            Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
            List<Integer> italicIndices,
            List<Integer> boldIndices,
            ProviderAtTime<Vertex> renderingLocationProvider,
            UUID uuid
    ) {
        return generateMockRenderable(
                font,
                lineHeightProvider,
                paddingBetweenGlyphs,
                lineText,
                borderThicknessProvider,
                borderColorProvider,
                colorProviderIndices,
                italicIndices,
                boldIndices,
                renderingLocationProvider,
                generateMockStaticProvider(null),
                generateMockStaticProvider(null),
                generateMockStaticProvider(null),
                uuid
        );
    }
}
