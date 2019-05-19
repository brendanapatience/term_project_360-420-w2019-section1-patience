# Code

Place the code you developped for the term project in this folder. Add any instructions/documentation required to run the code, and what results can be expected in this *README* file.


In order to run the simulation of the double pendulum using Euler's method,
set difSol (on line 13) to false. For fourth order Runge-Kutta, set it to true.
Additionally, to obtain the mechanical energy with respect to time data in a
file, set file (on line 14) to true, otherwise set it to false. To run the
simulation, simply type in the following commands in the command line:
	1. javac DoublePendulum.java
	2. java DoublePendulum

IMPORTANT!
Be sure to change the path of the PrintWriter classes to work with your computer.
They occur on lines 185, 195, 209 and 211. If the program is still unable to run,
replace the entire string with either "euler energy vs time.txt" or "runge-kutta
energy vs time.txt". This should create the files in the current directory instead.

To see any obtained results, look at the created files in either this directory
or the data directory.
