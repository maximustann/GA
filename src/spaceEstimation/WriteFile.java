package spaceEstimation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFile {

    public void write(String resultPath, ArrayList<double[]> results) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultPath));
        for(double[] result:results){
            writer.write(String.valueOf(result[0]) + "," + String.valueOf(result[1]) + "\n");
        }
        writer.close();
    }
}
