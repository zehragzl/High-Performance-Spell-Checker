SRC = src
BUILD = build
DOC = doc

# Find all Java source files
collect:
	find $(SRC) -name "*.java" > sources.txt

# Compile the project
build: collect
	javac -d $(BUILD) @sources.txt

# Run the interactive spell checker
run: build
	java -cp $(BUILD) SpellChecker.SpellChecker

# Run the performance benchmark suite
benchmark: build
	java -cp $(BUILD) SpellChecker.SpellCheckerBenchmark

# Generate Javadoc documentation
doc: collect
	javadoc -d $(DOC) -sourcepath $(SRC) -subpackages DataStructures:SpellChecker -windowtitle "High-Performance Spell Checker API" -doctitle "High-Performance Spell Checker" -header "Spell Checker v1.0" -quiet

# Build, benchmark, and generate docs
all: build benchmark doc

# Clean all generated files
clean:
	rm -rf $(BUILD) $(DOC) sources.txt

.PHONY: collect build run benchmark doc all clean
