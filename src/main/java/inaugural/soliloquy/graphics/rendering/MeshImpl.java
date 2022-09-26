package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import org.lwjgl.BufferUtils;
import soliloquy.specs.graphics.rendering.Mesh;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class MeshImpl implements Mesh {

    private int _vertexArrayObject;

    private int _verticesId;
    private int _uvId;

    private final int POSITIONS = 0;
    private final int TEXTURE_COORDS = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private final int NO_BUFFER = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private final int NO_VERTEX_ARRAY = 0;

    @SuppressWarnings("ConstantConditions")
    public MeshImpl(float[] vertices, float[] uvCoordinates) {
        Check.ifNull(vertices, "vertices");
        Check.ifNull(uvCoordinates, "uvCoordinates");

        _vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(_vertexArrayObject);

        _verticesId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, _verticesId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, vertices.length / 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        _uvId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, _uvId);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(uvCoordinates), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        unbind();
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    @Override
    public void render() {
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    @Override
    public void bind() {
        glBindVertexArray(_vertexArrayObject);
        glEnableVertexAttribArray(POSITIONS);
        glEnableVertexAttribArray(TEXTURE_COORDS);

        glBindBuffer(GL_ARRAY_BUFFER, _verticesId);
        glVertexAttribPointer(POSITIONS, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, _uvId);
        glVertexAttribPointer(TEXTURE_COORDS, 2, GL_FLOAT, false, 0, 0);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, NO_BUFFER);
        glDisableVertexAttribArray(POSITIONS);
        glDisableVertexAttribArray(TEXTURE_COORDS);
        glBindVertexArray(NO_VERTEX_ARRAY);
    }

    @Override
    public void cleanUp() {
        glDeleteVertexArrays(_vertexArrayObject);
        glDeleteBuffers(_verticesId);
    }

    @Override
    public String getInterfaceName() {
        return Mesh.class.getCanonicalName();
    }
}
