package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;

public class FakeShaderFactory implements ShaderFactory {
    @Override
    public Shader make(String s) throws IllegalArgumentException {
        return new FakeShader();
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
