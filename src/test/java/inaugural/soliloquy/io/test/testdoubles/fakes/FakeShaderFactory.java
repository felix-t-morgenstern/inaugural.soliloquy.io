package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.factories.ShaderFactory;

import static org.mockito.Mockito.mock;

public class FakeShaderFactory implements ShaderFactory {
    public Shader MostRecentlyCreated;

    @Override
    public Shader make(String s) throws IllegalArgumentException {
        return MostRecentlyCreated = mock(Shader.class);
    }
}
