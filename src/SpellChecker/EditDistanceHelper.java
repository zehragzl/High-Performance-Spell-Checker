package SpellChecker;

import DataStructures.GTUHashSet;
import DataStructures.GTUArrayList;
import DataStructures.GTUIterator;

public class EditDistanceHelper {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    // Generates all possible words with an edit distance of 1 (deletion, substitution, insertion, transposition)
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

    // Generates suggestions by considering both edit distance 1 and 2 (if necessary)
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
