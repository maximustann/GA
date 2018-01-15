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
 * This default data collector only contains the time collector.
 * All the actual data you would like to collect must be defined in your extension.
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since PSO framework 1.0
 */
public abstract class DataCollector {
	protected ArrayList<Double> timeData;
	protected long start, end;


	// initialize timer field
	public DataCollector(){
		timeData= new ArrayList<Double>();
	}


    /**
     * collect result
     * @param result can be a 2D array or 1D array
     */
	public abstract void collect(Object result);

	/**
	 * collect individual
	 */
	public abstract void collectSet(Object set);


	/**
	 * if the flag is 0, start timer
	 * else stop the timer.
	 *
	 * Translate time into seconds and keep 2 decimal places
	 */
	public void collectTime(int flag){
		if(flag == 0) start = System.nanoTime();
		else {
			end = System.nanoTime();
			timeData.add(new Double(Math.floor((end - start) / 10000000.0)) / 100.0);
		}
	}

	/**
	 * Print the average time elapse of a run.
	 */
	public void printMeanTime(){
		int size = timeData.size();
		double sum = 0;
		for(int i = 0; i < size; i++){
			sum += timeData.get(i);
		}
		System.out.println("time is : " + Math.floor(sum / size * 100) / 100.0);
	}

	/**
	 *
	 * @return timeData a list of Double
	 */
	public ArrayList<Double> getTime(){
		return timeData;
	}
}
