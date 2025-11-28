package com.library.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PersistenceManager {
    public static <K, V extends Serializable> void saveMap(Map<K, V> map, Path file) throws IOException {
        Files.createDirectories(file.getParent());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
            oos.writeObject(map);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<?, ?> loadMap(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.toFile()))) {
            return (Map<?, ?>) ois.readObject();
        }
    }
}
