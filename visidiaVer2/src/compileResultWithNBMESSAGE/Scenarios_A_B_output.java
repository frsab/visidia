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
		//[idA=14 idCloneA=6 posA=(50 216) posCloneA=(500 100) 
		//detectRate=0,00240000000000000000 averageMemorySize=3073.0384 maxMemorySize=844.0 averageNbMessageSent=157.1776 detectionNb=6 lineNB=2500 totalNbMessageSent=392944 totalMemorySize=7682596.0]23.999999999999996
		
		//[idA=13 idCloneA=1 posA=(150 150) posCloneA=(300 500) 
		//detectRate=0.00300000000000000000 averageMemorySize=4593.66 maxMemorySize=1384.0 averageNbMessageSent=193.901 
		//detectionNb=12 lineNB=4000 totalNbMessageSent=775604 totalMemorySize=1.837464E7]30.0

		
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMinimumFractionDigits(20);
		formatter.setDecimalSeparatorAlwaysShown(true);
		String fileName="";
		String resultLine="";
		fileName=""
		+"gnuplotFile_idA="
		//+		Main.extractIntFromStringAtPos(line,1)
		+"_posAx"
		+		Main.extractIntFromStringAtPos(line,3)
		+"_posAy"
		+		Main.extractIntFromStringAtPos(line,4)
		+"_"+graphSize
		+"_"
		+	algoName	
		+".txt"
		;
		resultLine=""
		+		Main.extractIntFromStringAtPos(line,5)		+"		"
		+		Main.extractIntFromStringAtPos(line,6)		+"		"
		// detectRate
		+		(Main.extractFloatFromStringAtPos(line,7))		+" "
		// averageNbMessageSent
		+		(Main.extractFloatFromStringAtPos(line,8))		+" "
		// average_nb_WinessCacheMax
		+		(Main.extractFloatFromStringAtPos(line,9))		+" "
		// average_nb_WinessCache
		+		(Main.extractFloatFromStringAtPos(line,10))		+" "
		// average_nb_claimsMax
		+		(Main.extractFloatFromStringAtPos(line,11))		+" "
		// average_nb_claims
		+		(Main.extractFloatFromStringAtPos(line,12))		+" "
		// averageMemorySize
		+		(Main.extractFloatFromStringAtPos(line,13))		+" "
		// maxMemorySize
		+		(Main.extractFloatFromStringAtPos(line,14))		+" "
		
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
