# Toy language Compiler

A compiler for the Toy language, made with Java CUP and JFlex. This implementation results in the creation of an executable file given a compliant Toy file.

## Build

### Requirements

- Java 11
- Maven
- Clang

### Build instructions

To build this project, just type

> mvn package

A jar *with all dependencies* will be created under the `release/` directory.  

> Note that code generation for *Javacup* and *jflex* is disabled by default. If you want to manually generate the sources for Javacup and Jflex, type *"mvn generate-sources -P source-gen"* and the .java classes will be placed under *target* folder.

A compiled version of this compiler can be found under the `release/` directory. The release is composed of:  

- ***Toy2C.jar***: a pre-compiled version of this software
- ***Toy2C***: an executable script that, given a .toy file, compiles it into a .c file and then call `clang` to output a machine-code executable
- ***install_native_libraries***: an executable script that create a hard link under `/usr/local` directory for the `toy_functions.h` library, a native library needed in the compilation phase.  

An example of the *Toy2C* usage:

> ./Toy2C FILENAME 

The output file is placed in the same directory of the input file.

## Documentation

You can find full documentation under the `doc/` directory.  
A pre-compiled `pdf` file is already present, but is possible to compile the documentation manually typing

> ./compile_doc

## Authors
- *Luigi Crisci*  
- *Alessio Cuccurullo*