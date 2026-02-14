#! /usr/bin/env bash

unzip glad.zip -d glad

cd glad/src || exit

clang -c gl.c
clang -fPIC gl.o -nostartfiles -shared -o libglad.so

cd ../../src/main/java || exit

gladLocation="$(realpath ./glad)"

if [ -d "$gladLocation" ]; then
    rm -rf "$gladLocation"
fi

jextract --include-dir ../../../glad/include/glad --output . --target-package glad --library glad --use-system-load-library ../../../glad/include/glad/gl.h