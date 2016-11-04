package GaAllocationProblem;

import java.io.IOException;
import java.util.ArrayList;

import FileHandlers.WriteByRow;
import FileHandlers.WriteCsvFile;

public class WriteFileHai {
	private WriteByRow fitnessWriter;
	private WriteByRow timeWriter;
	private String fitnessAddr;
	private String timeAddr;
	
	public WriteFileHai(String fitnessAddr, String timeAddr){
		fitnessWriter = new WriteByRow(",", 1);
		timeWriter = new WriteByRow(",", 1);
		this.fitnessAddr = fitnessAddr;
		this.timeAddr = timeAddr;
	}
	
	public void writeResults(ArrayList<Double> fitness, ArrayList<Double> time) throws IOException{
		fitnessWriter.write(fitnessAddr, fitness);
		timeWriter.write(timeAddr, time);
	}
}
