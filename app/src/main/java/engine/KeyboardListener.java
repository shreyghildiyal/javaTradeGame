package engine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardListener {

    private static KeyboardListener instance;
    private boolean keyPressed[] = new boolean[512]; // assuming we dont have more keys than this in the keyboard

    private KeyboardListener() {

    }

    private static KeyboardListener get() {
        if (instance == null) {
            instance = new KeyboardListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < get().keyPressed.length) {
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true;
            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false;
            } else {
                System.out.println("No idea what the action could be");
            }
        }
    }

    public static boolean isKeyPressed(int key) {

        return get().keyPressed[key];

    }
}
