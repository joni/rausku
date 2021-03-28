package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.lighting.LightSource;

import java.util.Collection;
import java.util.List;

public interface SceneDefinition {

    Camera getCamera();

    Collection<LightSource> getLights();

    List<SceneObjectInstance> getObjects();
}