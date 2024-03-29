import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FilesOperator {
    Path path = Paths.get("./class_info/");
    private void createDirectory(){
        try {
            Files.createDirectory(path);
        }  catch (Exception e) {
        }
    }

    default <T> void saveDataToFile(String fileName, T listClass) throws IOException {
        createDirectory();
        ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(path.resolve(fileName + ".txt").toFile()))
        );

        out.writeObject(listClass);
        out.flush();
        out.close();
    }

    default void loadDataFromFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(path.resolve(fileName + ".txt").toFile()))
        )) {
            System.out.println(in.readObject());
        } catch (EOFException E) {
            // Service exception to final read data file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void variableUpdate() throws IOException, ClassNotFoundException;

}
