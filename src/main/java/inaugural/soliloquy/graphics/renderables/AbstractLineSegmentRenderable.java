package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.LineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

abstract class AbstractLineSegmentRenderable
        extends AbstractRenderable
        implements LineSegmentRenderable {
    private ProviderAtTime<Vertex> vertex1Provider;
    private ProviderAtTime<Vertex> vertex2Provider;
    private ProviderAtTime<Float> thicknessProvider;
    private ProviderAtTime<Color> colorProvider;

    public AbstractLineSegmentRenderable(ProviderAtTime<Vertex> vertex1Provider,
                                         ProviderAtTime<Vertex> vertex2Provider,
                                         ProviderAtTime<Float> thicknessProvider,
                                         ProviderAtTime<Color> colorProvider,
                                         int z, UUID uuid,
                                         RenderableStack containingStack) {
        super(z, uuid, containingStack);
        setVertex1Provider(vertex1Provider);
        setVertex2Provider(vertex2Provider);
        setThicknessProvider(thicknessProvider);
        setColorProvider(colorProvider);
    }

    @Override
    public ProviderAtTime<Vertex> getVertex1Provider() {
        return vertex1Provider;
    }

    @Override
    public void setVertex1Provider(ProviderAtTime<Vertex> vertex1Provider)
            throws IllegalArgumentException {
        this.vertex1Provider = Check.ifNull(vertex1Provider, "vertex1Provider");
    }

    @Override
    public ProviderAtTime<Vertex> getVertex2Provider() {
        return vertex2Provider;
    }

    @Override
    public void setVertex2Provider(ProviderAtTime<Vertex> vertex2Provider)
            throws IllegalArgumentException {
        this.vertex2Provider = Check.ifNull(vertex2Provider, "vertex2Provider");
    }

    @Override
    public ProviderAtTime<Float> getThicknessProvider() {
        return thicknessProvider;
    }

    @Override
    public void setThicknessProvider(ProviderAtTime<Float> thicknessProvider)
            throws IllegalArgumentException {
        this.thicknessProvider = Check.ifNull(thicknessProvider, "thicknessProvider");
    }

    @Override
    public ProviderAtTime<Color> getColorProvider() {
        return colorProvider;
    }

    @Override
    public void setColorProvider(ProviderAtTime<Color> colorProvider)
            throws IllegalArgumentException {
        this.colorProvider = Check.ifNull(colorProvider, "colorProvider");
    }
}
