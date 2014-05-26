package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.Random;

public class BuildingAppState extends AbstractAppState {
    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private GameAppState gamePlayAppState;
    private ScreenAppState startScreenAppState;
    private DirectionalLight sun;
    private AmbientLight ambient;
    public GameState gameState = GameState.INITIALIZE;
    private AppStateManager appStateManager;

    private enum GameState {
        IDLE, INITIALIZE;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.appStateManager = stateManager;
        this.app = (SimpleApplication) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();

        gamePlayAppState = stateManager.getState(GameAppState.class);
        startScreenAppState = stateManager.getState(ScreenAppState.class);

    }

    @Override
    public void update(float tpf) {
        if (gameState == GameState.INITIALIZE) {
            buildWorld();
            startScreenAppState.setInGameGUI();
            gameState = GameState.IDLE;
        }
    }

    public void buildWorld() {

        this.app.getFlyByCamera().setEnabled(true);
        this.app.getFlyByCamera().setMoveSpeed(10);
        inputManager.setCursorVisible(false);
        this.app.getCamera().setLocation(new Vector3f(-18,10,45));
        Spatial teapot = assetManager.loadModel("Models/newBuilding/Building.j3o");
        rootNode.attachChild(teapot);
        teapot.depthFirstTraversal(new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    ((Geometry) spatial).getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
                }
            }
        });


        teapot.depthFirstTraversal(new SceneGraphVisitor() {
            Random rand = new Random((long) 5f);

            public void visit(Spatial spatial) {
                if ((spatial != null) && (spatial.getName() != null)) {
                    System.out.println(spatial.getName());
                    if (spatial.getName().contains("chicken")) {
                        Spatial ChickenNode = assetManager.loadModel("Models/Chicken/Sphere.mesh.j3o");
                        rootNode.attachChild(ChickenNode);
                        Vector3f myVector = new Vector3f(spatial.getLocalTranslation());
                        myVector.setY(myVector.getY() + 0.7f);
                        ChickenNode.setLocalTranslation(myVector);
                        ChickenNode.setLocalScale(0.1f);

                        float randomRotate = (float) (-3.14f + (rand.nextFloat() * ((3.14f + 3.14f) + 1)));
                        ChickenNode.rotate(0f, randomRotate, 0f);
                        ChickenControl myWrenchControl = new ChickenControl(assetManager, gamePlayAppState);
                        myWrenchControl.setSpatial(ChickenNode);

                        ChickenNode.addControl(myWrenchControl);
                    }
                }
            }
        });
        initLight();
    }

    public void printCongrats() {
        stateDetached(appStateManager);
        startScreenAppState.setWinScreen();
        inputManager.setCursorVisible(true);
        gamePlayAppState.resetCurrentChicken();
    }

    public void printLost() {
        stateDetached(appStateManager);
        startScreenAppState.setLostScreen();
        inputManager.setCursorVisible(true);
        gamePlayAppState.resetCurrentChicken();
    }

    public void initializeState() {
        gamePlayAppState.nTotalTime = 0;
        gameState = GameState.INITIALIZE;
    }

    public void initLight() {
        ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White.mult(.6f));
        rootNode.addLight(ambient);
        sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -2.5f, -1f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        rootNode.detachAllChildren();
        rootNode.removeLight(sun);
        rootNode.removeLight(ambient);
    }
}
