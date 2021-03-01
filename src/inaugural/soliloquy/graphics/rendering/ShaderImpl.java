package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.Shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderImpl implements Shader {
    private int _program;

    private final int NO_PROGRAM = 0;

    private final int STATUS_OK = 1;

    private final int INVALID_LOCATION = -1;

    private final int ATTRIBUTE_LOCATION_VERTICES = 0;
    private final int ATTRIBUTE_LOCATION_UV_COORDS = 1;

    private final Map<String,Integer> UNIFORM_LOCATIONS = new HashMap<>();

    public ShaderImpl(String filename) {
        Check.ifNullOrEmpty(filename, "filename");

        _program = glCreateProgram();

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, createShader(filename + ".vs"));
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) != STATUS_OK) {
            throw new RuntimeException(glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, createShader(filename + ".fs"));
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != STATUS_OK) {
            throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
        }

        glAttachShader(_program, vertexShader);
        glAttachShader(_program, fragmentShader);

        glBindAttribLocation(_program, ATTRIBUTE_LOCATION_VERTICES, "vertices");
        glBindAttribLocation(_program, ATTRIBUTE_LOCATION_UV_COORDS, "uvCoords");

        glLinkProgram(_program);
        if (glGetProgrami(_program, GL_LINK_STATUS) != STATUS_OK) {
            throw new RuntimeException(glGetProgramInfoLog(_program));
        }

        glValidateProgram(_program);
        if (glGetProgrami(_program, GL_VALIDATE_STATUS) != STATUS_OK)
        {
            throw new RuntimeException(glGetProgramInfoLog(_program));
        }
    }

    private static String createShader(String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public void bind() {
        glUseProgram(_program);
    }

    public void unbind() {
        glUseProgram(NO_PROGRAM);
    }

    private void setUniform(String name, Consumer<Integer> action) {
        int location;
        if (!UNIFORM_LOCATIONS.containsKey(name)) {
            location = glGetUniformLocation(_program, name);
            if (location == INVALID_LOCATION) {
                throw new RuntimeException("ShaderImpl.setUniform: Invalid location name (" + name +
                        ")");
            }
            UNIFORM_LOCATIONS.put(name, location);
        }
        else {
            location = UNIFORM_LOCATIONS.get(name);
        }
        action.accept(location);
    }

    @Override
    public void setUniform(String name, float f) throws IllegalArgumentException {
        setUniform(name, location -> glUniform1f(location, f));
    }

    @Override
    public void setUniform(String name, float f1, float f2) throws IllegalArgumentException {
        setUniform(name, location -> glUniform2f(location, f1, f2));
    }

    @Override
    public void setUniform(String name, float f1, float f2, float f3)
            throws IllegalArgumentException {
        setUniform(name, location -> glUniform3f(location, f1, f2, f3));

    }

    @Override
    public void setUniform(String name, float f1, float f2, float f3, float f4)
            throws IllegalArgumentException {
        setUniform(name, location -> glUniform4f(location, f1, f2, f3, f4));
    }

    @Override
    public String getInterfaceName() {
        return Shader.class.getCanonicalName();
    }
}
