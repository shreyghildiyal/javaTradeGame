package engine;

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


    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.posX = 0;
        this.posY = 0;
        this.lastX = 0;
        this.lastY = 0;

        this.isDragging = false;
    }

    public MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }
}
