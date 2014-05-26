package mygame;

import com.jme3.app.SimpleApplication;

public class Main extends SimpleApplication {
    
    public static void main(String[] args) {
       Main app = new Main();
       app.start();  
    }
    
    @Override
    public void simpleInitApp() {       
        setDisplayFps(false);
        setDisplayStatView(false);

        ScreenAppState startScreenAppState = new ScreenAppState();
        stateManager.attach(startScreenAppState);
        inputManager.setCursorVisible(true);
    }
   
}