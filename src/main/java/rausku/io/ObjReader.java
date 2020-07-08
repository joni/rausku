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

    private final Scanner fileScanner;
    List<Vec> vertices = new ArrayList<>();
    List<Vec> normals = new ArrayList<>();
    List<Polygon> faces = new ArrayList<>();

    public ObjReader(InputStream inputStream) {
        fileScanner = new Scanner(inputStream);
    }

    public List<Polygon> read() {

        float x;
        float y;
        float z;

        while (fileScanner.hasNextLine()) {
            String command = fileScanner.next();
            String line = fileScanner.nextLine();
            Scanner scanner = new Scanner(line);

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
                    String as = scanner.next();
                    Vertex a = createVertex(as);
                    String bs = scanner.next();
                    Vertex b = createVertex(bs);
                    String cs = scanner.next();
                    Vertex c = createVertex(cs);
                    faces.add(new Polygon(a, b, c));
                    while (scanner.hasNext()) {
                        b = c;
                        cs = scanner.next();
                        c = createVertex(cs);
                        faces.add(new Polygon(a, b, c));
                    }
                    break;
                default:
                    // unknown
                    break;
            }
        }

        return faces;
    }

    private Vertex createVertex(String cs) {
        String[] split = cs.split("/");
        int i = Integer.parseInt(split[0]);
        int j = i;
        if (split.length > 2) {
            j = Integer.parseInt(split[2]);
        }
        return Vertex.of(vertices.get(i - 1), normals.get(j - 1));
    }

    @Override
    public void close() throws IOException {
        fileScanner.close();
    }
}
