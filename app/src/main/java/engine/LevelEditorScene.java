package engine;

import static org.lwjgl.opengl.GL40.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import rederer.Shader;

public class LevelEditorScene extends Scene {

    final int coordSize = 3;
    final int colourSize = 4;

    private Shader defaultShader;

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

        // Shader dummyShader = new Shader("assets/shaders/default.glsl");
        // dummyShader.compile();

        init();
    }

    @Override
    public void update(float deltaTime) {

        // glUseProgram(defaultShader);
        defaultShader.use();

        // Bind the VOA we want to use
        glBindVertexArray(voaID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        // glUseProgram(0);
        defaultShader.detach();
    }

    @Override
    public void init() {

        defaultShader = new Shader("assets/shaders/default.glsl");

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
}
