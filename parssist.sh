#!/bin/bash

# Default values
INPUT_GRAMMAR="../../input/input.gra"
INPUT_LEXER="../../input/input.lex"
OUTPUT_LANGUAGE="java"
OUTPUT_COMMAND="codegeneration"
ALGORITHM="auto"
NAME="Parser"
MODULE="example.Parser"
MODE="1" # 0 - read directly from input, 1 - read from file

# If the tasks are with gradle, then the following command should be used:
GRADLE_TASK="run"


# Parse command line arguments
while [[ $# -gt 0 ]]; do 
    case "$1" in
        -g|--grammar)
            INPUT_GRAMMAR="$2"
            shift 2
            ;;
        -l|--lexer)
            INPUT_LEXER="$2"
            shift 2
            ;;
        -lang|--language)
            OUTPUT_LANGUAGE="$2"
            shift 2
            ;;
        -a|--algorithm)
            ALGORITHM="$2"
            shift 2
            ;;
        -n|--name)
            NAME="$2"
            shift 2
            ;;
        -m|--module)
            MODULE="$2"
            shift 2
            ;;
        -mode|--mode)
            MODE="$2"
            shift 2
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
    shift
done

# Run the code generation command depending on the output language
case "$OUTPUT_LANGUAGE" in
    java)
        # Run gradle to generate the Java code
        cd java
        ./gradlew "$GRADLE_TASK" --args="$OUTPUT_COMMAND $INPUT_LEXER $INPUT_GRAMMAR $NAME $MODULE $ALGORITHM $MODE"
        ;;
    *)
        echo "Unknown output language: $OUTPUT_LANGUAGE"
        exit 1
        ;;
esac