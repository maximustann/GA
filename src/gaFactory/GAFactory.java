/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA algorithm framework
 * Description:  GA algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * GAFactory.java - An interface of ga factory
 */
package gaFactory;
import algorithm.*;
import dataCollector.DataCollector;

/**
 * GAFactory
 *
 * @author Boxiong Tan (Maximus Tann)
 * @since GA framework 1.0
 */
public interface GAFactory {
	public InitPop getInitPopMethod();
	public Mutation getMutation();
	public Selection getSelection(int tournamentSize, int optimization);
	public Crossover getCrossover();
	public Sort getSort();
	public DataCollector getDataCollector();
	public Elitism getElitism(int elitSize, int optimization);
}
