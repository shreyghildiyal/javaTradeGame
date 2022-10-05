package engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;


import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

// import org.lwjgl.glfw.GLFWErrorCallback;




public class GameWindow {

    final private int swapInterval = 1;
    
    private int height;
    private int width;
    private String name;

    private long glfwWindow;
    private static GameWindow window;

    private GameWindow() {
        height = 200;
        width = 300;
        name = "Hello";
    }

    public static GameWindow getGameWindow() {
        if (window == null) {
            window = new GameWindow();
        }
        return window;
    }

    public void run() {
        System.out.println("Run method of game window started");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free Error callback
        glfwTerminate();
        GLFWErrorCallback cb = glfwSetErrorCallback(null);
        if (cb != null) {
            cb.free();
        }

    }

    private void init() {
        // Setup error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if ( !glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.name, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create glfw window");
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(swapInterval);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Important for some bindings
        GL.createCapabilities();
    }

    private void loop() {
        System.out.println("Starting the game loop");
        while  (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(1.0f, 0.5f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}
