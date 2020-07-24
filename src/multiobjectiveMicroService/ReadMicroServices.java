package multiobjectiveMicroService;

import FileHandlers.ReadByRow;
import FileHandlers.ReadCsvFile;

public class ReadMicroServices {
    private ReadCsvFile readByRow;
    private int [][] microServiceMatrix;

    public ReadMicroServices(
            int microServiceSize,
            int maximumServiceNum,
            String microServicePath){
        readByRow = new ReadByRow();
        microServiceMatrix = new int [microServiceSize][maximumServiceNum];
        double[][] tempMatrix = new double[microServiceSize][maximumServiceNum];
        readByRow.read(microServicePath, tempMatrix);
        for(int row = 0; row < microServiceSize; row++){
            for(int col = 0; col < maximumServiceNum; col++){
                microServiceMatrix[row][col] = (int) tempMatrix[row][col];
            }
        }
    }

    public int[][] getMicroserviceMatrix() {
        return microServiceMatrix;
    }
}
