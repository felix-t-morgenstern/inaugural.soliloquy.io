package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.Tools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.renderers.TextLineRenderer;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TextLineRendererImplTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final Color DEFAULT_COLOR = Color.BLACK;
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final long MOST_RECENT_TIMESTAMP = 123123L;

    private TextLineRenderer textLineRenderer;

    @BeforeEach
    public void setUp() {
        textLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES,
                DEFAULT_COLOR, WINDOW_RESOLUTION_MANAGER, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(null, DEFAULT_COLOR,
                        WINDOW_RESOLUTION_MANAGER, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRendererImpl(RENDERING_BOUNDARIES, null,
                        WINDOW_RESOLUTION_MANAGER, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> textLineRenderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> textLineRenderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        var font = new FakeFont();
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
        var textLineRenderable = new FakeTextLineRenderable(font,
                lineHeightProvider, 0f, textLine, new FakeStaticProvider<>(1f),
                new FakeStaticProvider<>(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);
        long timestamp = MOST_RECENT_TIMESTAMP;



        assertThrows(IllegalArgumentException.class, () -> textLineRenderer.render(null, 0L));

        textLineRenderable.RenderingLocationProvider = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.RenderingLocationProvider = renderingAreaProvider;
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.Font = font;
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0f);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0.25f);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(null, new FakeStaticProvider<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(null);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(6);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(-1, new FakeStaticProvider<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(-1);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        colorProviderIndices.remove(textLine.length());
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object) (null));
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        italicIndices.remove((Object) (-1));
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        italicIndices.remove(2);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        italicIndices.remove((Object) (textLine.length()));
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.ItalicIndices = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.ItalicIndices = italicIndices;
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object) (null));
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        boldIndices.remove((Object) (-1));
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(textLine.length());
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        boldIndices.remove((Object) (textLine.length()));
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        boldIndices.remove(2);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BoldIndices = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.BoldIndices = boldIndices;
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BorderColorProvider = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.BorderColorProvider = new FakeStaticProvider<>(null);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BorderThicknessProvider = new FakeStaticProvider<>(-0.0001f);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.BorderThicknessProvider = new FakeStaticProvider<>(1f);
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Uuid = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.Uuid = uuid;
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = TextJustification.UNKNOWN;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.render(textLineRenderable, 0L));
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            //noinspection UnusedAssignment
            textLineRenderer.render(textLineRenderable, timestamp++);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }
    }

    @Test
    public void testTextLineLengthWithInvalidArgs() {
        var font = new FakeFont();
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
        var textLineRenderable = new FakeTextLineRenderable(font,
                lineHeightProvider, 0f, textLine, new FakeStaticProvider<>(1f),
                new FakeStaticProvider<>(null), colorProviderIndices, italicIndices,
                boldIndices, renderingAreaProvider, uuid);



        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderer.render(null, MOST_RECENT_TIMESTAMP));

        textLineRenderable.RenderingLocationProvider = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.RenderingLocationProvider = renderingAreaProvider;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Font = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Font = font;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0f);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.LineHeightProvider = new FakeStaticProvider<>(0.25f);
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(null, new FakeStaticProvider<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(null);
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(6, null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(6);
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(-1, new FakeStaticProvider<>(Color.BLUE));
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(-1);
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        colorProviderIndices.put(textLine.length(), null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        colorProviderIndices.remove(textLine.length());
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        //noinspection RedundantCast,SuspiciousMethodCalls
        italicIndices.remove((Object) (null));
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        italicIndices.remove((Object) (-1));
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        italicIndices.add(2);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        italicIndices.remove(2);
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.ItalicIndices = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.ItalicIndices = italicIndices;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        //noinspection SuspiciousMethodCalls,RedundantCast
        boldIndices.remove((Object) (null));
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(-1);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        boldIndices.remove((Object) (-1));
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        boldIndices.add(3);
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        boldIndices.remove(2);
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.BoldIndices = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.BoldIndices = boldIndices;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Uuid = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Uuid = uuid;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = null;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
        }
        catch (IllegalArgumentException e) {
            fail("Should not throw IllegalArgumentException when all params are ostensibly valid");
        }
        catch (Exception ignored) {
        }

        textLineRenderable.Justification = TextJustification.UNKNOWN;
        assertThrows(IllegalArgumentException.class,
                () -> textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP));
        textLineRenderable.Justification = TextJustification.LEFT;
        try {
            textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);
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

        var textLineLength = textLineRenderer.textLineLength(textLineRenderable,
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
                textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP);

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
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        textLineRenderable.DropShadowSizeProvider = new FakeStaticProvider<>(null);
        textLineRenderable.DropShadowOffsetProvider = null;

        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        textLineRenderable.DropShadowOffsetProvider = new FakeStaticProvider<>(null);
        textLineRenderable.DropShadowColorProvider = null;

        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
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
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
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
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));

        textLineRenderable.DropShadowOffsetProvider =
                new FakeStaticProvider<>(vertexOf(.456f, .789f));
        textLineRenderable.DropShadowColorProvider = new FakeStaticProvider<>(null);

        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderOrGetLineLengthWithOutdatedTimestamp() {
        var font = new FakeFont();
        var lineHeightProvider = new FakeStaticProvider<>(0.5f);
        var textLineRenderable = new FakeTextLineRenderable(font,
                lineHeightProvider, 0f, "", new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null), null, listOf(), listOf(),
                new FakeStaticProvider<>(vertexOf(0f, 0f)),
                UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderer.render(textLineRenderable, MOST_RECENT_TIMESTAMP - 1L));
        assertThrows(IllegalArgumentException.class, () ->
                textLineRenderer.textLineLength(textLineRenderable, MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long) textLineRenderer.mostRecentTimestamp());
    }
}
