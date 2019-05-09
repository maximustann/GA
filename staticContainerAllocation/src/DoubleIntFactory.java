import algorithms.*;
import dataCollector.DataCollector;
import gaFactory.GAFactory;

public class DoubleIntFactory implements GAFactory{
    public InitPop getInitPopMethod(){
        return null;
    }
    public Mutation getMutation(){

        return null;
    }
    public Selection getSelection(int tournamentSize, int optimization){

        return null;
    }
    public Crossover getCrossover(){

        return null;
    }
    public Sort getSort(){

        return null;
    }
    public DataCollector getDataCollector(){

        return null;
    }
    public Elitism getElitism(int elitSize, int optimization){

        return null;
    }
    //	public Constraint getConstraint();
    public Distance getDistance(){

        return null;
    }
}
