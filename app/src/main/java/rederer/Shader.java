package rederer;

import static org.lwjgl.opengl.GL40.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Shader {

    /**
     *
     */
    private static final String FRAGMENT_BLOCK_KEY = "fragment";
    /**
     *
     */
    private static final String VERTEX_BLOCK_KEY = "vertex";
    /**
     *
     */
    private static final String SHADER_TYPE_LINE_REGEX = " *#type +([a-zA-Z]+)";
    private static final String SHADER_TYPE_LINE_SPLIT = "#type";
    private int shaderProgramID;
    // private int shaderID;
    final int coordSize = 3;
    final int colourSize = 4;

    // private String vertexSrc;
    // private String fragmentSrc;
    private String filepath;
    private int vertexID;
    Map<String, String> shaderBlocks;
    private int fragmentID;

    public Shader(String filepath) {
        this.filepath = filepath;

        File file = new File(filepath);

        shaderBlocks = getShaderBlockMap(file);

        if (!shaderBlocks.containsKey(VERTEX_BLOCK_KEY)) {
            log.error("The file " + this.filepath + " does not contain a vertex block");
            assert false;
        }
        if (!shaderBlocks.containsKey(FRAGMENT_BLOCK_KEY)) {
            log.error("The file " + this.filepath + " does not contain a fragment block");
            assert false;
            // System.exit(1);
        }

        init();
    }

    private void init() {

        // load, compile and link shaders
        loadVertexShader();
        loadFragmentShader();
        linkShaders();

        // // Generate VAO VBO EBO and send to GPU
        // voaID = glGenVertexArrays();
        // glBindVertexArray(voaID);

        // // Create a float buffer of vertices
        // FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        // vertexBuffer.put(vertexArray).flip();

        // // Create VBO
        // vboID = glGenBuffers();
        // glBindBuffer(GL_ARRAY_BUFFER, vboID);
        // glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // // Create the indices and upload
        // IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        // elementBuffer.put(elementArray).flip();

        // eboID = glGenBuffers();
        // glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        // glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // // add the vertex attribute pointers

        // int vertexSizeBytes = Float.BYTES * (coordSize + colourSize);
        // glVertexAttribPointer(0, coordSize, GL_FLOAT, false, vertexSizeBytes, 0);
        // glEnableVertexAttribArray(0);

        // glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes,
        // coordSize * Float.BYTES);
        // glEnableVertexAttribArray(1);
    }

    private void linkShaders() {
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // check for linking errors
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);

        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Shader linking failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false;
        }
    }

    private void loadFragmentShader() {
        // Load and compile the fragment shaders
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass shader source to GPU
        System.out.println("Fragment ID " + fragmentID);
        glShaderSource(fragmentID, this.shaderBlocks.get(FRAGMENT_BLOCK_KEY));
        glCompileShader(fragmentID);

        // cehck for errors in shader compilation
        int success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false;
        }
    }

    private void loadVertexShader() {
        // Load and compile the vertex shaders
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // pass shader source to GPU
        glShaderSource(vertexID, this.shaderBlocks.get(VERTEX_BLOCK_KEY));
        glCompileShader(vertexID);

        // cehck for errors in shader compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false;
        }
    }

    private Map<String, String> getShaderBlockMap(File file) {
        String line;
        Map<String, String> shaderBlocks = new HashMap<>();

        try (FileReader fr = new FileReader(file)) {
            try (BufferedReader br = new BufferedReader(fr)) {
                String blockName = null;
                List<String> blockStrings = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    // System.out.println("line " + line);
                    if (line.matches(SHADER_TYPE_LINE_REGEX)) {
                        if (blockName != null) {
                            shaderBlocks.put(blockName, String.join("\n", blockStrings));
                        }
                        blockName = getBlockName(line, shaderBlocks, blockName, blockStrings);

                    } else {
                        blockStrings.add(line);

                    }

                }
                if (blockName != null) {
                    shaderBlocks.put(blockName, String.join("\n", blockStrings));
                }
            } catch (IOException e) {
                log.error("Error with the BufferedReader", e);
                assert false;
            }
        } catch (IOException e) {
            log.error("Error with FileReader " + this.filepath, e);
            assert false;
        }

        return shaderBlocks;
    }

    private String getBlockName(String line, Map<String, String> shaderBlocks, String blockName,
            List<String> blockStrings) {

        blockName = null;
        blockStrings.clear();

        line = line.replaceAll(SHADER_TYPE_LINE_SPLIT, " ");
        line = line.trim();
        blockName = line;
        return blockName;
    }

    public void use() {
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
