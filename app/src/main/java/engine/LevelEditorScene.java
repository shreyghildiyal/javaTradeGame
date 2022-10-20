package engine;

import static org.lwjgl.opengl.GL40.*;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n"
            + "layout (location=0) in vec3 attrPos;\n"
            + "layout (location=1) in vec4 attrColour;\n"
            + "\n"
            + "out vec4 fragmentColour;\n"
            + "\n"
            + "void main()  {\n"
            + "    fragmentColour = attrColour;\n"
            + "    gl_Position = vec4(attrPos, 1.0);\n"
            + "}";
    private String fragmentShaderSrc = "#version 330 core\n"
            + "\n"
            + "in vec4 fragmentColour;\n"
            + "\n"
            + "out vec4 colour;\n"
            + "\n"
            + "void main() {\n"
            + "    colour = fragmentColour;\n"
            + "}";

    private int vertexID;
    private int fragmentID;
    private int shaderProgram;

    final int coordSize = 3;
    final int colourSize = 4;

    private float[] vertexArray = {
            // first three numbers in row are coordinates, the next four are colours
            // x,y,z,r,g,b,a
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top left
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, // top right
    };

    // IMPORTANT: Must be in counter clockwise order
    private int[] elementArray = {
            0, 3, 2, // upper right triangle
            0, 2, 1 // bottom left triangle
    };

    private int voaID;
    private int vboID;
    private int eboID;

    public LevelEditorScene() {

    }

    @Override
    public void update(float deltaTime) {

        System.out.println("Level Editor Scene Update");

        glUseProgram(shaderProgram);

        // Bind the VOA we want to use
        glBindVertexArray(voaID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }

    @Override
    public void init() {

        // Compile and link shaders
        loadVertexShader();
        loadFragmentShader();
        linkShaders();

        // Generate VAO VBO EBO and send to GPU
        voaID = glGenVertexArrays();
        glBindVertexArray(voaID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add the vertex attribute pointers

        int vertexSizeBytes = Float.BYTES * (coordSize + colourSize);
        glVertexAttribPointer(0, coordSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes, coordSize * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    private void linkShaders() {
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // check for linking errors
        int success = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Shader linking failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false;
        }
    }

    private void loadVertexShader() {
        // Load and compile the vertex shaders
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // pass shader source to GPU
        glShaderSource(vertexID, vertexShaderSrc);
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

    private void loadFragmentShader() {
        // Load and compile the fragment shaders
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass shader source to GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
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
}
