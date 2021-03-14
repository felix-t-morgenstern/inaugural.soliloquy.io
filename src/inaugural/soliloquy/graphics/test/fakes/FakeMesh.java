package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.Mesh;

public class FakeMesh implements Mesh {
    public float[] Vertices;
    public float[] UvCoordinates;

    public FakeMesh() {

    }

    public FakeMesh(float[] vertices, float[] uvCoordinates) {
        Vertices = vertices;
        UvCoordinates = uvCoordinates;
    }

    @Override
    public void render() {

    }

    @Override
    public void bind() {

    }

    @Override
    public void unbind() {

    }

    @Override
    public void cleanUp() {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
