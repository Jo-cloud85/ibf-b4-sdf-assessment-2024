package sg.edu.nus.iss;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 1. javac -d bin src/sg/edu/nus/iss/*
// 2. java -cp bin sg.edu.nus.iss.Main Rush2.csv


public class Main {

    private static List<String> singlePokeStack;
    private static Map<Integer, List<String>> pokeMap;

    private static final String ONE = "(1)";
    private static final String TWO = "(2)";
    private static final String THREE = "(3)";
    private static final String FOUR = "(4)";
    private static final String QUIT = "q";

    public static void main(String[] args) throws Exception {

        // Run Your Code here
        String csvFile = args[0];

        if (csvFile != null) {
            // run file.
        } else {
            System.out.println("Missing csv file ...");
        }

        printHeader();
        
        FileService fileService = new FileService();
        List<String> allPokeStacks = fileService.ReadCSV(csvFile);
        // System.out.println(allPokeStacks);
        String[] allPokeStacksArr = allPokeStacks.toString().split("\\|, ");
        pokeMap = new LinkedHashMap<>();
        for (int i = 0; i < allPokeStacksArr.length; i++) {
            if (allPokeStacksArr[i].startsWith("[")) {
                allPokeStacksArr[i] = allPokeStacksArr[i].substring(1);
            }

            if (allPokeStacksArr[i].endsWith("|]")) {
                allPokeStacksArr[i] = allPokeStacksArr[i].substring(0, allPokeStacksArr[i].length() - 3);
            }

            String[] singlePokeStackArr = allPokeStacksArr[i].split(",");
            singlePokeStack = new LinkedList<>(List.of(singlePokeStackArr));
            pokeMap.put(i + 1, singlePokeStack);
        }

        //System.out.println(pokeMap);

        Console console = System.console();
        String input = "";
        input = console.readLine("Enter your selection > ");

        boolean quit = false;

        while (!quit) {
            if (input.startsWith("q")) {
                printExitMessage();
                quit = true;
            }

            else if (input.equals("1")) {
                String moreUserInput = console.readLine("Display the list of unique Pokemon in stack (1 - 8) > \n");
                printUniquePokemonStack(Integer.parseInt(moreUserInput));
                continue;
            }

            else if (input.equals("2")) {
                String moreUserInput = console.readLine("Search for the next occurence of 5 stars Pokemon in all stacks based on entered Pokemon > \n");
                printNext5StarsPokemon(moreUserInput);
                //continue;
            }

            else if (input.equals("4")) {
                String moreUserInput1 = console.readLine("Create a new Pokemon stack and save to a new file > " + "\n");
                String moreUserInput2 = console.readLine("Enter filename to save (e.g. path/filename.csv)" + "\n");
            
                Scanner scanner1 = new Scanner(moreUserInput1);
                Scanner scanner2 = new Scanner(moreUserInput2);
                // scanner.useDelimiter(",");
                String line1;
                String line2;
                while (scanner1.hasNextLine() && scanner2.hasNextLine()) {
                    line1 = scanner1.nextLine();
                    line2 = scanner2.nextLine();
                    savePokemonStack(line1, line2);
                }
                scanner1.close();
                scanner2.close();
                continue;
            }

            else {
                pressAnyKeyToContinue();
            }
        }
    }

    public static void clearConsole() throws IOException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Task 1
    public static void pressAnyKeyToContinue() {
        // your code here
        // printHeader();
        Console console = System.console();
        //String input = "";
        String input = console.readLine("Invalid command. Press any key to continue...");
        //System.out.println("Invalid command. Press any key to continue...");
        String[] commandArr = new String[]{"1", "2", "3", "4", "q"};
        boolean found = Arrays.stream(commandArr).anyMatch(x -> input.equals(x));
        if (!found) {
            printHeader();
        }
    }

    // Task 1
    public static void printHeader() {
        // Task 1 - your code here
        String menuScreen = """
                Welcome to Pokemon Gaole Legend 4 Rush 2

                (1) View unique list of Pokemon in the selected stack
                (2) Find next 5 stars Pokemon occurence
                (3) Create new Pokemon stack and save (append) to csv file
                (4) Print distinct Pokemon and cards count
                (q) to exit the program
                """;
        System.out.println(menuScreen);
    }

    // Task 1
    public static void printExitMessage() {
        // Task 1 - your code here
        String exitMessage = """

                Thank you for using the program...
                Hope to see you soon...
                """;
        System.out.println(exitMessage);
    }

    // Task 1
    public static void savePokemonStack(String pokemonStack, String filename) throws IOException {
        // Task 1 - your code here
        FileService fileService = new FileService();
        fileService.writeAsCSV(pokemonStack, filename);
    }

    // Task 2
    public static void printUniquePokemonStack(Integer stack) {
        // Task 2 - your code here
        if (stack < 1 || stack > 8) {
            System.out.println("Invalid stack number");
            pressAnyKeyToContinue();
        } else {
            List<String> requestedPokeStackList = pokeMap.get(stack);
                
            List<String> tempList = new LinkedList<>();
            
            for (int i = 0; i< requestedPokeStackList.size(); i++) {
                if (!tempList.contains(requestedPokeStackList.get(i))) {
                    tempList.add(requestedPokeStackList.get(i));
                }
            }

            for (int i=0; i<tempList.size(); i++) {
                System.out.println((i+1) + " ==> " + tempList.get(i));
            }
        }
    }

    // Task 2
    public static void printNext5StarsPokemon(String enteredPokemon) {
        // Task 2 - your code here
        for (Map.Entry<Integer, List<String>> entry : pokeMap.entrySet()) {
            List<String> singleStackList = entry.getValue();
            Integer stackNum = entry.getKey();
            if (!singleStackList.contains(enteredPokemon)) {
                System.out.println("Set " + stackNum + "\n" + enteredPokemon + " not found in this set.");
                System.out.println("-".repeat(40));
            } else {
                // check if 5 star pokemon exist
                for (String poke : singleStackList) {
                    int pokeStar = Integer.parseInt(poke.split("\\*")[0]);
                    if (pokeStar == 5) {
                        int index = singleStackList.indexOf(poke);
                        int numLeft = singleStackList.size() - index + 1;
                        System.out.println(poke + ">>>" + numLeft + " cards to go.");
                    }
                }
               
                System.out.println("Set " + stackNum + "\nNo 5 stars Pokemon found subsequently in the stack.");
                System.out.println("---------");
            }
        }
    }

    // Task 2
    public static void printPokemonCardCount() {
        // Task 2 - your code here
    }

}
