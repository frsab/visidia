package compileResultWithNBMESSAGE;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
	private static final String RESULT_FILES_FOLDER = "./traceSquareAllsAlgosResult/";
	public static final String COMPILED_RESULT_FILES_FOLDER = "./traceSquareAllsAlgosResult_compiled/";
	

	public static void main(String[] args) {
		deleteDirectory(COMPILED_RESULT_FILES_FOLDER);
		File f = new File(RESULT_FILES_FOLDER);
		ArrayList<String> listFichiers= 		getFilesList(f);
		ArrayList<String> listAlgo =getListAlgo();
		ArrayList<String> currentAlgoFilesList =null;
		ArrayList<String> currentAlgoCurrentSizeFilesList =null;
		ArrayList<Integer> listSize=null;
		for (Iterator iterator = listAlgo.iterator(); iterator.hasNext();) {
			currentAlgoFilesList = new ArrayList<String>();
			String currentAlgo = (String) iterator.next();
			for (Iterator iterator2 = listFichiers.iterator(); iterator2.hasNext();) {
				String currentFile = (String) iterator2.next();
				if(currentFile.contains(currentAlgo)){
					currentAlgoFilesList.add(currentFile);
				}
			}
			listSize = getNotRepeatedInt(listFichiers,1);
			for (Iterator iterator1 = listSize.iterator(); iterator1.hasNext();) {
				currentAlgoCurrentSizeFilesList= new ArrayList<String>();
				//System.out.println("------------------");
				Integer size = (Integer) iterator1.next();
				for (Iterator iterator2 = currentAlgoFilesList.iterator(); iterator2.hasNext();) {
					String currentAlgoFile = (String) iterator2.next();
					if(size==extractIntFromStringAtPos(currentAlgoFile,1)){
						//System.out.println(currentAlgoFile);
						currentAlgoCurrentSizeFilesList.add(currentAlgoFile);
					}
				}
				if(currentAlgoCurrentSizeFilesList.size()>0)
				fixStatSynthesis(currentAlgoCurrentSizeFilesList);
			}
		}
	}

	static void deleteDirectory(String emplacement) {
		
			 
			File path = new File(emplacement);
		    if( path.exists() ) {
		      File[] files = path.listFiles();
		      for(int i=0; i<files.length; i++) {
		       
		           files[i].delete();
		      }
		    }
		  }
		
	

	private static void fixStatSynthesisShow(ArrayList<String> FilesList) {
		for (Iterator iterator = FilesList.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			
			//System.out.println(string);
		}
	}

	private static void fixStatSynthesis(ArrayList<String> filesList) {
		fixStatSynthesisShow(filesList);
		String synthesisFileName= null;
		if(filesList.size()>0){
			synthesisFileName=filesList.get(0).substring(0, filesList.get(0).length() - 6);
			synthesisFileName+=".txt";
		}
		ListSimulationSynthesis lsitSimSynthese =new ListSimulationSynthesis();
		for (Iterator iterator = filesList.iterator(); iterator.hasNext();) {
			String currentFile = (String) iterator.next();
			BufferedReader currentBufferReader;
			try {
				currentBufferReader = new BufferedReader(new FileReader(RESULT_FILES_FOLDER+currentFile));
				String line;
				try {
					while ((line = currentBufferReader.readLine()) != null){
						lsitSimSynthese.update(line);
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
		System.out.println();
		lsitSimSynthese.show();
		lsitSimSynthese.save(COMPILED_RESULT_FILES_FOLDER+synthesisFileName);
	}

	private static int indexSim(String ligne) {
		//System.out.println(ligne);
		int index=(extractIntFromStringAtPos(ligne,2)-1);
		//System.out.println(index);
		return index;
	}

	private static ArrayList<String> getTempList(String algo, Integer size, ArrayList<String> listFichiers) {
		ArrayList<String> list= new ArrayList<String>();
		for (Iterator iterator = listFichiers.iterator(); iterator.hasNext();) {
			String line = (String) iterator.next();
			if(line.contains(algo)&& (size.equals(extractIntFromStringAtPos(line,1)))){
				list.add(line);
			}
			
		}
		return list;
	}

	private static ArrayList<String> getListAlgo() {
		ArrayList<String> list= new ArrayList<String>();
		String[]tableauAlgos={"LSM_pts","LSM_Walk_pts", "RED_pts","SDC_pts"};
		for (int i = 0; i < tableauAlgos.length; i++) {
			list.add(tableauAlgos[i]);
		}
		return list;
	}

	private static ArrayList<Integer> getNotRepeatedInt(ArrayList<String> listFichiers, int pos) {
		ArrayList<Integer> listSize = new ArrayList<Integer>();
		for (int j = 0; j < listFichiers.size(); j++) {
			Integer currentInt=	extractIntFromStringAtPos(listFichiers.get(j),pos);
			if(currentInt!= 0 && !listSize.contains(currentInt))
				listSize.add(currentInt);
		}
		return listSize;
	}

	private static ArrayList<String> getFilesList(File f) {
		String[]tableauAlgos={"LSM_pts","LSM_Walk_pts", "RED_pts","SDC_pts"};
		String[] tableauFichiers;
		ArrayList<Integer> listSize = new ArrayList<Integer>();
		ArrayList<String> listFichiers=new ArrayList<String>();
		int sizeG=0;
		int versionG=0;
		int i;
		tableauFichiers = f.list();
		for (int j = 0; j < tableauFichiers.length; j++) {
			listFichiers.add(tableauFichiers[j]);
		}
		Collections.sort(listFichiers);
		for (i = 0; i < tableauFichiers.length; i++) {
			String current = tableauFichiers[i];
			sizeG=extractIntFromStringAtPos(current,1);
			versionG=extractIntFromStringAtPos(current,2);
			//System.out.println(current);
			update(sizeG,versionG,current);
			//System.out.println("-----> sizeG"+sizeG+"versionG"+versionG);
			try {
				BufferedReader fichier  = new BufferedReader(new FileReader("./traceSquareAllsAlgosResult/"+current));
				String ligne;
				try {
					while ((ligne = fichier.readLine()) != null) {
					     ;// System.out.println(ligne);
					  }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      try {
					fichier.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listFichiers;
	}

	static int extractIntFromStringAtPos(String line, int n) {
		Pattern pattern = Pattern.compile ("\\d+");
		String target = line.toString();
		Matcher matcher = pattern.matcher (target);
		
		int currentInt=0;
		for (int j = 0; j < n; j++) {
			if(matcher.find())
				currentInt =(int)Integer.parseInt(matcher.group());
			else
				return 0;
			
		}
		return currentInt;

	}
	static float extractFloatFromStringAtPos(String line, int n) {
		Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
		//Pattern pattern = Pattern.compile ("\\d+");
		String target = line.toString();
		Matcher matcher = pattern.matcher (target);
		
		float currentFloat=0;
		for (int j = 0; j < n; j++) {
			if(matcher.find())
				currentFloat =Float.parseFloat(matcher.group());
			else
				return 0;
			
		}
		return currentFloat;

	}

	

	private static void update(int sizeG, int versionG, String line) {
		
		Pattern pattern = Pattern.compile ("\\d+");
		String target = line.toString();
		Matcher matcher = pattern.matcher (target);
		
		if(matcher.find())
			sizeG=(int)Integer.parseInt(matcher.group());		
		if(matcher.find())
			versionG=(int)Integer.parseInt(matcher.group());


		
	}

	

	public static void saveLine(String line, String fileURL, String fileFormat) {
		BufferedWriter out = null;
		try {
			File f=new File(fileURL);	
			out = new BufferedWriter(new FileWriter(f, true));
			
			if ( f.length()==0 && fileFormat.equals("gnuPlotOut")){
				System.out.println(fileURL +" gnuPlotOut_________________<<<<<<<<<");
				out.write("#posCloneA_X posCloneA_X   "
				+"detectRate"
				+"   averageNbMessageSent"
				+"   average_nb_WinessCacheMax"
				+"   average_nb_WinessCache"
				+"   average_nb_claimsMax"
				+"   average_nb_claims"
				+"   averageMemorySize"
				+"   axMemorySize \n");

			}
			
			// out = new BufferedWriter(new FileWriter(new
			// File(this.getClass().getName()+this.getNetSize()+".txt"),true));
			out.write(line + "\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
