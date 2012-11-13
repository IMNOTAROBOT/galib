galib
=====

Genetic Algorithms library written in Java

### Usage
    java -jar GAlib.jar <algorithm> <input-file>

### List of Algorithms
 - TGA: Simple Genetic Algorithm

### Input file for SGA
A .properties file with the folowing keys:
 - P_c: Crossover probability
 - P_m: Mutation probability
 - M: Number or
 - G: Number of generations
 - L: Length of the Genotype string
 - Func: A fully-name java class that implements the IFitness interface
 - Verbose: Verbose level, 0 for none 1 for full verbose
