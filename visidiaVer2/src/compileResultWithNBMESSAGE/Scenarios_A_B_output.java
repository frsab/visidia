package compileResultWithNBMESSAGE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import visidia.VisidiaMain;

public class Scenarios_A_B_output {
	private static final String GNUPLOT_FOLDER = "./gnuplotFiles/";
	

	

	public static void main(String[] args) {
		Main.deleteDirectory(GNUPLOT_FOLDER);
		File f = new File(Main.COMPILED_RESULT_FILES_FOLDER);
		ArrayList<String> listFichiers= 		getFilesList(f);
		for (Iterator iterator = listFichiers.iterator(); iterator.hasNext();) {
			String currentFile = (String) iterator.next();
			System.out.println(currentFile);
			BufferedReader currentBufferReader;
			try {
				currentBufferReader = new BufferedReader(new FileReader(Main.COMPILED_RESULT_FILES_FOLDER+currentFile));
				String line;
				try {
					while ((line = currentBufferReader.readLine()) != null){
						
						
						fixLine(line,Main.extractIntFromStringAtPos(currentFile,1),currentFile.substring(12,17));
						;//System.out.println(line);
					}
						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}

	private static void fixLine(String line,int graphSize,String algoName) {
		//posA=(0 0) posCloneA=(100 100) detectRate=1.00000000000000000000 
		//averageNbMessageSent=897.5134865134866average_nb_WinessCacheMax3.3516483516483517 
		//average_nb_WinessCache28.84115884115884average_nb_claimsMax3.3516483516483517 
		//average_nb_claims52.01698301698302 averageMemorySize=5662.127872127872 
		//maxMemorySize=2734.0 detectionNb=1001 lineNB=1001 totalNbMessageSent=898411 totalMemorySize=5667790.0

		
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMinimumFractionDigits(20);
		formatter.setDecimalSeparatorAlwaysShown(true);
		String fileName="";
		String resultLine="";
		fileName=""
		+"gnuplotFile_idA="
		//+		Main.extractIntFromStringAtPos(line,1)
		+"_posAx"
		+		Main.extractIntFromStringAtPos(line,1)
		+"_posAy"
		+		Main.extractIntFromStringAtPos(line,2)
		+"_"+graphSize
		+"_"
		+	algoName	
		+".txt"
		;
		resultLine=""
		+		Main.extractIntFromStringAtPos(line,3)		+"		"
		+		Main.extractIntFromStringAtPos(line,4)		+"		"
		// detectRate
		+		(Main.extractFloatFromStringAtPos(line,5))		+" "
		// averageNbMessageSent
		+		(Main.extractFloatFromStringAtPos(line,6))		+" "
		// average_nb_WinessCacheMax
		+		(Main.extractFloatFromStringAtPos(line,7))		+" "
		// average_nb_WinessCache
		+		(Main.extractFloatFromStringAtPos(line,8))		+" "
		// average_nb_claimsMax
		+		(Main.extractFloatFromStringAtPos(line,9))		+" "
		// average_nb_claims
		+		(Main.extractFloatFromStringAtPos(line,10))		+" "
		// averageMemorySize
		+		(Main.extractFloatFromStringAtPos(line,11))		+" "
		// maxMemorySize
		+		(Main.extractFloatFromStringAtPos(line,12))		+" "
		
				;
		Main.saveLine(resultLine,GNUPLOT_FOLDER+fileName,"gnuPlotOut");
		
	}

	private static ArrayList<String> getFilesList(File f) {
		String[] tableauFichiers = f.list();
		ArrayList<String> listFichiers=new ArrayList<String>();
		for (int i = 0; i < tableauFichiers.length; i++) {
			
			listFichiers.add(tableauFichiers[i]);
			
		}
		return listFichiers;
	}

}
