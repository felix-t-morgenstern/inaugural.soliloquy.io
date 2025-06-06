package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.Shader;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.opengl.GL20.*;

@SuppressWarnings("FieldCanBeLocal")
public class ShaderImpl implements Shader {
    private final int PROGRAM;

    private final int NO_PROGRAM = 0;

    private final int STATUS_OK = 1;

    private final int INVALID_LOCATION = -1;

    private final int ATTRIBUTE_LOCATION_VERTICES = 0;
    private final int ATTRIBUTE_LOCATION_UV_COORDS = 1;

    private final Map<String, Object> UNIFORM_VALUES = mapOf();

    private final Map<String, Integer> UNIFORM_LOCATIONS = mapOf();

    public ShaderImpl(String filename) {
        Check.ifNullOrEmpty(filename, "filename");

        PROGRAM = glCreateProgram();

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

        glAttachShader(PROGRAM, vertexShader);
        glAttachShader(PROGRAM, fragmentShader);

        glBindAttribLocation(PROGRAM, ATTRIBUTE_LOCATION_VERTICES, "vertices");
        glBindAttribLocation(PROGRAM, ATTRIBUTE_LOCATION_UV_COORDS, "uvCoords");

        glLinkProgram(PROGRAM);
        if (glGetProgrami(PROGRAM, GL_LINK_STATUS) != STATUS_OK) {
            throw new RuntimeException(glGetProgramInfoLog(PROGRAM));
        }

        glValidateProgram(PROGRAM);
        if (glGetProgrami(PROGRAM, GL_VALIDATE_STATUS) != STATUS_OK) {
            throw new RuntimeException(glGetProgramInfoLog(PROGRAM));
        }
    }

    private static String createShader(String filename) {
        var stringBuilder = new StringBuilder();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public void bind() {
        glUseProgram(PROGRAM);
    }

    public void unbind() {
        glUseProgram(NO_PROGRAM);
    }

    private void setUniform(String name, Consumer<Integer> action) {
        Check.ifNullOrEmpty(name, "name");
        int location;
        if (!UNIFORM_LOCATIONS.containsKey(name)) {
            location = glGetUniformLocation(PROGRAM, name);
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
        synchronized (this) {
            if (UNIFORM_VALUES.containsKey(name)) {
                if (UNIFORM_VALUES.get(name).equals(f)) {
                    return;
                }
            }
            setUniform(name, location -> glUniform1f(location, f));
            UNIFORM_VALUES.put(name, f);
        }
    }

    // NB: In this and the following method, I assume that a uniform's value type does not change.
    //     This is a valid assumption, so I am content letting the code break if the cast fails or
    //     if an index is out-of-bounds.
    @Override
    public void setUniform(String name, float f1, float f2) throws IllegalArgumentException {
        synchronized (this) {
            if (UNIFORM_VALUES.containsKey(name)) {
                float[] uniformValue = (float[]) UNIFORM_VALUES.get(name);
                if (uniformValue[0] == f1 && uniformValue[1] == f2) {
                    return;
                }
            }
            setUniform(name, location -> glUniform2f(location, f1, f2));
            UNIFORM_VALUES.put(name, new float[]{f1, f2});
        }
    }

    @Override
    public void setUniform(String name, float f1, float f2, float f3)
            throws IllegalArgumentException {
        synchronized (this) {
            if (UNIFORM_VALUES.containsKey(name)) {
                var uniformValue = (float[]) UNIFORM_VALUES.get(name);
                if (uniformValue[0] == f1 && uniformValue[1] == f2 && uniformValue[2] == f3) {
                    return;
                }
            }
            setUniform(name, location -> glUniform3f(location, f1, f2, f3));
            UNIFORM_VALUES.put(name, new float[]{f1, f2, f3});
        }
    }

    @Override
    public void setUniform(String name, float f1, float f2, float f3, float f4)
            throws IllegalArgumentException {
        synchronized (this) {
            if (UNIFORM_VALUES.containsKey(name)) {
                var uniformValue = (float[]) UNIFORM_VALUES.get(name);
                if (uniformValue[0] == f1 && uniformValue[1] == f2 && uniformValue[2] == f3 &&
                        uniformValue[3] == f4) {
                    return;
                }
            }
            setUniform(name, location -> glUniform4f(location, f1, f2, f3, f4));
            UNIFORM_VALUES.put(name, new float[]{f1, f2, f3, f4});
        }
    }

    // TODO: The logic here is duplicated from Shader; consider moving to Tools
    @Override
    public void setUniform(String name, Color color) throws IllegalArgumentException {
        Check.ifNull(color, "color");
        var rgba = color.getColorComponents(null);
        setUniform(name, rgba[0], rgba[1], rgba[2], color.getAlpha() / MAX_CHANNEL_VAL);
    }
}
