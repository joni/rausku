package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.lighting.LightSource;
import rausku.math.Vec;

import java.util.Collection;
import java.util.List;

public interface SceneDefinition {

    default Camera getCamera() {
        return Camera.initialCamera();
    }

    default Collection<LightSource> getLights() {
        return List.of(
                new DirectionalLight(Vec.unit(1, -1, -.5f), Color.of(.8f, .8f, .7f)),
                new AmbientLight(Color.of(.2f, .25f, .3f)));
    }

    List<SceneObjectInstance> getObjects();
}