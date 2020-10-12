package NSGAII_NSGGA_Manual;

import variableReplicasFirstStepGA.FirstStepGAChromosome;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReplicaNumWriteFile {

    private String replicaNumPath;
    private String replicaFitnessPath;
    public ReplicaNumWriteFile(String replicaNumPath, String replicaFitnessPath){
        this.replicaNumPath = replicaNumPath;
        this.replicaFitnessPath = replicaFitnessPath;
    }

    public void writeConverge(ArrayList<ArrayList<FirstStepGAChromosome>> dominatedSet) throws IOException {
        int size = dominatedSet.size();
        int counter = 0;
        for(ArrayList<FirstStepGAChromosome> paretoFront:dominatedSet){
            writeFitness(paretoFront, counter);
            counter++;
        }
    }

    public void writeFitness(ArrayList<FirstStepGAChromosome> paretoFront, int gen) throws IOException {
        String path = replicaFitnessPath + gen + ".csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for(FirstStepGAChromosome chromosome:paretoFront){
            double energyFit = chromosome.getEnergyFitness();
            double availFit = chromosome.getAvailabilityFitness();
            writer.write(String.valueOf(energyFit) + "," +
                    String.valueOf(availFit) + "\n");
        }
        writer.close();
    }

    public void writeReplicaNum(
            int counter,
            double[] containerCpu,
            double[] containerMem,
            double[] containerAppId,
            double[] containerMicroServiceId,
            double[] containerReplicaId) throws IOException {
        String path = replicaNumPath + counter + ".csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for(int i = 0; i < containerCpu.length; i++){
            writer.write(String.valueOf(containerCpu[i]) + "," +
                            String.valueOf(containerMem[i]) + "," +
                            String.valueOf(containerAppId[i]) + "," +
                            String.valueOf(containerMicroServiceId[i]) + "," +
                            String.valueOf(containerReplicaId[i]) + "\n");
        }
        writer.close();
    }

}
