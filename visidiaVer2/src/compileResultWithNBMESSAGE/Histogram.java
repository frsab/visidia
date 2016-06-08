package compileResultWithNBMESSAGE;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Histogram implements Comparable{
	//idA=0 idCloneA=5 posA=(0 0) posCloneA=(200 200)
	//detectRate=0.23709273182957394000 
	//		averageNbMessageSent=127.50375939849624average_nb_WinessCacheMax0.49724310776942354 average_nb_WinessCache53.736340852130326average_nb_claimsMax0.49724310776942354 average_nb_claims0.5298245614035088 
	//
	//averageMemorySize=1874.7197994987469 maxMemorySize=124.0 detectionNb=473 lineNB=1995 totalNbMessageSent=254370 totalMemorySize=3740066.0

			
	public String histNetString() {
		int indexAlgo=0;
		if(Algo.contains("LSM_pts")){
			indexAlgo=0;
		
		}
		else if(Algo.contains("RED_pts")){
			indexAlgo=1;			
		}
		else if(Algo.contains("SDC_pts")){
			indexAlgo=2;			
		}
		else if(Algo.contains("LSM_Walk_pts")){
			indexAlgo=3;					
			
		}
		return  
		indexAlgo + "_" 
		+ graphSize + " " 
		+ averageNbMessage + " " 
		+ averageDetectRate+ " " 
		+ averageNbWitness ;


	}
	public String histString() {
		return "Algo " + Algo + " graphSize " + graphSize + " nb_elements" + nb_elements
				+ " averageNbMessage=" + averageNbMessage + " averageDetectRate=" + averageDetectRate
				+ "averageNbWitness=" + averageNbWitness ;

	}
	private String Algo;
	private int graphSize;
	private int nb_elements;
	
	public Histogram(String algo, int graphSize) {
		super();
		Algo = algo;
		this.graphSize = graphSize;
		this.nb_elements = 0;
		this.averageNbMessage = 0;
		this.averageDetectRate = 0;
		this.averageNbWitness = 0;
		this.totalNbMessage = 0;
		this.totalDetectRate = 0;
		this.totalNbWitness = 0;
	}
	public String getAlgo() {
		return Algo;
	}
	public void setAlgo(String algo) {
		Algo = algo;
	}
	public int getGraphSize() {
		return graphSize;
	}
	public void setGraphSize(int graphSize) {
		this.graphSize = graphSize;
	}
	public int getNb_elements() {
		return nb_elements;
	}
	public void setNb_elements(int nb_elements) {
		this.nb_elements = nb_elements;
	}
	public double getAverageNbMessage() {
		return averageNbMessage;
	}
	public void setAverageNbMessage(double averageNbMessage) {
		this.averageNbMessage = averageNbMessage;
	}
	public double getAverageDetectRate() {
		return averageDetectRate;
	}
	public void setAverageDetectRate(double averageDetectRate) {
		this.averageDetectRate = averageDetectRate;
	}
	public double getAverageNbWitness() {
		return averageNbWitness;
	}
	public void setAverageNbWitness(double averageNbWitness) {
		this.averageNbWitness = averageNbWitness;
	}
	public double getTotalNbMessage() {
		return totalNbMessage;
	}
	public void setTotalNbMessage(double totalNbMessage) {
		this.totalNbMessage = totalNbMessage;
	}
	public double getTotalDetectRate() {
		return totalDetectRate;
	}
	public void setTotalDetectRate(double totalDetectRate) {
		this.totalDetectRate = totalDetectRate;
	}
	public double getTotalNbWitness() {
		return totalNbWitness;
	}
	public void setTotalNbWitness(double totalNbWitness) {
		this.totalNbWitness = totalNbWitness;
	}
	private double averageNbMessage ;
	private double averageDetectRate ;
	private double averageNbWitness ;
	
	private double totalNbMessage ;
	private double totalDetectRate ;
	private double totalNbWitness ;

	@Override
	public int compareTo(Object o) {
		if (((Histogram) o).getAlgo().equals(this.getAlgo())){
			return 0;
		}
		return 1;
	}
	public void updateHistogram(String line) {

		this.nb_elements++;
		this.totalNbMessage += extractFloatFromStringAtPos(line ,8);;
		this.totalDetectRate += extractFloatFromStringAtPos(line ,7);;
		this.totalNbWitness += extractFloatFromStringAtPos(line ,10);;
		averageNbMessage= totalNbMessage/nb_elements;
		averageDetectRate= totalDetectRate/nb_elements;
		averageNbWitness =totalNbWitness/nb_elements;
	
		
	}
		
	private float extractFloatFromStringAtPos(String line, int n) {
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
	
	
}
