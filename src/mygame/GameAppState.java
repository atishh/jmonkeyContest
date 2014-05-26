package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GameAppState extends AbstractAppState implements ScreenController {

    public SimpleApplication app;
    private InputManager inputManager;
    private AppStateManager stateManager;
    public BuildingAppState worldAppState;
    private ScreenAppState startScreenAppState;
    private int nMaxChicken = 13;
    private int currentChicken = 0;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.inputManager = this.app.getInputManager();
        this.stateManager = stateManager;

        startScreenAppState = stateManager.getState(ScreenAppState.class);

        worldAppState = new BuildingAppState();
        stateManager.attach(worldAppState);

        inputManager.setCursorVisible(true);
    }

    public float nTotalTime = 0; 
    
    @Override
    public void update(float tpf) {
        startScreenAppState.updateGameGUIChicken(currentChicken, nMaxChicken);
        startScreenAppState.updateGameGUITime((int)nTotalTime, 30);
        nTotalTime += tpf;
        System.out.println(nTotalTime);
        if(nTotalTime > 30)
        {
            nTotalTime = 0;
            currentChicken = 0;
            worldAppState.printLost();
        }
    }

    public void catchChicken() {
        currentChicken++;
        if (currentChicken >= nMaxChicken) {
            currentChicken = 0;
            nTotalTime = 0;
            worldAppState.printCongrats();
        }
    }

    public void resetCurrentChicken() {
        currentChicken = 0;
    }

    public void endGame() {
        stateManager.detach(worldAppState);
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
}
