package rausku.geometry;


import org.junit.jupiter.api.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class GroupTest {

    @Test
    void testIntercept() {

        List<SceneObject> objs = List.of(new Cube(), QuadraticForm.createSphere(Vec.origin(), 1));

        Group group = new Group();
        for (SceneObject obj : objs) {
            group.addObject(obj);
        }

        Ray ray = Ray.fromOriginDirection(Vec.point(10, 0, 0), Vec.of(-1, 0, 0));
//        assertTrue(group.getIntercept(ray).isValid());
    }

    @Test
    void testBoundingBox() {
        List<SceneObject> objs = List.of(new Cube(), QuadraticForm.createSphere(Vec.origin(), 1));
        Group group = new Group();
        for (SceneObject obj : objs) {
            group.addObject(obj);
        }

//        assertNotNull(group.getBoundingBox());
    }
}