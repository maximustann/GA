/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA framework
 * Description:  GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * WriteFileHai.java - write result to file
 */
package GaAllocationProblem;

import java.io.IOException;
import java.util.ArrayList;

import FileHandlers.WriteByRow;

public class WriteFileHai {
	private WriteByRow fitnessWriter;
	private WriteByRow timeWriter;
	private String fitnessAddr;
	private String timeAddr;
	
	/**
	 * 
	 * @param fitnessAddr an address to store fitness results
	 * @param timeAddr an address to store time results
	 */
	public WriteFileHai(String fitnessAddr, String timeAddr){
		fitnessWriter = new WriteByRow(",", 1);
		timeWriter = new WriteByRow(",", 1);
		this.fitnessAddr = fitnessAddr;
		this.timeAddr = timeAddr;
	}
	
	/**
	 * write results to files. 
	 * @param fitness fitness values
	 * @param time time values
	 * @throws IOException
	 */
	public void writeResults(ArrayList<Double> fitness, ArrayList<Double> time) throws IOException{
		fitnessWriter.write(fitnessAddr, fitness);
		timeWriter.write(timeAddr, time);
	}
}
