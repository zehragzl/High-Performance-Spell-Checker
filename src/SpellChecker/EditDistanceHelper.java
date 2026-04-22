package SpellChecker;

import DataStructures.GTUHashSet;
import DataStructures.GTUArrayList;
import DataStructures.GTUIterator;

/**
 * Utility class for generating spelling suggestions using edit distance algorithms.
 *
 * <p>This class implements a candidate generation approach to spell checking:
 * instead of computing pairwise Levenshtein distances against the entire
 * dictionary (which would be O(d × m × n) for d dictionary words), it
 * <b>generates</b> all possible single-edit variants of the input word and
 * checks each candidate against the dictionary in O(1) average time.</p>
 *
 * <h3>Supported Edit Operations (Distance 1)</h3>
 * <ul>
 *   <li><b>Deletion</b> — Remove one character at each position → n candidates</li>
 *   <li><b>Substitution</b> — Replace each character with 25 alternatives → 25n candidates</li>
 *   <li><b>Insertion</b> — Insert a-z at each position → 26(n+1) candidates</li>
 *   <li><b>Transposition</b> — Swap adjacent characters (words ≤ 6 chars) → n-1 candidates</li>
 * </ul>
 *
 * <h3>Edit Distance 2</h3>
 * <p>If insufficient suggestions are found at distance 1, the algorithm applies
 * the same four operations to each distance-1 candidate, yielding distance-2
 * suggestions. A seen-words set prevents duplicate entries.</p>
 *
 * <h3>Complexity</h3>
 * <ul>
 *   <li>ED1 generation: O(n) for a word of length n</li>
 *   <li>ED2 generation: O(n²) — ED1 applied to each ED1 result</li>
 *   <li>Dictionary filtering: O(1) per candidate (hash lookup)</li>
 * </ul>
 *
 * @author Zehra Güzel
 * @version 1.0
 * @see DataStructures.GTUHashSet
 */
public class EditDistanceHelper {

    /** The lowercase English alphabet used for generating edit candidates. */
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * Generates the set of all words within edit distance 1 of the given word.
     *
     * <p>This method produces candidates using four edit operations:
     * deletion, substitution, insertion, and transposition (the latter
     * only for words of 6 characters or fewer to bound computation).</p>
     *
     * <p><b>Total candidates ≈ 52n + 26</b> for a word of length n.</p>
     *
     * @param word the input word (must be lowercase and non-null)
     * @return a {@link GTUHashSet} containing all unique edit distance 1 candidates
     */
    public static GTUHashSet<String> generateEditDistance1Set(String word) {
        GTUHashSet<String> edits = new GTUHashSet<>();
        int len = word.length();

        // Deletion: Remove one character at a time
        for (int i = 0; i < len; i++) {
            StringBuilder sb = new StringBuilder(word);
            sb.deleteCharAt(i);  // Remove character at index i
            edits.add(sb.toString());
        }

        // Substitution: Replace each character with every letter from the alphabet
        for (int i = 0; i < len; i++) {
            char originalChar = word.charAt(i);
            for (char c : ALPHABET) {
                if (c != originalChar) {  // Ensure no substitution with the same character
                    StringBuilder sb = new StringBuilder(word);
                    sb.setCharAt(i, c);  // Replace character at index i
                    edits.add(sb.toString());
                }
            }
        }

        // Insertion: Insert each letter of the alphabet at every position
        for (int i = 0; i <= len; i++) {
            for (char c : ALPHABET) {
                StringBuilder sb = new StringBuilder(word);
                sb.insert(i, c);  // Insert character at index i
                edits.add(sb.toString());
            }
        }

        // Transposition (only for short words <= 6 characters): Swap adjacent characters
        if (len <= 6) {
            for (int i = 0; i < len - 1; i++) {
                if (word.charAt(i) != word.charAt(i + 1)) {
                    StringBuilder sb = new StringBuilder(word);
                    char tmp = sb.charAt(i);
                    sb.setCharAt(i, sb.charAt(i + 1));  // Swap characters at indices i and i+1
                    sb.setCharAt(i + 1, tmp);
                    edits.add(sb.toString());
                }
            }
        }

        return edits;
    }

    /**
     * Generates spelling suggestions for a misspelled word by checking
     * edit distance 1 and (if needed) edit distance 2 candidates against
     * the dictionary.
     *
     * <p>The method first collects all valid dictionary words within
     * edit distance 1. If fewer than {@code maxSuggestions} (10,000) are
     * found, it extends the search to edit distance 2 by applying ED1
     * to each ED1 candidate.</p>
     *
     * <p>A seen-words set ensures no duplicate suggestions are returned.</p>
     *
     * @param input      the misspelled word to generate suggestions for
     * @param dictionary the dictionary hash set to validate candidates against
     * @return a {@link GTUArrayList} of valid dictionary words similar to the input
     */
    public static GTUArrayList<String> generateSuggestions(String input, GTUHashSet<String> dictionary) {
        GTUArrayList<String> suggestions = new GTUArrayList<>();
        if (input == null || dictionary == null || dictionary.size() == 0) return suggestions;

        GTUHashSet<String> seenWords = new GTUHashSet<>();
        GTUHashSet<String> ed1Set = generateEditDistance1Set(input);  // Get all words with edit distance 1
        int maxSuggestions = 10000;  // Maximum number of suggestions to return

        // First, try to add all valid words with edit distance 1
        GTUIterator<String> it1 = ed1Set.keyIterator();
        while (it1.hasNext() && suggestions.size() < maxSuggestions) {
            String candidate = it1.next();
            // Only add the candidate if it's not already seen and exists in the dictionary
            if (!seenWords.contains(candidate) && dictionary.contains(candidate)) {
                suggestions.add(candidate);
                seenWords.add(candidate);  
            }
        }

        // If we've already reached the max suggestions, return early
        if (suggestions.size() >= maxSuggestions) return suggestions;

        // If necessary, generate edit distance 2 suggestions for words seen in edit distance 1
        GTUIterator<String> ed1It = ed1Set.keyIterator();
        while (ed1It.hasNext() && suggestions.size() < maxSuggestions) {
            String ed1Word = ed1It.next();
            if (seenWords.contains(ed1Word)) continue;  // Skip if we've already processed this word

            GTUHashSet<String> ed2Set = generateEditDistance1Set(ed1Word);  
            GTUIterator<String> ed2It = ed2Set.keyIterator();

            // Add valid edit distance 2 words to suggestions
            while (ed2It.hasNext() && suggestions.size() < maxSuggestions) {
                String candidate = ed2It.next();
                if (!seenWords.contains(candidate) && dictionary.contains(candidate)) {
                    suggestions.add(candidate);
                    seenWords.add(candidate); 
                }
            }
        }

        return suggestions;
    }
}
