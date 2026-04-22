package SpellChecker;

import java.io.IOException;
import DataStructures.GTUHashSet;
import DataStructures.GTUArrayList;
import DataStructures.GTUIterator;

/**
 * Performance benchmark suite for the High-Performance Spell Checker.
 *
 * <p>This class measures and reports key performance metrics including
 * dictionary load time, memory consumption, hash collision statistics,
 * lookup latency for both correct and misspelled words, and edit distance
 * generation throughput.</p>
 *
 * <p>Usage: {@code java -cp build SpellChecker.SpellCheckerBenchmark}</p>
 *
 * @author Zehra Güzel
 * @version 1.0
 */
public class SpellCheckerBenchmark {

    /**
     * Runs the full benchmark suite and prints results to standard output.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║   High-Performance Spell Checker — Benchmark Suite      ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            // --- Dictionary Load Benchmark ---
            SpellChecker checker = new SpellChecker();
            long loadStart = System.nanoTime();
            checker.loadDictionary("dictionary.txt");
            long loadEnd = System.nanoTime();
            double loadTimeMs = (loadEnd - loadStart) / 1e6;

            // --- Memory Measurement ---
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            Thread.sleep(100); // Allow GC to complete
            long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
            double memoryMB = memoryUsed / (1024.0 * 1024.0);

            // Access dictionary via reflection-free approach: reload to get collision count
            // We use a local GTUHashSet for controlled benchmarking
            GTUHashSet<String> dict = new GTUHashSet<>();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("dictionary.txt"));
            String line;
            int wordCount = 0;
            while ((line = reader.readLine()) != null) {
                dict.add(line.trim().toLowerCase());
                wordCount++;
            }
            reader.close();

            System.out.println("┌─────────────────────────────────────────────────────┐");
            System.out.println("│  DICTIONARY LOAD                                    │");
            System.out.println("├─────────────────────────────────────────────────────┤");
            System.out.printf("│  Load Time:          %10.2f ms                   │%n", loadTimeMs);
            System.out.printf("│  Dictionary Size:    %,10d words                │%n", wordCount);
            System.out.printf("│  Total Collisions:   %,10d                      │%n", dict.getCollisionCount());
            System.out.printf("│  Memory Used:        %10.2f MB                   │%n", memoryMB);
            System.out.println("└─────────────────────────────────────────────────────┘");
            System.out.println();

            // --- Warm-up Phase ---
            System.out.println("  Warming up JIT compiler...");
            GTUIterator<String> warmIt = dict.keyIterator();
            int warmCount = 0;
            while (warmIt.hasNext() && warmCount < 20) {
                String w = warmIt.next();
                dict.contains(w);
                EditDistanceHelper.generateSuggestions(w, dict);
                warmCount++;
            }
            System.out.println("  Warm-up complete (20 words processed).");
            System.out.println();

            // --- Lookup Benchmark: Correct Words ---
            String[] correctWords = {"hello", "world", "algorithm", "structure", "performance",
                                     "dictionary", "computer", "science", "program", "language"};
            long totalCorrectNs = 0;
            for (String word : correctWords) {
                long t0 = System.nanoTime();
                dict.contains(word);
                long t1 = System.nanoTime();
                totalCorrectNs += (t1 - t0);
            }
            double avgCorrectMs = (totalCorrectNs / (double) correctWords.length) / 1e6;

            // --- Lookup Benchmark: Misspelled Words ---
            String[] misspelledWords = {"helo", "wrold", "algoritm", "strucutre", "perfomance",
                                        "dictionray", "compter", "scince", "progam", "languge"};
            long totalMisspelledNs = 0;
            for (String word : misspelledWords) {
                long t0 = System.nanoTime();
                dict.contains(word);
                EditDistanceHelper.generateSuggestions(word, dict);
                long t1 = System.nanoTime();
                totalMisspelledNs += (t1 - t0);
            }
            double avgMisspelledMs = (totalMisspelledNs / (double) misspelledWords.length) / 1e6;

            System.out.println("┌─────────────────────────────────────────────────────┐");
            System.out.println("│  LOOKUP PERFORMANCE                                 │");
            System.out.println("├─────────────────────────────────────────────────────┤");
            System.out.printf("│  Avg Lookup (correct):     %8.4f ms              │%n", avgCorrectMs);
            System.out.printf("│  Avg Lookup (misspelled):  %8.4f ms              │%n", avgMisspelledMs);
            System.out.println("└─────────────────────────────────────────────────────┘");
            System.out.println();

            // --- Edit Distance Generation Benchmark ---
            String testWord = "performance";
            long ed1Start = System.nanoTime();
            GTUHashSet<String> ed1Set = EditDistanceHelper.generateEditDistance1Set(testWord);
            long ed1End = System.nanoTime();
            double ed1Ms = (ed1End - ed1Start) / 1e6;

            // Count ED1 candidates
            int ed1Count = ed1Set.size();

            long ed2Start = System.nanoTime();
            GTUArrayList<String> suggestions = EditDistanceHelper.generateSuggestions(testWord, dict);
            long ed2End = System.nanoTime();
            double ed2Ms = (ed2End - ed2Start) / 1e6;

            System.out.println("┌─────────────────────────────────────────────────────┐");
            System.out.println("│  EDIT DISTANCE ANALYSIS (word: \"" + testWord + "\")       │");
            System.out.println("├─────────────────────────────────────────────────────┤");
            System.out.printf("│  ED1 Candidates:     %,10d                      │%n", ed1Count);
            System.out.printf("│  ED1 Generation:     %10.4f ms                  │%n", ed1Ms);
            System.out.printf("│  Full Suggestion:    %10.4f ms                  │%n", ed2Ms);
            System.out.printf("│  Suggestions Found:  %,10d                      │%n", suggestions.size());
            System.out.println("└─────────────────────────────────────────────────────┘");
            System.out.println();

            System.out.println("╔══════════════════════════════════════════════════════════╗");
            System.out.println("║   Benchmark Complete                                    ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");

        } catch (IOException e) {
            System.err.println("Failed to load dictionary: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Benchmark interrupted: " + e.getMessage());
        }
    }
}
