package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.shared.HasId;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static inaugural.soliloquy.tools.testing.Mock.hydrateMockHandler;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class AbstractImageAssetRenderableHandlerTests<TAsset extends HasId>
        extends AbstractRenderableWithMouseEventsHandlerTests {
    protected final String ASSET_ID = randomString();

    protected final String BORDER_THICKNESS = randomString();
    protected final String BORDER_COLOR = randomString();

    protected final String COLOR_SHIFT = randomString();
    protected final String AREA = randomString();

    @org.mockito.Mock protected ProviderAtTime<Float> mockBorderThicknessProvider;
    @org.mockito.Mock protected ProviderAtTime<Color> mockBorderColorProvider;
    @org.mockito.Mock protected ProviderAtTime<FloatBox> mockAreaProvider;

    @org.mockito.Mock protected ColorShift mockShift;
    @org.mockito.Mock protected TypeHandler<ColorShift> mockShiftHandler;

    protected TAsset mockAsset;
    protected Function<String, TAsset> mockGetAsset;

    protected void setUp(Class<TAsset> clazz) {
        super.setUp();

        var mockAssetAndLookup = generateMockLookupFunctionWithId(clazz, ASSET_ID);
        mockAsset = mockAssetAndLookup.entities.getFirst();
        mockGetAsset = mockAssetAndLookup.lookup;

        hydrateMockHandler(mockProviderHandler,
                pairOf(mockBorderThicknessProvider, BORDER_THICKNESS),
                pairOf(mockBorderColorProvider, BORDER_COLOR),
                pairOf(mockAreaProvider, AREA)
        );

        hydrateMockHandler(mockShiftHandler,
                pairOf(mockShift, COLOR_SHIFT)
        );
    }

    protected void setUpMockRenderable(ImageAssetRenderable mockRenderable) {
        super.setUpMockRenderable(mockRenderable);

        when(mockRenderable.getBorderThicknessProvider()).thenReturn(mockBorderThicknessProvider);
        when(mockRenderable.getBorderColorProvider()).thenReturn(mockBorderColorProvider);
        when(mockRenderable.colorShifts()).thenReturn(listOf(mockShift));
        when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(mockAreaProvider);
    }

    protected void verifyWritten(ImageAssetRenderable mockRenderable) {
        super.verifyWritten(mockRenderable);

        verify(mockRenderable, once()).getBorderThicknessProvider();
        verify(mockProviderHandler, once()).write(mockBorderThicknessProvider);
        verify(mockRenderable, once()).getBorderColorProvider();
        verify(mockProviderHandler, once()).write(mockBorderColorProvider);
        verify(mockRenderable, once()).colorShifts();
        verify(mockShiftHandler, once()).write(mockShift);
        verify(mockRenderable, once()).getRenderingDimensionsProvider();
        verify(mockProviderHandler, once()).write(mockAreaProvider);

        verify(mockAsset, once()).id();
    }

    protected void verifyRead() {
        super.verifyRead();

        verify(mockGetAsset, once()).apply(ASSET_ID);
        verify(mockProviderHandler, once()).read(BORDER_THICKNESS);
        verify(mockProviderHandler, once()).read(BORDER_COLOR);
        verify(mockShiftHandler, once()).read(COLOR_SHIFT);
        verify(mockProviderHandler, once()).read(AREA);
    }
}
