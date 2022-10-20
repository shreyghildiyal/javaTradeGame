package engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import utils.Time;

// import org.lwjgl.glfw.GLFWErrorCallback;

public class GameWindow {

    final private int swapInterval = 1;

    private int height;
    private int width;
    private String name;

    private long glfwWindow;
    private static GameWindow window;

    private static Scene currentScene = null;

    private float r, g, b, a;
    private final float baseR = 1.0f;
    private final float baseG = 0.75f;
    private final float baseB = 0.5f;

    private GameWindow() {
        height = 200;
        width = 300;
        name = "Hello";
        r = baseR;
        g = baseG;
        b = baseB;
        a = 1;
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
        if (!glfwInit()) {
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

        // Setup MouseCallbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // Setup Keyboard callbacks
        glfwSetKeyCallback(glfwWindow, KeyboardListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(swapInterval);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Important for some bindings
        GL.createCapabilities();

        currentScene = new LevelEditorScene();
    }

    private void loop() {
        System.out.println("Starting the game loop");

        // float startTime = Time.getTimeNanoSeconds();
        float prevStart = Time.getTimeNanoSeconds();
        float deltaTime;

        while (!glfwWindowShouldClose(glfwWindow)) {

            deltaTime = Time.getTimeNanoSeconds() - prevStart;
            prevStart = Time.getTimeNanoSeconds();
            // Poll events
            glfwPollEvents();
            currentScene.update(deltaTime);

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            // if (KeyboardListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            // r = r * 0.9f;
            // g = g * 0.9f;
            // b = b * 0.9f;
            // }

            // if (r < 0.001) {
            // r = 1;
            // g = 0.5f;
            // }

            glfwSwapBuffers(glfwWindow);

        }
    }

    public static void changeScene(int newScene) {

        if (newScene == 0) {
            currentScene = new LevelEditorScene();
            currentScene.init();
        } else if (newScene == 1) {
            currentScene = new LevelScene();
            currentScene.init();
        } else {
            assert false;
        }

    }

    public void setBrightness(float f) {
        r = baseR * f;
        g = baseG * f;
        b = baseB * f;
    }
}
