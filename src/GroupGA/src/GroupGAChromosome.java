package GroupGA.src;

import algorithms.Chromosome;
import algorithms.Gene;

import java.util.ArrayList;

public class GroupGAChromosome extends Chromosome {
    private ArrayList<PM> pmList;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int size() {
        return pmList.size();
    }

    @Override
    public Gene cut(int cutPoint, int geneIndicator) {
        return null;
    }

    @Override
    public void print() {

    }

    @Override
    public Chromosome clone() {
        return null;
    }

    @Override
    public boolean equals(Chromosome target) {
        return false;
    }
}
