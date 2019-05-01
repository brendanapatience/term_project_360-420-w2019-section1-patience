/**
Motion of a pendulum and variation in period as a function of amplitude.

Author: J.-F. Briere
Current version written: March 2019
Description: Calculates the motion of a pendulum as a function of time and extract the period from the data.

NOTE: You need the package jmathplot to run the code.
*/
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import org.math.plot.*;
import org.math.plot.plotObjects.*;

public class DoublePendulum
{
	// constants
	public static final double G = 9.807;	//acceleration due to gravity in m/s/s
	public static final double L = 1.0; 	//length of rigid weightless rods in m
	public static final double M1 = 1.0;	//mass of mass 1 in kg
	public static final double M2 = 1.0;	//mass of mass 2 in kg
	
	//main method
	public static void main(String[] args)
	{
		System.out.println("hello");
	}
}
