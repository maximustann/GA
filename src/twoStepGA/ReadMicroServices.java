package twoStepGA;

import FileHandlers.ReadByCol;
import FileHandlers.ReadByRow;
import FileHandlers.ReadCsvFile;

public class ReadMicroServices {
    private ReadCsvFile readByCol;
    private int[][] microService;

    public ReadMicroServices(
            int microServiceSize,
            String microServicePath){
//        readByRow = new ReadByRow();
        readByCol = new ReadByCol();
        microService = new int[1][microServiceSize];
        double[][] tempMatrix = new double[1][microServiceSize];
        readByCol.read(microServicePath, tempMatrix);
//        readByRow.read(microServicePath, tempMatrix);
        for(int i = 0; i < microServiceSize; i++){
                microService[0][i] = (int)tempMatrix[0][i];
        }
    }

    public int[][] getMicroService() {
        return microService;
    }
}
