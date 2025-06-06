package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.TextLineRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TextLineRenderableFactoryImplTests {
    private final FakeFont FONT = new FakeFont();
    private final float LINE_HEIGHT = 0.123f;
    private final FakeStaticProvider<Float> LINE_HEIGHT_PROVIDER =
            new FakeStaticProvider<>(LINE_HEIGHT);
    private final TextJustification JUSTIFICATION = TextJustification.LEFT;
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

    private final UUID UUID = java.util.UUID.randomUUID();
    @Mock private ProviderAtTime<String> mockLineTextProvider;
    @Mock private RenderableStack mockContainingStack;

    private TextLineRenderableFactory textLineRenderableFactory;

    @BeforeEach
    public void setUp() {
        //noinspection unchecked
        mockLineTextProvider = mock(ProviderAtTime.class);
        mockContainingStack = mock(RenderableStack.class);

        textLineRenderableFactory = new TextLineRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        int z = 123;
        float paddingBetweenGlyphs = 0.456f;
        TextLineRenderable textLineRenderable = textLineRenderableFactory.make(FONT,
                mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION, paddingBetweenGlyphs,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_PROVIDER, DROP_SHADOW_SIZE_PROVIDER,
                DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER, z, UUID,
                mockContainingStack);

        assertNotNull(textLineRenderable);
        assertInstanceOf(TextLineRenderableImpl.class, textLineRenderable);
        assertSame(FONT, textLineRenderable.getFont());
        assertSame(mockLineTextProvider, textLineRenderable.getLineTextProvider());
        assertSame(LINE_HEIGHT_PROVIDER, textLineRenderable.lineHeightProvider());
        assertEquals(JUSTIFICATION, textLineRenderable.getJustification());
        assertEquals(paddingBetweenGlyphs, textLineRenderable.getPaddingBetweenGlyphs());
        assertEquals(COLOR_PROVIDER_INDICES, textLineRenderable.colorProviderIndices());
        assertSame(mockContainingStack, textLineRenderable.containingStack());
    }

    // NB: Not testing make with invalid params, since it tests the same logic of
    //     TextLineRenderableImpl::new
}
