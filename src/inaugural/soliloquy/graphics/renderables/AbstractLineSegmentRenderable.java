package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.LineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

abstract class AbstractLineSegmentRenderable
        extends AbstractRenderable
        implements LineSegmentRenderable {
    private ProviderAtTime<Vertex> _vertex1Provider;
    private ProviderAtTime<Vertex> _vertex2Provider;
    private ProviderAtTime<Float> _thicknessProvider;
    private ProviderAtTime<Color> _colorProvider;

    public AbstractLineSegmentRenderable(ProviderAtTime<Vertex> vertex1Provider,
                                         ProviderAtTime<Vertex> vertex2Provider,
                                         ProviderAtTime<Float> thicknessProvider,
                                         ProviderAtTime<Color> colorProvider,
                                         int z, UUID uuid,
                                         Consumer<Renderable> updateZIndexInContainer,
                                         Consumer<Renderable> removeFromContainer) {
        super(z, uuid, updateZIndexInContainer, removeFromContainer);
        setVertex1Provider(vertex1Provider);
        setVertex2Provider(vertex2Provider);
        setThicknessProvider(thicknessProvider);
        setColorProvider(colorProvider);
    }

    @Override
    public ProviderAtTime<Vertex> getVertex1Provider() {
        return _vertex1Provider;
    }

    @Override
    public void setVertex1Provider(ProviderAtTime<Vertex> vertex1Provider)
            throws IllegalArgumentException {
        _vertex1Provider = Check.ifNull(vertex1Provider, "vertex1Provider");
    }

    @Override
    public ProviderAtTime<Vertex> getVertex2Provider() {
        return _vertex2Provider;
    }

    @Override
    public void setVertex2Provider(ProviderAtTime<Vertex> vertex2Provider)
            throws IllegalArgumentException {
        _vertex2Provider = Check.ifNull(vertex2Provider, "vertex2Provider");
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
