#! /usr/bin/env bash

unzip glad.zip -d glad

cd glad/src || exit

clang -c gl.c
clang -fPIC gl.o -nostartfiles -shared -o libglad.so