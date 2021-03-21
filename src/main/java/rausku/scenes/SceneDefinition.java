package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.lighting.LightSource;

import java.util.Collection;
import java.util.List;

public interface SceneDefinition {

    default Camera getCamera() {
        return Camera.initialCamera();
    }

    Collection<LightSource> getLights();

    List<SceneObjectInstance> getObjects();
}