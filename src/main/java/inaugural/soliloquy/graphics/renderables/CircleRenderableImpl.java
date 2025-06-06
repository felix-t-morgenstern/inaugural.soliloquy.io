package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.CircleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

public class CircleRenderableImpl extends AbstractRenderable implements CircleRenderable {
    private ProviderAtTime<Vertex> centerProvider;
    private ProviderAtTime<Float> widthProvider;
    private ProviderAtTime<Color> colorProvider;

    public CircleRenderableImpl(ProviderAtTime<Vertex> centerProvider,
                                ProviderAtTime<Float> widthProvider,
                                ProviderAtTime<Color> colorProvider,
                                int z, UUID uuid, RenderableStack containingStack) {
        super(z, uuid, containingStack);
        setCenterProvider(centerProvider);
        setWidthProvider(widthProvider);
        setColorProvider(colorProvider);
    }

    @Override
    public ProviderAtTime<Vertex> getCenterProvider() {
        return centerProvider;
    }

    @Override
    public void setCenterProvider(ProviderAtTime<Vertex> centerProvider)
            throws IllegalArgumentException {
        this.centerProvider = Check.ifNull(centerProvider, "centerProvider");
    }

    @Override
    public ProviderAtTime<Float> getWidthProvider() {
        return widthProvider;
    }

    @Override
    public void setWidthProvider(ProviderAtTime<Float> widthProvider)
            throws IllegalArgumentException {
        this.widthProvider = Check.ifNull(widthProvider, "widthProvider");
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
