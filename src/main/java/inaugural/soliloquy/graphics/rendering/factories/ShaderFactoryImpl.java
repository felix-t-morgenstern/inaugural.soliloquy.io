package inaugural.soliloquy.graphics.rendering.factories;

import inaugural.soliloquy.graphics.rendering.ShaderImpl;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;

public class ShaderFactoryImpl implements ShaderFactory {
    @Override
    public Shader make(String filenamePrefix) throws IllegalArgumentException {
        return new ShaderImpl(filenamePrefix);
    }
}
