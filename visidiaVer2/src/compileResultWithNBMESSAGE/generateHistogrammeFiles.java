package compileResultWithNBMESSAGE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class generateHistogrammeFiles {
	private static final String RESULT_FILES_FOLDER = "./traceSquareAllsAlgosResult_compiled/";
	public static final String GNUPLOT_HISTOGRAMS_FILES = "./gnuplotHistFiles/";


	public static void main(String[] args) {
		deleteDirectory(GNUPLOT_HISTOGRAMS_FILES);
		ArrayList<Histogram> histograms = new ArrayList<Histogram>();
		File f = new File(RESULT_FILES_FOLDER);
		String[] listFichiers= f.list();;	//getFilesList(f);
		for (int i = 0; i < listFichiers.length; i++) {
			String currentFile = listFichiers[i];
			//System.out.println(currentFile.substring(12,20));
			Histogram h=null;
			
			if(currentFile.contains("LSM_pts")){
				 h = new Histogram ("LSM_pts",extractIntFromStringAtPos(currentFile,1));
			
			}
			else if(currentFile.contains("RED_pts")){
				 h = new Histogram ("RED_pts",extractIntFromStringAtPos(currentFile,1));				
			}
			else if(currentFile.contains("SDC_pts")){
				 h = new Histogram ("SDC_pts",extractIntFromStringAtPos(currentFile,1));				
			}
			else if(currentFile.contains("LSM_Walk_pts")){
				 h = new Histogram ("LSM_Walk_pts",extractIntFromStringAtPos(currentFile,1));					
				
			}
			if(h!=null){
				try {
					BufferedReader fichier  = new BufferedReader(new FileReader(RESULT_FILES_FOLDER+currentFile));
					String ligne;
					try {
						while ((ligne = fichier.readLine()) != null) {
							//System.out.println(ligne);
						     h.updateHistogram(ligne);
						  //   System.out.println(h);
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
				  System.out.println(h.histString());
				  saveLine(h.histNetString(),GNUPLOT_HISTOGRAMS_FILES+"hist.txt");
				  histograms.add(h);
				  System.out.println(histograms.size());
			}
		}

			int[] sizes = {100,1000,5000};
			String[] algos= {"RED_pts","LSM_pts","LSM_Walk_pts","SDC_pts"};
			for( String algo:algos){
				String s_averageNbMessage="";
				String s_averageDetectRate="";
				String s_averageNbWitness="";
				for(int s:sizes){
					for (int i=0;i<histograms.size();i++){
						if(histograms.get(i).getAlgo().equals(algo) && histograms.get(i).getGraphSize()==s ){
							 s_averageNbMessage+=histograms.get(i).getAverageNbMessage()+ " ";
							 s_averageDetectRate+=histograms.get(i).getAverageDetectRate()+ " ";
							 s_averageNbWitness +=histograms.get(i).getAverageNbWitness()+ " ";	
						}	
					}
				}
				saveLine(algo+" "+s_averageNbMessage,GNUPLOT_HISTOGRAMS_FILES+"hists_averageNbMessage.txt");
				saveLine(algo+" "+s_averageDetectRate,GNUPLOT_HISTOGRAMS_FILES+"hists_averageDetectRate.txt");
				saveLine(algo+" "+s_averageNbWitness,GNUPLOT_HISTOGRAMS_FILES+"hists_averageNbWitness.txt");	
			}
		
		
	}


		private static void saveLine(String line, String fileURL) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(new File(fileURL), true));
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


	private static ArrayList<String> getListAlgo() {
		ArrayList<String> list= new ArrayList<String>();
		String[]tableauAlgos={"RED_pts","LSM_pts","LSM_Walk_pts","SDC_pts"};
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

	private static Integer extractIntFromStringAtPos(String currentAlgoFile, int n) {
		Pattern pattern = Pattern.compile ("\\d+");
		String target = currentAlgoFile.toString();
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


	private static ArrayList<String> getFilesList(File f) {
		String[]tableauAlgos={"RED_pts","LSM_pts","LSM_Walk_pts","SDC_pts"};
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
		//	update(sizeG,versionG,current);
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


	private static void deleteDirectory(String emplacement) {
		File path = new File(emplacement);
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	       
	           files[i].delete();
	      }
	    }
		
	}

}
