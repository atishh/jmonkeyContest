package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ScreenAppState extends AbstractAppState implements ScreenController {
    private SimpleApplication app;
    private AssetManager assetManager;
    private InputManager inputManager;
    private ViewPort guiViewPort;
    private AudioRenderer audioRenderer;
    private AppStateManager stateManager;
    private Nifty nifty;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.guiViewPort = this.app.getGuiViewPort();
        this.audioRenderer = this.app.getAudioRenderer();
        this.app.getFlyByCamera().setEnabled(false);
        initNifty();
    }

    @Override
    public void update(float tpf) {
    }
    
    private NiftyJmeDisplay niftyDisplay;
  
    private void initNifty() {
        niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/startScreen.xml", "start", this);

        guiViewPort.addProcessor(niftyDisplay);
    }

    GameAppState gamePlayAppState;
    
    public void menuStartGame() {
        gamePlayAppState = new GameAppState();
        stateManager.attach(gamePlayAppState);
    }

    public void menuRestartGame() {
        gamePlayAppState.worldAppState.initializeState();
    }

    public void menuQuitGame() {
        app.stop();
    }

    public void setWinScreen() {
        nifty.gotoScreen("win");
    }
    
    public void setLostScreen() {
        nifty.gotoScreen("lost");
    }

    public void setInGameGUI() {
        nifty.gotoScreen("ingame");
    }

    public void updateGameGUIChicken(int currentChicken, int maxChicken) {
        Screen screen = nifty.getScreen("ingame");
        Element ChickenFront = screen.findElementByName("ChickenFront");
        TextRenderer RendererFront = ChickenFront.getRenderer(TextRenderer.class);
        RendererFront.setText("" + currentChicken + " / " + maxChicken);
    }

    public void updateGameGUITime(int currentChicken, int maxChicken) {
        Screen screen = nifty.getScreen("ingame");
        Element ChickenFront = screen.findElementByName("TimeFront");
        TextRenderer RendererFront = ChickenFront.getRenderer(TextRenderer.class);
        RendererFront.setText("" + currentChicken + " / " + maxChicken);
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
}
