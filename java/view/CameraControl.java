package view;


import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

public final class CameraControl {
    public CameraControl(Camera cam, Spatial target, InputManager inputManager){
        ChaseCamera camera = new ChaseCamera(cam, target, inputManager);
        cam.setLocation(new Vector3f(0.0f, 200.0f, 0.0f));
        camera.setDefaultDistance(200);
        camera.setMaxDistance(300);
        camera.setMinDistance(100);
        camera.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        camera.setInvertVerticalAxis(true);
    }
}
