package SpellChecker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import DataStructures.GTUHashSet;
import DataStructures.GTUArrayList;
import DataStructures.GTUIterator;

public class SpellChecker {
    private GTUHashSet<String> dictionary;

    // Constructor to initialize the dictionary as an empty GTUHashSet
    public SpellChecker() {
        dictionary = new GTUHashSet<>();
    }

    // Method to load words from a dictionary file into the GTUHashSet
    public void loadDictionary(String filepath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        while ((line = reader.readLine()) != null) {
            dictionary.add(line.trim().toLowerCase());
        }
        reader.close();
    }
        
    public boolean isValidWordWithSpecialChars(String word) {
        // The word must contain at least one letter and can only consist of letters, digits, and specific special characters (e.g., @ . - , ? !)
        return word.matches(".*\\p{L}.*") && word.matches("[\\p{L}\\p{N}@\\.\\-\\,\\!?\\+]*");
    }


    // Method to check if a word is valid based on dictionary lookup and special character validation
    public boolean checkWord(String word) {
        if (!isValidWordWithSpecialChars(word)) {
            System.out.println("Invalid word. Only letters, digits, and certain special characters (@ . -) are allowed, and the word must contain at least one letter.");
            return false;
        }
        return dictionary.contains(word.toLowerCase()); // Check the word in dictionary
    }

    // Method to run the interactive spell checker for user input
    public void runInteractive() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a word (or type 'exitprogram' to quit): ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Exit condition
            if (input.equals("exitprogram")) {
                System.out.println("Exiting SpellChecker. Goodbye!");
                break;
            }

            long t0 = System.nanoTime();

            // Check if the word is valid or incorrect
            if (checkWord(input)) {
                System.out.println("Correct.");
            } else {
                if (!isValidWordWithSpecialChars(input)) {
                    continue; // Skip suggestion generation for invalid word
                }
                System.out.println("Incorrect.");
                GTUArrayList<String> sugg = EditDistanceHelper.generateSuggestions(input, dictionary);
                System.out.print("Suggestions: ");
                if (sugg.size() == 0) {
                    System.out.println("No suggestions found.");
                } else {
                    for (int i = 0; i < sugg.size(); i++) {
                        System.out.print(sugg.get(i));
                        if (i < sugg.size() - 1) System.out.print(", ");
                    }
                    System.out.println();
                }
            }

            long t1 = System.nanoTime();
            System.out.printf("Lookup and suggestion took %.2f ms%n", (t1 - t0) / 1e6);
        }
    }

    // Warm-up method to optimize the dictionary hash structure and the edit distance suggestion process
    private void warmUp() {
        int count = 0;
        GTUIterator<String> it = dictionary.keyIterator();
        while (it.hasNext() && count < 20) {
            String word = it.next();
            dictionary.contains(word); // Optimize hash structure for lookup
            EditDistanceHelper.generateSuggestions(word, dictionary); // Optimize edit-distance function for word suggestions
            count++;
        }
    }

    // Main method to run the spell checker application
    public static void main(String[] args) {
        try {
            SpellChecker checker = new SpellChecker();
            checker.loadDictionary("dictionary.txt");
            System.out.println("Total collisions during dictionary load: " + checker.dictionary.getCollisionCount());

            // Trigger garbage collection to ensure accurate memory usage measurement
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
            System.out.printf("Approximate memory used: %.2f MB%n", memoryUsed / (1024.0 * 1024.0));

            checker.warmUp(); // Pre-load the dictionary and optimize the system before user interaction

            checker.runInteractive(); // Start the interactive mode
        } catch (IOException e) {
            System.err.println("Failed to load dictionary: " + e.getMessage());
        }
    }
}