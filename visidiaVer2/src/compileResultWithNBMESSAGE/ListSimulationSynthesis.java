package compileResultWithNBMESSAGE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ListSimulationSynthesis {
	private long nbTotaleSim;
	private long nbConfig;
	private ArrayList<SimulationSynthesis>listSimSynhesis; 
	
	
	public ListSimulationSynthesis() {
		super();
		this.nbTotaleSim = 0;
		this.nbConfig = 0;
		this.listSimSynhesis = new ArrayList<SimulationSynthesis>();
	}

	

	public void update(String line) {
		SimulationSynthesis current = new SimulationSynthesis(line);
		
		int indexOfCurrent=this.indexOf(current);
		if(indexOfCurrent<0){
			listSimSynhesis.add(current);	
		}
		else{
			//System.out.println("Merge " +line);
			SimulationSynthesis x=listSimSynhesis.get(indexOfCurrent);
			x.merge(current);
			listSimSynhesis.set(indexOfCurrent, x);
		}
	}



	private int indexOf(SimulationSynthesis current) {
		int i = 0;
		for (Iterator iterator = listSimSynhesis.iterator(); iterator.hasNext();i++) {
			SimulationSynthesis simulationSynthesis = (SimulationSynthesis) iterator.next();
			if(current.compareTo(simulationSynthesis)==0)
				return i;
				
		}
		
		return -1;
	}



	private boolean exist(SimulationSynthesis ss) {
		for (Iterator iterator = listSimSynhesis.iterator(); iterator.hasNext();) {
			SimulationSynthesis simulationSynthesis = (SimulationSynthesis) iterator.next();
			if(ss.compareTo(simulationSynthesis)==0)
				return true;
			
		}
		return false;
	}



	public void show() {
		//System.out.println(listSimSynhesis.size());
		for (Iterator iterator = listSimSynhesis.iterator(); iterator.hasNext();) {
			SimulationSynthesis simulationSynthesis = (SimulationSynthesis) iterator.next();
//			System.out.println(listSimSynhesis.size()+"_"+simulationSynthesis);
			System.out.println(simulationSynthesis);
			
		}
		
	}



	public void save(String synthesisFileName) {
		System.out.println("Enregistrer la synthese dans :  "+synthesisFileName);
		for (Iterator iterator = listSimSynhesis.iterator(); iterator.hasNext();) {
			SimulationSynthesis simulationSynthesis = (SimulationSynthesis) iterator.next();
			saveLine(simulationSynthesis.toString(),synthesisFileName);
			//System.out.println(listSimSynhesis.size()+"_"+simulationSynthesis);
			
		}
		
	}



	private void saveLine(String line, String fileURL) {
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

}
