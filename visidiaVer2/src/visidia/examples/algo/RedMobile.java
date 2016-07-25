package visidia.examples.algo;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;


import visidia.simulation.process.algorithm.SensorSyncAlgorithm;
import visidia.simulation.process.messages.StringMessage;

public class RedMobile extends SensorSyncAlgorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8437452624823359954L;
	private WitnessCache cache;
	private Vector<SensorMessage> claims;
	private boolean receiving;
	private boolean isMalacious;
	private WitnessCache witnessPoints;
	private static Boolean cloneDetected = new Boolean(false);

	@Override
	public Object clone() {
		return new RedMobile();
	}

	@Override
	public void init() {
		Point posInitial = this.vertex.getPos();
		Point lastPos = new Point(posInitial);
		this.nextPulse();


		Random rand = new Random();
		setConfiguration();

		/*// Step -1
		this.cache = new WitnessCache();
		this.claims = new Vector<SensorMessage>();
		this.receiving = true;
		RedMobile.cloneDetected = false;

		this.setUpRouting();
		this.nextPulse();

		this.setUpRouting();
		this.clearWitnessPoints();
		this.nextPulse();

		String label = this.getProperty("label").toString();
		if (label.equals(new String("N")) || label.equals(new String("L"))) {
			this.isMalacious = false;
		} else {
			this.isMalacious = true;
		}
		this.nextPulse();

		// Step 0
		this.fixWitnessPoints();
		if (this.getId() == 1)
			for (int i = 0; i < witnessPoints.size(); i++) {
				;// System.out.println("Wtiness point position "+ i+" :
					// "+(WitnessPoints.get(i)).getX()+"
					// "+(WitnessPoints.get(i)).getY());

			}

		this.nextPulse();

		// Step 1
		this.broadcastLoc();
		this.nextPulse();
		// Step 1.5
		this.receiveClaims();
		this.nextPulse();

		// Step 2,2.5
		this.transmitClaims();
		this.nextPulse();

		// Step 2.5,3
		while (receiving) {
			claims.clear();
			this.receiveClaims(false);
			this.nextPulse();
			receiving = false;
			this.nextPulse();
			this.processClaims();
			this.nextPulse();
		}

		// Step 4
		if (this.cache.cloneDetected() && !isMalacious) {

			this.putProperty("label", new String("L"));
			synchronized (cloneDetected) {
				if (!cloneDetected) {
					//System.out.println(String.valueOf(iterationNumber) + " " + "detected");
					cloneDetected = true;
				}
				//else System.out.println(String.valueOf(iterationNumber) + " " + "NotDetected");
			}
		}*/
		
			int itMayMove = rand.nextInt(5); // Entre 0 et 1
			if (this.vertex.getPos().equals(lastPos))
				;//System.out.println(lastPos + "inchangée" + this.vertex.getPos());
			else if ((this.vertex.getPos().getX() != lastPos.getX()) && (this.vertex.getPos().getY() != lastPos.getY()))
				;//System.out.println(this.getId() + "oblique" + lastPos + " " + this.vertex.getPos());

			lastPos = new Point(this.vertex.getPos());
			// System.out.println("sortir de l'etat sleep"+this.getId()+"
			// "+nombre+" "+this.vertex.getPos().x+" "+this.vertex.getPos().y+"
			// ");
			if (itMayMove != 0)
				this.move(this.getId());
			else
				;//System.out.println("move with " + itMayMove);
			this.nextPulse();
			if (getArity() != 0){ 
				for (int i = 0; i < getArity(); i++) {
					this.sendTo(i, new StringMessage("Hello"+i));
				}
				
			}	
			if (this.getId() == 1)
				System.out.println("____________________________");

		}

	private void processClaims() {
		// TODO Auto-generated method stub
		
	}

	private void receiveClaims(boolean b) {
		// TODO Auto-generated method stub
		
	}

	private void transmitClaims() {
		// TODO Auto-generated method stub
		
	}

	private void receiveClaims() {
		// TODO Auto-generated method stub
		
	}

	private void broadcastLoc() {
		// TODO Auto-generated method stub
		
	}

	private void fixWitnessPoints() {
		// TODO Auto-generated method stub
		
	}

	private void clearWitnessPoints() {
		// TODO Auto-generated method stub
		
	}

	private void setUpRouting() {
		// TODO Auto-generated method stub
		
	}

	private void setConfiguration() {
		if(this.getId()==1){
			this.putProperty("label", new String("P"));
		}
		else if (this.getId()==2){
			this.putProperty("label", new String("N"));
		}
		nextPulse();
	}
}


