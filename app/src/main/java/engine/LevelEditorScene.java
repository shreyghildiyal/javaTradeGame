package engine;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float remainingSceneDuration = 0;
    private final float maxSceneDuration = 2000000000;

    public LevelEditorScene() {
        remainingSceneDuration = maxSceneDuration;
        System.out.println("inside LevelEditorScene");
    }

    @Override
    public void update(float deltaTime) {
        
        if (!changingScene && KeyboardListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene) {
            
            if (remainingSceneDuration > 0) {
                remainingSceneDuration -= deltaTime;
                GameWindow.getGameWindow().setBrightness(remainingSceneDuration/maxSceneDuration);
            } else {
                remainingSceneDuration = maxSceneDuration;
                System.out.println("");
                GameWindow.changeScene(1);
            }
        }
    }
    
}
