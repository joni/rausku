package rausku.io;

import rausku.geometry.Polygon;
import rausku.geometry.Vertex;
import rausku.math.Vec;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObjReader implements Closeable, AutoCloseable {

    private final Scanner scanner;

    public ObjReader(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    public List<Polygon> read() {

        List<Vec> vertices = new ArrayList<>();
        List<Vec> normals = new ArrayList<>();
        List<Polygon> faces = new ArrayList<>();

        float x;
        float y;
        float z;

        while (scanner.hasNextLine()) {
            String command = scanner.next();

            switch (command) {
                case "#":
                    // comment
                    break;
                case "v":
                    // vertex coordinates
                    x = scanner.nextFloat();
                    y = scanner.nextFloat();
                    z = scanner.nextFloat();
                    vertices.add(Vec.point(x, y, z));
                    break;
                case "vn":
                    // vertex normal
                    x = scanner.nextFloat();
                    y = scanner.nextFloat();
                    z = scanner.nextFloat();
                    normals.add(Vec.of(x, y, z));
                    break;
                case "f":
                    // face
                    int a = scanner.nextInt() - 1;
                    int b = scanner.nextInt() - 1;
                    int c = scanner.nextInt() - 1;
                    faces.add(new Polygon(
                            Vertex.of(vertices.get(a), normals.get(a)),
                            Vertex.of(vertices.get(b), normals.get(b)),
                            Vertex.of(vertices.get(c), normals.get(c)))
                    );
                    break;
                default:
                    // unknown
                    break;
            }

            scanner.nextLine();
        }

        return faces;
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
