package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;

public class FakeShaderFactory implements ShaderFactory {
    public Shader MostRecentlyCreated;

    @Override
    public Shader make(String s) throws IllegalArgumentException {
        return MostRecentlyCreated = new FakeShader();
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
