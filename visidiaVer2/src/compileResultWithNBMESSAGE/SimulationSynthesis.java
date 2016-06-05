package compileResultWithNBMESSAGE;

import java.awt.Point;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationSynthesis implements Comparable {
	private int idA;
	private int idCloneA;
	private Point posA;
	private Point posCloneA;

	private double averageNbMessageSent;
	private long detectionNb;
	private long lineNB;
	private long totalNbMessageSent;
	private long total_nb_WinessCacheMax;
	private long total_nb_WinessCache;
	private long total_nb_claimsMax;
	private long total_nb_claims;

	private double detectRate;
	private double averageMemorySize;
	private double maxMemorySize;
	private double totalMemorySize;

	private double average_nb_WinessCacheMax;
	private double average_nb_WinessCache;
	private double average_nb_claimsMax;
	private double average_nb_claims;

	public SimulationSynthesis(int idA, int idCloneA) {
		super();
		this.idA = idA;
		this.idCloneA = idCloneA;
	}

	public SimulationSynthesis(String line) {
		// 8 notDtected idA=8 idCloneA=0 posCloneA=(50,50) posA=(100, 100)
		// nb_WinessCacheMax=0 nb_WinessCache=14 nb_claimsMax=0 nb_claims=0
		// Nb_Messages=14
		// totalMemory=476 maxMemory=34
		super();
		this.idA = this.extractIntFromStringAtPos(line, 2);
		this.idCloneA = this.extractIntFromStringAtPos(line, 3);
		this.posA = new Point(this.extractIntFromStringAtPos(line, 4), this.extractIntFromStringAtPos(line, 5));
		this.posCloneA = new Point(this.extractIntFromStringAtPos(line, 6), this.extractIntFromStringAtPos(line, 7));
		total_nb_WinessCacheMax = this.extractIntFromStringAtPos(line, 8);
		total_nb_WinessCache = this.extractIntFromStringAtPos(line, 9);
		total_nb_claimsMax = this.extractIntFromStringAtPos(line, 10);
		total_nb_claims = this.extractIntFromStringAtPos(line, 11);
		this.totalNbMessageSent = this.extractIntFromStringAtPos(line, 12);
		this.totalMemorySize = this.extractIntFromStringAtPos(line, 13);
		this.maxMemorySize = this.extractIntFromStringAtPos(line, 14);

		if (line.contains("notDtected")) {
			this.detectionNb = 0;
		} else if (line.contains("detected")) {
			for (int i = 0; i < 1000; i++) {
				// System.out.print("#");

			}
			this.detectionNb = 1;
		}

		this.lineNB = 1;
		this.averageMemorySize = totalMemorySize / lineNB;
		this.averageNbMessageSent = averageNbMessageSent / lineNB;
		this.detectRate = this.detectionNb / lineNB;
	}

	public void update(double detectRate, double averageMemorySize, double maxMemorySize, double averageNbMessageSent,
			double current_nb_WinessCacheMax, double current_nb_WinessCache, double current_nb_claimsMax,
			double current_nb_claims) {

		this.detectRate = detectRate;
		this.averageMemorySize = averageMemorySize;
		this.maxMemorySize = maxMemorySize;
		this.averageNbMessageSent = averageNbMessageSent;
		average_nb_WinessCacheMax = current_nb_WinessCacheMax;
		average_nb_WinessCache = current_nb_WinessCache;
		average_nb_claimsMax = current_nb_claimsMax;
		average_nb_claims = current_nb_claims;
	}

	public void update(String ligne) {
		incrementLineNb();
		if (ligne.contains("detected"))
			this.incrementDetectionNb();
		int wc = extractIntFromStringAtPos(ligne, 5);
		int c = extractIntFromStringAtPos(ligne, 7);
		maxMemorySize = Math.max(this.getMaxMemorySize(), wc + c);

		total_nb_WinessCacheMax += this.extractIntFromStringAtPos(ligne, 8);
		total_nb_WinessCache += this.extractIntFromStringAtPos(ligne, 9);
		total_nb_claimsMax += this.extractIntFromStringAtPos(ligne, 10);
		total_nb_claims += this.extractIntFromStringAtPos(ligne, 11);

		totalNbMessageSent += extractIntFromStringAtPos(ligne, 8);
		totalMemorySize += wc + c;
		this.averageMemorySize = (wc + c) / this.getLineNB();
		this.averageNbMessageSent = this.getTotalNbMessageSent() / this.getLineNB();
		average_nb_WinessCacheMax = total_nb_WinessCacheMax / this.getLineNB();
		average_nb_WinessCache = total_nb_WinessCache / this.getLineNB();
		average_nb_claimsMax = total_nb_claimsMax / this.getLineNB();
		average_nb_claims = total_nb_claims / this.getLineNB();

	}

	private int extractIntFromStringAtPos(String line, int n) {
		Pattern pattern = Pattern.compile("\\d+");
		String target = line.toString();
		Matcher matcher = pattern.matcher(target);

		int currentInt = 0;
		for (int j = 0; j < n; j++) {
			if (matcher.find())
				currentInt = (int) Integer.parseInt(matcher.group());
			else
				return -1;

		}
		return currentInt;

	}

	@Override
	public String toString() {

		DecimalFormat formatter = new DecimalFormat();
		formatter.setMinimumFractionDigits(20);
		formatter.setDecimalSeparatorAlwaysShown(true);

		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		// symbols.setGroupingSeparator('*');
		formatter.setDecimalFormatSymbols(symbols);

		// if(detectionNb>0)
		// return "("+idCloneA+","+idA+") :"+"SimulationSynthesis "
		return "" + "idA=" + idA 
				+ " idCloneA=" + idCloneA 
				+ " posA=(" + (((int) posA.getX())-50) 
				+ " " + (((int) posA.getY())-50)+ ")" 
				+ " posCloneA=(" + (((int) posCloneA.getX())-50) 
				+ " " +(((int) posCloneA.getY())-50) + ")"
				// 
				+ " detectRate=" + formatter.format(detectRate) 
				+ " averageNbMessageSent=" + averageNbMessageSent
				+ "average_nb_WinessCacheMax" + average_nb_WinessCacheMax
				+ " average_nb_WinessCache" + average_nb_WinessCache 
				+ "average_nb_claimsMax" + average_nb_claimsMax
				+ " average_nb_claims" + average_nb_claims
				+ " averageMemorySize=" + averageMemorySize
				+ " maxMemorySize=" + maxMemorySize 
				+ " detectionNb=" + detectionNb 
				+ " lineNB=" + lineNB 
				+ " totalNbMessageSent=" + totalNbMessageSent
				+ " totalMemorySize=" + totalMemorySize ;
	}

	@Override
	public int compareTo(Object ss) {
		if (((SimulationSynthesis) ss).getIdA() == this.getIdA()
				&& ((SimulationSynthesis) ss).getIdCloneA() == this.getIdCloneA()) {
			return 0;
		} else {
			return 1;
		}
	}

	public void merge(SimulationSynthesis simulationSynthesis) {
		// System.out.println("");
		// System.out.println(this);
		// System.out.println(simulationSynthesis);

		// 8 notDtected idA=8 idCloneA=0 posCloneA=(50,50) posA=(100, 100)
		// nb_WinessCacheMax=0 nb_WinessCache=14 nb_claimsMax=0 nb_claims=0
		// Nb_Messages=14
		// totalMemory=476 maxMemory=34

		// is.idA =
		// this.idCloneA =
		// this.posA=
		// this.posCloneA=
		
		detectionNb+=simulationSynthesis.detectionNb;
		lineNB+=simulationSynthesis.lineNB;
		totalNbMessageSent+=simulationSynthesis.totalNbMessageSent;
		total_nb_WinessCacheMax+=simulationSynthesis.total_nb_WinessCacheMax;
		total_nb_WinessCache+=simulationSynthesis.total_nb_WinessCache;
		total_nb_claimsMax+=simulationSynthesis.total_nb_claimsMax;
		total_nb_claims+=simulationSynthesis.total_nb_claims;
		
		
		



		this.totalNbMessageSent += simulationSynthesis.getTotalNbMessageSent();
		this.totalMemorySize += simulationSynthesis.getTotalMemorySize();// =
																			// this.extractIntFromStringAtPos(line,
																			// 13);
		this.maxMemorySize = Math.max(simulationSynthesis.getMaxMemorySize(), this.getMaxMemorySize());

		this.detectionNb += simulationSynthesis.getDetectionNb();
		this.lineNB += simulationSynthesis.getLineNB();

		// if(simulationSynthesis.getDetectionNb()>0){
		// System.out.println(simulationSynthesis);
		// System.out.println(this);

		// System.out.println(this);

		// }
		// if(simulationSynthesis.getLineNB()>0){
		// System.out.println(simulationSynthesis);

		// System.out.println(this);

		// System.out.println(this);

		// }

		this.averageMemorySize = (double) totalMemorySize / lineNB;
		this.averageNbMessageSent = (double) totalNbMessageSent / lineNB;
		this.detectRate = (double) this.detectionNb / lineNB;
		this.average_nb_WinessCacheMax = (double) this.total_nb_WinessCacheMax / lineNB;
		this.average_nb_WinessCache = (double) this.total_nb_WinessCache / lineNB;
		this.average_nb_claimsMax = (double) this.total_nb_claimsMax / lineNB;
		this.average_nb_claims = (double) this.total_nb_claims / lineNB;
		

		// System.out.println(this);

	}

	private void incrementDetectionNb() {
		this.detectionNb++;

	}

	private void incrementLineNb() {
		this.lineNB++;

	}

	public int getIdA() {
		return idA;
	}

	public void setIdA(int idA) {
		this.idA = idA;
	}

	public int getIdCloneA() {
		return idCloneA;
	}

	public void setIdCloneA(int idCloneA) {
		this.idCloneA = idCloneA;
	}

	public double getAverageNbMessageSent() {
		return averageNbMessageSent;
	}

	public void setAverageNbMessageSent(double averageNbMessageSent) {
		this.averageNbMessageSent = averageNbMessageSent;
	}

	public double getaverage_nb_WinessCache() {
		return average_nb_WinessCache;
	}

	public void setAverage_nb_WinessCache(double average_nb_WinessCache) {
		this.average_nb_WinessCache = average_nb_WinessCache;
	}

	public double getAverage_nb_claimsMax() {
		return average_nb_claimsMax;
	}

	public void setAverage_nb_claimsMax(double average_nb_claimsMax) {
		this.average_nb_claimsMax = average_nb_claimsMax;
	}

	public double getAverage_nb_claims() {
		return average_nb_claims;
	}

	public void setAverage_nb_claims(double average_nb_claims) {
		this.average_nb_claims = average_nb_claims;
	}

	public double getAverage_nb_WinessCacheMax() {
		return average_nb_WinessCacheMax;
	}

	public void setAverage_nb_WinessCacheMax(double average_nb_WinessCacheMax) {
		this.average_nb_WinessCacheMax = average_nb_WinessCacheMax;
	}

	public double getTotalNbMessageSent() {
		return totalNbMessageSent;
	}

	public void setTotalNbMessageSent(int totalNbMessageSent) {
		this.totalNbMessageSent = totalNbMessageSent;
	}

	public double getTotalMemorySize() {
		return totalMemorySize;
	}

	public void setTotalMemorySize(int totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}

	public long getDetectionNb() {
		return detectionNb;
	}

	public void setDetectionNb(long detectionNb) {
		this.detectionNb = detectionNb;
	}

	public long getLineNB() {
		return lineNB;
	}

	public void setLineNB(long lineNB) {
		this.lineNB = lineNB;
	}

	public double getDetectRate() {
		return detectRate;
	}

	public void setDetectRate(double detectRate) {
		this.detectRate = detectRate;
	}

	public double getAverageMemorySize() {
		return averageMemorySize;
	}

	public void setAverageMemorySize(double averageMemorySize) {
		this.averageMemorySize = averageMemorySize;
	}

	public double getMaxMemorySize() {
		return maxMemorySize;
	}

	public void setMaxMemorySize(double maxMemorySize) {
		this.maxMemorySize = maxMemorySize;
	}

}
