package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.LineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

abstract class AbstractLineSegmentRenderable
        extends AbstractRenderable
        implements LineSegmentRenderable {
    private ProviderAtTime<Pair<Float, Float>> _vertex1LocationProvider;
    private ProviderAtTime<Pair<Float, Float>> _vertex2LocationProvider;
    private ProviderAtTime<Float> _thicknessProvider;
    private ProviderAtTime<Color> _colorProvider;

    public AbstractLineSegmentRenderable(ProviderAtTime<Pair<Float, Float>>
                                                 vertex1LocationProvider,
                                         ProviderAtTime<Pair<Float, Float>>
                                                 vertex2LocationProvider,
                                         ProviderAtTime<Float> thicknessProvider,
                                         ProviderAtTime<Color> colorProvider,
                                         int z, UUID uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer) {
        super(z, uuid, updateZIndexInContainer, removeFromContainer);
        setVertex1LocationProvider(vertex1LocationProvider);
        setVertex2LocationProvider(vertex2LocationProvider);
        setThicknessProvider(thicknessProvider);
        setColorProvider(colorProvider);
    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex1LocationProvider() {
        return _vertex1LocationProvider;
    }

    @Override
    public void setVertex1LocationProvider(ProviderAtTime<Pair<Float, Float>> vertex1LocationProvider)
            throws IllegalArgumentException {
        _vertex1LocationProvider = Check.ifNull(vertex1LocationProvider, "vertex1LocationProvider");
    }

    @Override
    public ProviderAtTime<Pair<Float, Float>> getVertex2LocationProvider() {
        return _vertex2LocationProvider;
    }

    @Override
    public void setVertex2LocationProvider(ProviderAtTime<Pair<Float, Float>> vertex2LocationProvider)
            throws IllegalArgumentException {
        _vertex2LocationProvider = Check.ifNull(vertex2LocationProvider, "vertex2LocationProvider");
    }

    @Override
    public ProviderAtTime<Float> getThicknessProvider() {
        return _thicknessProvider;
    }

    @Override
    public void setThicknessProvider(ProviderAtTime<Float> thicknessProvider)
            throws IllegalArgumentException {
        _thicknessProvider = Check.ifNull(thicknessProvider, "thicknessProvider");
    }

    @Override
    public ProviderAtTime<Color> getColorProvider() {
        return _colorProvider;
    }

    @Override
    public void setColorProvider(ProviderAtTime<Color> colorProvider)
            throws IllegalArgumentException {
        _colorProvider = Check.ifNull(colorProvider, "colorProvider");
    }
}
