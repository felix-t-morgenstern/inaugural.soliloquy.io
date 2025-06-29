package inaugural.soliloquy.io.graphics.rendering.factories;

import inaugural.soliloquy.io.graphics.rendering.ShaderImpl;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.factories.ShaderFactory;

public class ShaderFactoryImpl implements ShaderFactory {
    @Override
    public Shader make(String filenamePrefix) throws IllegalArgumentException {
        return new ShaderImpl(filenamePrefix);
    }
}
