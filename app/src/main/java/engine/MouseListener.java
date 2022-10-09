package engine;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private static MouseListener instance;

    private double scrollX;
    private double scrollY;
    private double posX;
    private double posY;
    private double lastX;
    private double lastY;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;


    public boolean isDragging() {
        return isDragging;
    }

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.posX = 0;
        this.posY = 0;
        this.lastX = 0;
        this.lastY = 0;

        this.isDragging = false;
    }

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        get().lastX = get().posX;
        get().lastY = get().posY;

        get().posX = posX;
        get().posY = posY;

        get().isDragging = get().mouseButtonPressed[0]; // assuming we only support dragging with button 0
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {

        System.out.println("The mouse button " + button + " had the action " + action);
        if (button < get().mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
        
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public static double getX() {
        return  get().posX;
    }

    public static double getY() {
        return  get().posY;
    }

    public static double getdX() {
        return get().posX - get().lastX;
    }

    public static double getdY() {
        return  get().posY - get().lastY;
    }

    public static double getScrollX() {
        return get().scrollX;
    }

    public static double getScrollY() {
        return get().scrollY;
    }

    public static boolean getIsdragging() {
        return get().isDragging;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
        
    }
}
