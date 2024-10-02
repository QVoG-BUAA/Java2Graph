package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtil {

    public static void writeFile(String FilePath, String str) {
        // ensure parent directory exists
        Path parent = Path.of(FilePath).getParent();
        if (parent != null) {
            parent.toFile().mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(FilePath);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read file content into a string
    public static String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            // System.out.println(numRead);
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return fileData.toString();
    }
}
