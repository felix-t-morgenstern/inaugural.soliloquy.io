package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.TextLineRenderableFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
import soliloquy.specs.io.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomFloat;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TextLineRenderableFactoryImplTests {
    private final HorizontalAlignment ALIGNMENT = HorizontalAlignment.LEFT;
    private final Map<Integer, ProviderAtTime<Color>> COLOR_PROVIDER_INDICES = mapOf();
    private final List<Integer> ITALIC_INDICES = listOf();
    private final List<Integer> BOLD_INDICES = listOf();

    private final UUID UUID = java.util.UUID.randomUUID();
    @Mock private Font mockFont;
    @Mock private ProviderAtTime<Float> mockHeightProvider;
    @Mock private ProviderAtTime<String> mockLineTextProvider;
    @Mock private Component mockContainingComponent;
    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private ProviderAtTime<Vertex> mockLocationProvider;
    @Mock private ProviderAtTime<Float> mockDropShadowSizeProvider;
    @Mock private ProviderAtTime<Vertex> mockDropShadowOffsetProvider;
    @Mock private ProviderAtTime<Color> mockDropShadowColorProvider;

    private TextLineRenderableFactory textLineRenderableFactory;

    @BeforeEach
    public void setUp() {
        textLineRenderableFactory = new TextLineRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        var z = randomInt();
        var paddingBetweenGlyphs = randomFloat();
        var renderable =
                textLineRenderableFactory.make(mockFont, mockLineTextProvider, mockLocationProvider,
                        mockHeightProvider, ALIGNMENT, paddingBetweenGlyphs,
                        COLOR_PROVIDER_INDICES, ITALIC_INDICES, BOLD_INDICES,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockDropShadowSizeProvider, mockDropShadowOffsetProvider,
                        mockDropShadowColorProvider, z, UUID, mockContainingComponent);

        assertNotNull(renderable);
        assertInstanceOf(TextLineRenderableImpl.class, renderable);
        assertSame(mockFont, renderable.getFont());
        assertSame(mockLineTextProvider, renderable.getLineTextProvider());
        assertSame(mockHeightProvider, renderable.lineHeightProvider());
        assertEquals(ALIGNMENT, renderable.getAlignment());
        assertEquals(paddingBetweenGlyphs, renderable.getPaddingBetweenGlyphs());
        assertEquals(COLOR_PROVIDER_INDICES, renderable.colorProviderIndices());
        assertSame(mockContainingComponent, renderable.getContainingComponent());

        renderable.delete();

        assertTrue(renderable.isDeleted());
    }

    // NB: Not testing make with invalid params, since it tests the same logic of
    //     TextLineRenderableImpl::new
}
