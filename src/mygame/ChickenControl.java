package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class ChickenControl extends AbstractControl {

    private GameAppState gamePlayAppState;
    private AudioNode chickenSound;

    public ChickenControl(AssetManager assetManager, GameAppState gamePlayAppState) {
        this.gamePlayAppState = gamePlayAppState;
        chickenSound = new AudioNode(assetManager, "Sounds/chicken.wav", false);
        chickenSound.setPositional(false);
        chickenSound.setVolume(0.6f);
    }

    public void capture() {
        spatial.removeFromParent();
        gamePlayAppState.catchChicken();
        chickenSound.play();
    }

    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(0, 0.5f * tpf, 0);
        Vector3f camLoc = gamePlayAppState.app.getCamera().getLocation();
        Vector3f spatialLoc = spatial.getLocalTranslation();

        if (spatialLoc.distance(camLoc) < 6) {
            spatial.move(0.5f * tpf, 0, 0.5f * tpf);
        }
        if (spatialLoc.distance(camLoc) < 2) {
            capture();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial mySpatial) {
        spatial = mySpatial;
    }
}
