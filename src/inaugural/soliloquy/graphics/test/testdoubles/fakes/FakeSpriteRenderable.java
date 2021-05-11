package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;

public class FakeSpriteRenderable implements SpriteRenderable {
    public Sprite Sprite;
    public List<ColorShift> ColorShifts;
    public ProviderAtTime<FloatBox> RenderingAreaProvider;
    public ProviderAtTime<Float> BorderThicknessProvider;
    public ProviderAtTime<Color> BorderColorProvider;
    public int Z;
    public EntityUuid Uuid;

    public FakeSpriteRenderable(Sprite sprite, List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider, EntityUuid uuid) {
        Sprite = sprite;
        ColorShifts = colorShifts;
        RenderingAreaProvider = renderingAreaProvider;
        BorderThicknessProvider = borderThicknessProvider;
        BorderColorProvider = borderColorProvider;
        Uuid = uuid;
    }

    public FakeSpriteRenderable(Sprite sprite, List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                EntityUuid id) {
        Sprite = sprite;
        ColorShifts = colorShifts;
        RenderingAreaProvider = renderingAreaProvider;
        Z = z;
        BorderThicknessProvider = new FakeStaticProviderAtTime<>(null);
        BorderColorProvider = new FakeStaticProviderAtTime<>(null);
        Uuid = id;
    }

    @Override
    public Sprite sprite() {
        return Sprite;
    }

    @Override
    public ProviderAtTime<Float> borderThicknessProvider() {
        return BorderThicknessProvider;
    }

    @Override
    public ProviderAtTime<Color> borderColorProvider() {
        return BorderColorProvider;
    }

    @Override
    public boolean capturesMouseEvents() {
        return false;
    }

    @Override
    public void click() throws UnsupportedOperationException {

    }

    @Override
    public void mouseOver() throws UnsupportedOperationException {

    }

    @Override
    public void mouseLeave() throws UnsupportedOperationException {

    }

    @Override
    public List<ColorShift> colorShifts() {
        return ColorShifts;
    }

    @Override
    public ProviderAtTime<FloatBox> renderingAreaProvider() {
        return RenderingAreaProvider;
    }

    @Override
    public int z() {
        return Z;
    }

    @Override
    public void delete() {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public EntityUuid uuid() {
        return Uuid;
    }
}
