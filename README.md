# 🔍 High-Performance Spell Checker

A high-performance spell checking application implemented in Java with custom data structures and advanced edit distance algorithms.

## 📌 Overview

This project implements a spell checker from scratch using custom-built hash-based data structures and edit distance algorithms. The system is optimized for fast dictionary lookups and intelligent word suggestions.

## ✨ Key Features

- **Custom Data Structures**: Implemented from scratch without using Java Collections
  - `GTUHashMap`: Hash table with quadratic probing collision resolution
  - `GTUHashSet`: Set implementation built on custom HashMap
  - `GTUArrayList`: Dynamic array implementation
  - `GTUIterator`: Custom iterator interface

- **Advanced Spell Checking**:
  - Edit distance-based word suggestions (distance 1 and 2)
  - Support for special characters (@, ., -, !, ?, +)
  - Intelligent word validation with pattern matching
  - Up to 10,000 suggestions per query

- **Performance Optimizations**:
  - Warm-up mechanism for JIT optimization
  - Dynamic rehashing with 0.5 load factor
  - Prime-sized hash table capacities
  - Collision tracking and analysis
  - Memory usage monitoring

## 🏗️ Architecture

### Data Structures Package (`DataStructures/`)

#### GTUHashMap
- **Collision Resolution**: Quadratic probing
- **Hash Function**: Bit manipulation for reduced clustering
- **Load Factor**: 0.5 (rehashes when exceeded)
- **Capacity**: Prime numbers for better distribution
- **Features**: Lazy deletion, collision counting

#### GTUHashSet
- Built on top of GTUHashMap
- O(1) average case for add, remove, contains operations
- Iterator support for traversal

#### GTUArrayList
- Dynamic resizing array
- Generic type support
- Standard list operations

### Spell Checker Package (`SpellChecker/`)

#### SpellChecker
- Dictionary loading from file
- Interactive spell checking mode
- Performance metrics (lookup time, memory usage)
- Word validation with regex patterns

#### EditDistanceHelper
- **Edit Distance 1 Operations**:
  - Deletion: Remove one character
  - Substitution: Replace with alphabet letters
  - Insertion: Add alphabet letters at any position
  - Transposition: Swap adjacent characters (for words ≤ 6 chars)

- **Edit Distance 2**: Generated from edit distance 1 results
- Duplicate prevention with seen-words tracking

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Make (optional, for using Makefile)

### Building the Project

Using Make:
```bash
make build
```

Manual compilation:
```bash
javac -d build @sources.txt
```

### Running the Application

Using Make:
```bash
make run
```

Manual execution:
```bash
java -cp build SpellChecker.SpellChecker
```

## 📖 Usage

1. **Start the application**: The program loads the dictionary and displays memory statistics
2. **Enter words**: Type any word to check spelling
3. **Get suggestions**: For misspelled words, receive intelligent suggestions
4. **Exit**: Type `exitprogram` to quit

### Example Session

```
Total collisions during dictionary load: 1234
Approximate memory used: 45.67 MB

Enter a word (or type 'exitprogram' to quit): hello
Correct.
Lookup and suggestion took 0.12 ms

Enter a word (or type 'exitprogram' to quit): helo
Incorrect.
Suggestions: hello, helot, halo, hero
Lookup and suggestion took 2.34 ms
```

## 🎯 Performance Metrics

The application tracks and displays:
- **Collision Count**: Number of hash collisions during dictionary loading
- **Memory Usage**: Approximate memory consumption
- **Lookup Time**: Time taken for word checking and suggestion generation
- **Warm-up**: Pre-optimization of hash structure and edit distance calculations

## 🧪 Technical Highlights

### Hash Function Design
```java
private int hash(K key) {
    int h = key.hashCode();
    h ^= (h >>> 16);        // Mix high bits
    h *= 0x45d9f3b;         // Multiply by prime
    h ^= (h >>> 16);        // Mix again
    return (h & 0x7fffffff) % capacity;
}
```

### Quadratic Probing
- Uses `k += 2` increment pattern
- Ensures all table positions are reachable
- Reduces clustering compared to linear probing

### Edit Distance Algorithm
- Generates all possible single-edit variations
- Filters against dictionary for valid words
- Extends to distance 2 when necessary
- Optimized with early termination

## 📊 Project Structure

```
High-Performance-Spell-Checker/
├── src/
│   ├── DataStructures/
│   │   ├── Entry.java
│   │   ├── GTUArrayList.java
│   │   ├── GTUHashMap.java
│   │   ├── GTUHashSet.java
│   │   └── GTUIterator.java
│   └── SpellChecker/
│       ├── EditDistanceHelper.java
│       └── SpellChecker.java
├── build/                  # Compiled classes
├── dictionary.txt          # Word dictionary
├── makefile               # Build automation
└── sources.txt            # Source file list
```

## 🔧 Configuration

- **Dictionary File**: `dictionary.txt` (one word per line)
- **Initial Capacity**: 101 (prime number)
- **Load Factor**: 0.5
- **Max Suggestions**: 10,000
- **Transposition Limit**: Words ≤ 6 characters

## 📝 Implementation Details

### Word Validation
- Must contain at least one letter
- Allowed characters: letters, digits, @, ., -, ,, !, ?, +
- Case-insensitive matching

### Memory Management
- Lazy deletion in hash table
- Garbage collection before memory measurement
- Efficient rehashing with prime capacities

### Performance Optimization
- Warm-up phase with 20 sample words
- JIT compiler optimization
- Efficient string operations with StringBuilder

## 🎓 Educational Value

This project demonstrates:
- Custom data structure implementation
- Hash table design and collision resolution
- Edit distance algorithms
- Performance optimization techniques
- Memory management in Java
- Generic programming
- Iterator pattern implementation

## 📄 License

This project was developed as an educational assignment.

## 👤 Author

**Zehra Güzel**

## 🙏 Acknowledgments

- Dictionary source: Standard English word list
- Algorithm references: Edit distance and hash table literature
