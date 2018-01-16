import FileHandlers.WriteByRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFileBilevel {
    private WriteByRow fitnessWriter;
    private WriteByRow timeWriter;
    private String fitnessAddr;
    private String timeAddr;
    private String base;


    /**
     *
     * @param fitnessAddr an address to store fitness results
     * @param timeAddr an address to store time results
     */
    public WriteFileBilevel(String fitnessAddr, String timeAddr){
//		fitnessWriter = new WriteByRow(",", 6);
//		timeWriter = new WriteByRow(",", 1);
        this.fitnessAddr = fitnessAddr;
        this.timeAddr = timeAddr;
    }
//
//    public WriteFileBilevel(String base){
//        this.base = base;
//    }
//    public void writeGenerationsFitnessToFile(ArrayList<ArrayList<double[]>> fitness, ArrayList<ArrayList<WSRP_IntChromosome>> nonDom) throws IOException {
//        int generation = fitness.size();
//        for(int i = 0; i < generation; i++){
//            String generationAddr = base + "/generationFitness/" + i;
//
//            File dir = new File(generationAddr);
//            dir.mkdir();
//            String generationFitAddr = generationAddr + "/fitness.csv";
//            String generationIndAddr = generationAddr + "/chromosome.csv";
//            fitnessWriter = new WriteByRow(",", 0);
//            fitnessWriter.writeArray(generationFitAddr, fitness.get(i));
//            fitnessWriter.writeArray(generationIndAddr, adapter(nonDom.get(i)));
//        }
//        System.out.println("Done");
//    }
//
//    private ArrayList<double[]> adapter(ArrayList<WSRP_IntChromosome> input){
//        ArrayList<double[]> output = new ArrayList<double[]>();
//        for(int i = 0; i < input.size(); i++){
//            double[] chromo = new double[input.get(i).size()];
//            for(int j = 0; j < input.get(i).size(); j++){
//                chromo[j] = input.get(i).individual[j];
//            }
//            output.add(chromo);
//        }
//        return output;
//    }
//
//    /**
//     * write results to files.
//     * @param fitness fitness values
//     * @param time time values
//     * @throws IOException
//     */
//    public void writeResults(ArrayList<ArrayList<ArrayList<double[]>>> fitness, ArrayList<Double> time
//    ) throws IOException{
//        fitnessWriter = new WriteByRow(",", 0);
//        timeWriter = new WriteByRow(",", 1);
//        WriteByRow nonDonSetWriter = new WriteByRow(",", 1);
//
//        // for each generation
//        for(int i = 0; i < fitness.size(); i++){
//            String runAddr = base + "/runFitness/" + i;
//            File dir = new File(runAddr);
//            dir.mkdirs();
//            for(int j = 0; j < fitness.get(i).size(); j++){
//                String generation = runAddr + "/" + j;
//                File gen = new File(generation);
//                gen.mkdirs();
//                String generationFit = generation + "/fitness.csv";
//                timeAddr = generation + "/time.csv";
//                fitnessWriter.writeArray(generationFit, fitness.get(i).get(j));
//                ArrayList<Double> tempTime = new ArrayList<Double>();
//                tempTime.add(time.get(i));
//                timeWriter.write(timeAddr, tempTime);
//            }
//
//        }
//    }


}
