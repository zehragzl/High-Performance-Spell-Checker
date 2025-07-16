SRC = src
BUILD = build

collect:
	find $(SRC) -name "*.java" > sources.txt

build: collect
	javac -d $(BUILD) @sources.txt

run: build
	java -cp $(BUILD) SpellChecker.SpellChecker

clean:
	rm -rf $(BUILD) sources.txt
