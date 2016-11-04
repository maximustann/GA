package FileHandlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WriteByRow implements WriteCsvFile {
	private String csvSplitBy;
	private int rowNum;
	
	
	public WriteByRow( String csvSplitBy, int rowNum){
		this.csvSplitBy = csvSplitBy;
		this.rowNum = rowNum;
	}
	
	@Override
	public void write(String fileName, ArrayList<Double> data) throws IOException {
		File file = null;
		int dataSize = data.size();
		int enterNum = data.size() / rowNum;
		try {
			file = new File(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!file.exists()){
			file.createNewFile();
		}
		
		@SuppressWarnings("resource")
		FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
		@SuppressWarnings("resource")
		PrintWriter writer = new PrintWriter(file);
		writer.print("");
		
		for(int i = 0, j = 0; i < dataSize; i++, j++){
			if(j == enterNum){
				fw.write(data.get(i).toString() + csvSplitBy + "\n");
				j = 0;
			} else {
				fw.write(data.get(i).toString() + csvSplitBy);
			}
		}
		fw.close();
	}

}
