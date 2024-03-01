package sg.edu.nus.iss;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

public class FileService {
    public List<String> ReadCSV(String fullPathFilename) {
        // Task 1 - your code here
        List<String> allPokeStacks = new LinkedList<>();

        Path csvFilePath = Paths.get(fullPathFilename);
        
        if (!Files.exists(csvFilePath)) {
            System.out.println("Cannot find the file you are looking for. Please check your file path!");
        } else {
            try (BufferedReader br = Files.newBufferedReader(csvFilePath)){
                String line = "";
                while ((line = br.readLine()) != null) {
                    allPokeStacks.add(line + "|"); // add a separator between different stacks
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allPokeStacks;
    }

    public void writeAsCSV(String pokemons, String fullPathFilename) throws IOException {
        // Task 1 - your code here
        Path csvFilePath = Paths.get(fullPathFilename);

        if (!Files.exists(csvFilePath)) {
            Files.createFile(csvFilePath);
        } else {
            try (BufferedWriter bw = Files.newBufferedWriter(csvFilePath, StandardOpenOption.APPEND)) {
                bw.newLine();
                bw.write(pokemons);
                bw.newLine();
                bw.flush();
            } catch (Exception e) {
                System.out.println("Error writing to file....");
            }
        } 
    }
}
