package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.Renderer;

public abstract class AbstractRenderer<TRenderable extends Renderable>
        extends HasOneGenericParam<TRenderable>
        implements Renderer<TRenderable> {
    private Long _mostRecentTimestamp;

    protected AbstractRenderer(TRenderable archetype) {
        super(archetype);
    }

    // TODO: Use logic from tools package to determine calling class name
    protected void validateTimestamp(long timestamp, String className) {
        if (_mostRecentTimestamp != null) {
            if (timestamp < _mostRecentTimestamp) {
                throw new IllegalArgumentException(
                        className + ".render: outdated timestamp provided");
            }
        }
        _mostRecentTimestamp = timestamp;
    }
}
