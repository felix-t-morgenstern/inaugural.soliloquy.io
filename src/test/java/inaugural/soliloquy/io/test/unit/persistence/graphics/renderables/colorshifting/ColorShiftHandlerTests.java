package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables.colorshifting;

import inaugural.soliloquy.io.persistence.graphics.renderables.colorshifting.ColorShiftHandler;
import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.colorshifting.*;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.hydrateMockHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.io.graphics.renderables.colorshifting.BrightnessShift.brightnessShift;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorComponentIntensityShift.colorComponentShift;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorRotationShift.rotationShift;

@ExtendWith(MockitoExtension.class)
public class ColorShiftHandlerTests {
    private final String TYPE_BRIGHTNESS = "brightness";
    private final String TYPE_ROTATION = "rotation";
    private final String TYPE_COMPONENT_SHIFT = "compShift";

    private final String AMOUNT_PROVIDER = randomString();
    private final boolean OVERRIDES = randomBoolean();
    private final ColorComponent COMPONENT = ColorComponent.fromValue(randomIntInRange(0, 4));

    @Mock private ProviderAtTime<Float> mockAmountProvider;
    @Mock private ProviderHandler mockProviderHandler;

    private TypeHandler<ColorShift> handler;

    @BeforeEach
    public void setUp() {
        hydrateMockHandler(mockProviderHandler, pairOf(mockAmountProvider, AMOUNT_PROVIDER));

        handler = new ColorShiftHandler(mockProviderHandler);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new ColorShiftHandler(null));
    }

    @Test
    public void testWriteBrightnessShift() {
        var shift = brightnessShift(mockAmountProvider, OVERRIDES);

        var output = handler.write(shift);

        assertEquals(writtenValue(TYPE_BRIGHTNESS), output);
        verify(mockProviderHandler, once()).write(mockAmountProvider);
    }

    @Test
    public void testWriteRotationShift() {
        var shift = rotationShift(mockAmountProvider, OVERRIDES);

        var output = handler.write(shift);

        assertEquals(writtenValue(TYPE_ROTATION), output);
        verify(mockProviderHandler, once()).write(mockAmountProvider);
    }

    @Test
    public void testWriteComponentShift() {
        var shift = colorComponentShift(mockAmountProvider, OVERRIDES, COMPONENT);

        var output = handler.write(shift);

        assertEquals(writtenValue(TYPE_COMPONENT_SHIFT), output);
        verify(mockProviderHandler, once()).write(mockAmountProvider);
    }

    @Test
    public void testWriteWithIllegalArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
        assertThrows(IllegalArgumentException.class,
                () -> handler.write(colorComponentShift(null, OVERRIDES, COMPONENT)));
        assertThrows(IllegalArgumentException.class,
                () -> handler.write(colorComponentShift(mockAmountProvider, OVERRIDES, null)));
    }

    @Test
    public void testReadBrightnessShift() {
        var output = handler.read(writtenValue(TYPE_BRIGHTNESS));

        assertNotNull(output);
        assertInstanceOf(BrightnessShift.class, output);
        assertSame(mockAmountProvider, output.AMOUNT_PROVIDER);
        assertEquals(OVERRIDES, output.OVERRIDES_PRIOR_SHIFTS_OF_SAME_TYPE);
        verify(mockProviderHandler, once()).read(AMOUNT_PROVIDER);
    }

    @Test
    public void testReadRotationShift() {
        var output = handler.read(writtenValue(TYPE_ROTATION));

        assertNotNull(output);
        assertInstanceOf(ColorRotationShift.class, output);
        assertSame(mockAmountProvider, output.AMOUNT_PROVIDER);
        assertEquals(OVERRIDES, output.OVERRIDES_PRIOR_SHIFTS_OF_SAME_TYPE);
        verify(mockProviderHandler, once()).read(AMOUNT_PROVIDER);
    }

    @Test
    public void testReadComponentShift() {
        var output = handler.read(writtenValue(TYPE_COMPONENT_SHIFT));

        assertNotNull(output);
        assertInstanceOf(ColorComponentIntensityShift.class, output);
        assertSame(mockAmountProvider, output.AMOUNT_PROVIDER);
        assertEquals(OVERRIDES, output.OVERRIDES_PRIOR_SHIFTS_OF_SAME_TYPE);
        assertEquals(COMPONENT, ((ColorComponentIntensityShift) output).COLOR_COMPONENT);
        verify(mockProviderHandler, once()).read(AMOUNT_PROVIDER);
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
        assertThrows(IllegalArgumentException.class,
                () -> handler.read(writtenValue("not a valid type")));
    }

    private String writtenValue(String type) {
        return String.format("{\"type\":\"%s\",\"amt\":\"%s\",\"overrides\":%s" +
                        (type.equals(TYPE_COMPONENT_SHIFT) ? ",\"comp\":%d" : "") + "}",
                type, AMOUNT_PROVIDER, OVERRIDES, COMPONENT.getValue());
    }
}
