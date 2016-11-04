/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * DataCollector.java - An Interface of Data collector.
 */
package dataCollector;

import java.util.ArrayList;

/**
 * data collector
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since PSO framework 1.0
 */
public abstract class DataCollector {
	protected ArrayList<Double> timeData;
	protected long start, end;
	
	public DataCollector(){
		timeData 	= new ArrayList<Double>();
	}
	
	
    /**
     * collect result
     * @param object result can be a 2D array or 1D array
     */	
	public abstract void collect(Object result);
	
	
	/**
	 * if the flag is 0 generation, start timer
	 * else stop the timer, 
	 * translate time into seconds and keep 2 decimal places
	 * @param gen current generation
	 */
	public void collectTime(int flag){
		if(flag == 0) start = System.nanoTime();
		else { 
			end = System.nanoTime();
			timeData.add(new Double(Math.floor((end - start) / 10000000.0)) / 100.0);
		}
	}
	
	public void printMeanTime(){
		int size = timeData.size();
		double sum = 0;
		for(int i = 0; i < size; i++){
			sum += timeData.get(i);
		}
		System.out.println("time is : " + Math.floor(sum / size * 100) / 100.0);
	}

	public ArrayList<Double> getTime(){
		return timeData;
	}
}
