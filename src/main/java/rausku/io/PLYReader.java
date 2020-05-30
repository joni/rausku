package rausku.io;

import rausku.geometry.Polygon;
import rausku.math.Vec;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PLYReader implements Closeable, AutoCloseable {

    private final Scanner fileScanner;
    List<Vec> vertices = new ArrayList<>();
    List<Vec> normals = new ArrayList<>();
    List<Polygon> faces = new ArrayList<>();

    int vertexCount;
    int faceCount;

    public PLYReader(InputStream inputStream) {
        fileScanner = new Scanner(inputStream);
    }

    public List<Polygon> read() throws IOException {
        String headerStart = fileScanner.nextLine();
        if (!headerStart.equals("ply")) {
            throw new IOException("file format");
        }
        String line;
        do {
            line = fileScanner.nextLine();
            Scanner scanner = new Scanner(line);
            String token = scanner.next();
            switch (token) {
                case "format" -> parseFormat(scanner);
                case "element" -> parseElement(scanner);
                case "property" -> parseProperty(scanner);
            }

        } while (!line.equals("end_header"));

        for (int i = 0; i < vertexCount; i++) {
            line = fileScanner.nextLine();
            parseVertex(line);
        }

        for (int i = 0; i < faceCount; i++) {
            line = fileScanner.nextLine();
            parseFace(line);
        }

        return faces;
    }

    private void parseFormat(Scanner scanner) throws IOException {
        String format = scanner.next();
        String version = scanner.next();

        if (!format.equals("ascii") && version.equals("1.0")) {
            throw new IOException("Unsupported format " + format + " version " + version);
        }
    }

    private void parseFace(String line) {
        String[] split = line.split("\\s+");
        int v1 = Integer.parseInt(split[1]);
        int v2 = Integer.parseInt(split[2]);
        int v3 = Integer.parseInt(split[3]);
        faces.add(new Polygon(vertices.get(v3), vertices.get(v2), vertices.get(v1)));
    }

    private void parseVertex(String line) {
        String[] split = line.split("\\s+");
        float x = Float.parseFloat(split[0]);
        float y = Float.parseFloat(split[1]);
        float z = Float.parseFloat(split[2]);
        vertices.add(Vec.point(x, y, z));
    }

    private void parseElement(Scanner scanner) {
        String elementType = scanner.next();
        int elementCount = scanner.nextInt();
        switch (elementType) {
            case "vertex":
                vertexCount = elementCount;
            case "face":
                faceCount = elementCount;
        }
    }

    private void parseProperty(Scanner scanner) {
        // let's ignore for now
    }

    @Override
    public void close() throws IOException {
        fileScanner.close();
    }
}
