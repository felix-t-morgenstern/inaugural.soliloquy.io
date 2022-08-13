package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.TextLineRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFont;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TextLineRenderableFactoryImplTests {
    private final FakeFont FONT = new FakeFont();
    private final float LINE_HEIGHT = 0.123f;
    private final FakeStaticProvider<Float> LINE_HEIGHT_PROVIDER =
            new FakeStaticProvider<>(LINE_HEIGHT);
    private final TextJustification JUSTIFICATION = TextJustification.LEFT;
    private final HashMap<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES = new HashMap<>();
    private final ArrayList<Integer> ITALIC_INDICES = new ArrayList<>();
    private final ArrayList<Integer> BOLD_INDICES = new ArrayList<>();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Pair<Float, Float>> RENDERING_LOCATION_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> DROP_SHADOW_SIZE_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Pair<Float, Float>> DROP_SHADOW_OFFSET_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> DROP_SHADOW_COLOR_PROVIDER =
            new FakeProviderAtTime<>();
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER = renderable -> {};
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER = renderable -> {};

    private final UUID UUID = java.util.UUID.randomUUID();
    @Mock private ProviderAtTime<String> _mockLineTextProvider;

    private TextLineRenderableFactory _textLineRenderableFactory;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        _mockLineTextProvider = mock(ProviderAtTime.class);

        _textLineRenderableFactory = new TextLineRenderableFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TextLineRenderableFactory.class.getCanonicalName(),
                _textLineRenderableFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        int z = 123;
        float paddingBetweenGlyphs = 0.456f;
        TextLineRenderable textLineRenderable = _textLineRenderableFactory.make(FONT,
                _mockLineTextProvider, LINE_HEIGHT_PROVIDER, JUSTIFICATION, paddingBetweenGlyphs,
                COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_LOCATION_PROVIDER, DROP_SHADOW_SIZE_PROVIDER,
                DROP_SHADOW_OFFSET_PROVIDER, DROP_SHADOW_COLOR_PROVIDER, z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER);

        assertNotNull(textLineRenderable);
        assertTrue(textLineRenderable instanceof TextLineRenderableImpl);
        assertSame(FONT, textLineRenderable.getFont());
        assertSame(_mockLineTextProvider, textLineRenderable.getLineTextProvider());
        assertSame(LINE_HEIGHT_PROVIDER, textLineRenderable.lineHeightProvider());
        assertEquals(JUSTIFICATION, textLineRenderable.getJustification());
        assertEquals(paddingBetweenGlyphs, textLineRenderable.getPaddingBetweenGlyphs());
        assertEquals(COLOR_PROVIDER_INDICES, textLineRenderable.colorProviderIndices());
    }

    // NB: Not testing make with invalid params, since it tests the same logic of
    //     TextLineRenderableImpl::new
}
