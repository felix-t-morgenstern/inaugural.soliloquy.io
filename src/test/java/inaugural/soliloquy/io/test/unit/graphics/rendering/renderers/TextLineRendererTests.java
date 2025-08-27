package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.Tools;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.assets.FontStyleInfo;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomFloatBox;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class TextLineRendererTests {
    private final Color DEFAULT_COLOR = Color.BLACK;
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    @Mock private Font mockFont;
    @Mock private FontStyleInfo mockFontInfo;

    private soliloquy.specs.io.graphics.rendering.renderers.TextLineRenderer renderer;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        lenient().when(mockFont.plain()).thenReturn(mockFontInfo);
        lenient().when(mockFont.bold()).thenReturn(mockFontInfo);
        lenient().when(mockFont.italic()).thenReturn(mockFontInfo);
        lenient().when(mockFont.boldItalic()).thenReturn(mockFontInfo);
        lenient().when(mockFontInfo.getUvCoordinatesForGlyph(anyChar())).thenReturn(randomFloatBox());

        renderer = new TextLineRenderer(mockRenderingBoundaries,
                DEFAULT_COLOR, WINDOW_RESOLUTION_MANAGER, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderer(null, DEFAULT_COLOR,
                        WINDOW_RESOLUTION_MANAGER, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderer(mockRenderingBoundaries, null,
                        WINDOW_RESOLUTION_MANAGER, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderer(mockRenderingBoundaries, DEFAULT_COLOR,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderer(mockRenderingBoundaries, DEFAULT_COLOR,
                        WINDOW_RESOLUTION_MANAGER, null));
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
        var lineHeightProvider = new FakeStaticProvider<>(0.25f);
        String textLine = "Text line";
        Map<Integer, ProviderAtTime<Color>> colorProviderIndices = mapOf();
        colorProviderIndices.put(4, new FakeStaticProvider<>(Color.RED));
        List<Integer> italicIndices = listOf();
        italicIndices.add(2);
        italicIndices.add(6);
        List<Integer> boldIndices = listOf();
        boldIndices.add(3);
        boldIndices.add(5);
        ProviderAtTime<Vertex> renderingAreaProvider = new FakeStaticProvider<>(vertexOf(0f, 0f));
        var uuid = UUID.randomUUID();
        var textLineRenderable = new FakeTextLineRenderable(mockFont,
                lineHeightProvider, 0f, textLine, new FakeStaticProvider<>(1f),
                new FakeStaticProvider<>(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);
        long timestamp = MOST_RECENT_TIMESTAMP;



        textLineRenderable.RenderingLocationProvider = null;
        textLineRenderable.RenderingLocationProvider = renderingAreaProvider;
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Font = null;
        textLineRenderable.Font = mockFont;
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0f);
        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0.25f);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(null, new FakeStaticProvider<>(Color.BLUE));
        colorProviderIndices.remove(null);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(6, null);
        colorProviderIndices.remove(6);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(-1, new FakeStaticProvider<>(Color.BLUE));
        colorProviderIndices.remove(-1);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(textLine.length(), null);
        colorProviderIndices.remove(textLine.length());
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(null);
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object) (null));
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(-1);
        italicIndices.remove((Object) (-1));
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(2);
        italicIndices.remove(2);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(textLine.length());
        italicIndices.remove((Object) (textLine.length()));
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.ItalicIndices = null;
        textLineRenderable.ItalicIndices = italicIndices;
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(null);
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object) (null));
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(-1);
        boldIndices.remove((Object) (-1));
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(textLine.length());
        boldIndices.remove((Object) (textLine.length()));
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(3);
        boldIndices.remove(2);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BoldIndices = null;
        textLineRenderable.BoldIndices = boldIndices;
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BorderColorProvider = null;
        textLineRenderable.BorderColorProvider = new FakeStaticProvider<>(null);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BorderThicknessProvider = new FakeStaticProvider<>(-0.0001f);
        textLineRenderable.BorderThicknessProvider = new FakeStaticProvider<>(1f);
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Uuid = null;
        textLineRenderable.Uuid = uuid;
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = null;
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = TextJustification.UNKNOWN;
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            //noinspection UnusedAssignment
            renderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }
    }

    @Test
    public void testTextLineLengthWithInvalidArgs() {
        var lineHeightProvider = new FakeStaticProvider<>(0.25f);
        String textLine = "Text line";
        Map<Integer, ProviderAtTime<Color>> colorProviderIndices = mapOf();
        colorProviderIndices.put(4, new FakeStaticProvider<>(Color.RED));
        List<Integer> italicIndices = listOf();
        italicIndices.add(2);
        italicIndices.add(6);
        List<Integer> boldIndices = listOf();
        boldIndices.add(3);
        boldIndices.add(5);
        ProviderAtTime<Vertex> renderingAreaProvider =
                new FakeStaticProvider<>(vertexOf(0f, 0f));
        var uuid = UUID.randomUUID();
        var textLineRenderable = new FakeTextLineRenderable(mockFont,
                lineHeightProvider, 0f, textLine, new FakeStaticProvider<>(1f),
                new FakeStaticProvider<>(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);



        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(null, MOST_RECENT_TIMESTAMP));

        textLineRenderable.RenderingLocationProvider = null;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.RenderingLocationProvider = renderingAreaProvider;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Font = mockFont;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0f);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0.25f);
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(null, new FakeStaticProvider<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(null);
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(6);
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(-1, new FakeStaticProvider<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(-1);
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(textLine.length());
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object) (null));
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        italicIndices.remove((Object) (-1));
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        italicIndices.remove(2);
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.ItalicIndices = null;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.ItalicIndices = italicIndices;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object) (null));
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        boldIndices.remove((Object) (-1));
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        boldIndices.remove(2);
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BoldIndices = null;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.BoldIndices = boldIndices;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Uuid = null;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Uuid = uuid;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = null;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = TextJustification.UNKNOWN;
        assertThrows(IllegalArgumentException.class,
                () -> renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            renderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }
    }

    @Test
    public void testTextLineLength() {
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
        var lineHeightProvider = new FakeStaticProvider<>(lineHeight);
        @SuppressWarnings("SpellCheckingInspection") String lineText = "AAAAAAAABBBBBBBB";
        List<Integer> italicIndices = listOf(1, 9);
        List<Integer> boldIndices = listOf(6, 14);

        var textLineRenderable = new FakeTextLineRenderable(font,
                lineHeightProvider, 0f, lineText, new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null), null, italicIndices, boldIndices,
                new FakeStaticProvider<>(vertexOf(0f, 0f)),
                UUID.randomUUID());

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
        assertEquals(Tools.round(expectedTextLineLength, 4), Tools.round(textLineLength, 4));
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
        var lineHeightProvider = new FakeStaticProvider<>(lineHeight);
        @SuppressWarnings("SpellCheckingInspection") String lineText = "AAAAAAAABBBBBBBB";
        var italicIndices = listOf(1, 9);
        var boldIndices = listOf(6, 14);

        var paddingBetweenGlyphs = 0.123f;

        var textLineRenderable = new FakeTextLineRenderable(font,
                lineHeightProvider, paddingBetweenGlyphs, lineText,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                italicIndices, boldIndices, new FakeStaticProvider<>(vertexOf(0f, 0f)),
                UUID.randomUUID());

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
        assertEquals(Tools.round(expectedTextLineLength, 4), Tools.round(textLineLength, 4));
    }

    @Test
    public void testRenderWithNullDropShadowProviders() {
        var font = new FakeFont();
        var textLineRenderable = new FakeTextLineRenderable(font,
                new FakeStaticProvider<>(0.5f), 0f, "", new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null), null, listOf(), listOf(),
                new FakeStaticProvider<>(vertexOf(0f, 0f)),
                null,
                new FakeStaticProvider<>(vertexOf(.456f, .789f)),
                new FakeStaticProvider<>(Color.WHITE),
                UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        textLineRenderable.DropShadowSizeProvider = new FakeStaticProvider<>(null);
        textLineRenderable.DropShadowOffsetProvider = null;

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        textLineRenderable.DropShadowOffsetProvider = new FakeStaticProvider<>(null);
        textLineRenderable.DropShadowColorProvider = null;

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithNegativeDropShadowSize() {
        var font = new FakeFont();
        var textLineRenderable = new FakeTextLineRenderable(font,
                new FakeStaticProvider<>(0.5f), 0f, "", new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null), null, listOf(), listOf(),
                new FakeStaticProvider<>(vertexOf(0f, 0f)),
                new FakeStaticProvider<>(-.123f),
                new FakeStaticProvider<>(vertexOf(.456f, .789f)),
                new FakeStaticProvider<>(Color.WHITE),
                UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithPositiveDropShadowSizeAndOtherNullDropShadowValues() {
        var font = new FakeFont();
        var textLineRenderable = new FakeTextLineRenderable(font,
                new FakeStaticProvider<>(0.5f), 0f, "", new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null), null, listOf(), listOf(),
                new FakeStaticProvider<>(vertexOf(0f, 0f)),
                new FakeStaticProvider<>(.123f),
                new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(Color.WHITE),
                UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        textLineRenderable.DropShadowOffsetProvider =
                new FakeStaticProvider<>(vertexOf(.456f, .789f));
        textLineRenderable.DropShadowColorProvider = new FakeStaticProvider<>(null);

        assertThrows(IllegalArgumentException.class, () ->
                renderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var lineHeightProvider = new FakeStaticProvider<>(0.25f);
        String textLine = "Text line";
        Map<Integer, ProviderAtTime<Color>> colorProviderIndices = mapOf();
        colorProviderIndices.put(4, new FakeStaticProvider<>(Color.RED));
        List<Integer> italicIndices = listOf();
        List<Integer> boldIndices = listOf();
        ProviderAtTime<Vertex> renderingAreaProvider = new FakeStaticProvider<>(vertexOf(0f, 0f));
        var uuid = UUID.randomUUID();
        var textLineRenderable = new FakeTextLineRenderable(mockFont,
                lineHeightProvider, 0f, textLine, new FakeStaticProvider<>(1f),
                new FakeStaticProvider<>(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);
        var timestamp = randomLong();

        renderer.render(textLineRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }
}
