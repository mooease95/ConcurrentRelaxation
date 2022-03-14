# ConcurrentRelaxation
Java implementation of the relaxation technique for solving equations. Now with new and improved concurrency functionality!

This program has a built in function of f(x,y) = x + y, which is used to generate and fill parameterised-sized 2d array, where x is the first dimension of the array and y is the second. It then creates a copy of the array, and fills all non-border elements with random numbers. The randomised copy of the array is solved sequentially and concurrently and the results are output in the console.

Run the Relaxation.java file with the following arguments:
args[0], Integer: Number of threads with which you want to run it concurrently.
args[1], Double: The precision to which you aspire for your solution.
args[2], Integer: Size of the array.
args[3], Integer: Lower bound to generate random number when creating the initial array.
args[4], Integer: Upper bound to generate random number when creating the initial array.
