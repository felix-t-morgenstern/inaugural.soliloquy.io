package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.factories.ShaderFactory;

public class FakeShaderFactory implements ShaderFactory {
    public Shader MostRecentlyCreated;

    @Override
    public Shader make(String s) throws IllegalArgumentException {
        return MostRecentlyCreated = new FakeShader();
    }
}
