package visidia.examples.algo;

import visidia.simulation.Console;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.StringMessage;
import visidia.simulation.process.messages.VectorMessage;
import visidia.stats.Statistics;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.Door;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Color;
import java.util.Random;
import java.awt.Point;
import java.util.Arrays;

public class RED_pts_mobile extends Routing {

	private boolean isMalacious;

	public static final int NoWitnessPoints = 1;

	private static Boolean receiving = true;
	private static Vector<Point> WitnessPoints = new Vector<Point>();
	private static Boolean cloneDetected = new Boolean(false);

	// Set to true so that comporomised and cloned nodes drop locaiotn claims
	private static Boolean dropLocationClaims = new Boolean(false);
	// Set to a value between 0 and 1. If set to x => probability of
	// transmission = 1-x
	private static double THRESHOLD = 0.50;
	// Node ID of clone A
	private static Integer cloneA = new Integer(0);
	// Starting node id for clone A'
	private static Integer startId = new Integer(1);

	@Override
	public Object clone() {
		return new RED_pts_mobile();
	}

	private void fixWitnessPoints() {
		synchronized (WitnessPoints) {
			if (this.WitnessPoints.size() < this.NoWitnessPoints) {
				this.WitnessPoints.addElement(this.getRandPoint());
			}
		}
	}

	private void clearWitnessPoints() {
		synchronized (WitnessPoints) {
			if (this.WitnessPoints.size() != 0) {
				this.WitnessPoints = new Vector<Point>();
				iterationNumber += 1;
			}
		}
	}

	private void broadcastLoc() {
		String label = this.getProperty("label").toString();
		// Dest Point(-1,-1) is used for broadcast
		if (!label.equals(new String("N")) && !label.equals(new String("L")) && !label.equals(new String("M"))) {
			synchronized (levelTrace) {
				this.levelTrace.incrementNbMessage(this.getArity());
			}
			this.sendAll(new SensorMessage(label, new Point(-1, -1), this.getPosition()));
		}
	}

	private boolean shouldISend() {
		double val = this.rand.nextDouble();
		if (val > this.THRESHOLD) {
			return true;
		} else {
			return false;
		}
	}

	private void transmitClaims() {
		if (this.isMalacious && this.dropLocationClaims)
			return;
		synchronized (WitnessPoints) {
			for (SensorMessage msg : this.claims) {
				if (this.shouldISend()) {
					for (Point dest : this.WitnessPoints) {
						msg.setDest(dest);
						// System.out.println(dest);
						this.forwardMessage(dest, msg);
					}
				}
			}
		}
	}

	private void processClaims() {
		Door d = new Door();
		synchronized (receiving) {
			for (SensorMessage msg : claims) {
				if (this.isMalacious && this.dropLocationClaims)
					continue;
				// Differs here for RED and LSM -- LSM = > We add all messages
				// to cache.
				if (!this.forwardMessage(msg.getDest(), msg)) {
					// Wasn't able to forward message => this was the
					// destination
					cache.addClaim(msg.getLabel(), msg.getClaim());
				} else {
					// Still some node needs to receive this message
					receiving |= true;
				}
			}
		}
	}

	@Override
	public void init() {
		// the 4 following lines for the version of RED with points evluation
		/*if (this.iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION + startId == this.getId() || this.getId() == cloneA) {
			if(this.iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION + startId == this.getId() ){
				posA=this.vertex.getPos();
				
			}
			if(this.getId() == cloneA){	
				posCloneA=new Point(this.vertex.getPos());
			}
			//System.out.println(this.getId()+" "+(int)this.vertex.getPos().getX()+" "+(int)this.vertex.getPos().getY());
			this.putProperty("label", new String("P"));
		} else {
			this.putProperty("label", new String("N"));
		}*/

setConfiguration();

		// Step -1
		this.cache = new WitnessCache();
		this.claims = new Vector<SensorMessage>();
		this.receiving = true;
		this.cloneDetected = false;

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
			for (int i = 0; i < WitnessPoints.size(); i++) {
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
		}
		/**
		 * 
			Statistics stats = null;

			Console console = this.proc.getServer().getConsole();
			if (console == null) {
				System.out.println("console null");
			} else {
				System.out.println("console not null");
				stats = console.getStats();
				if (stats == null) {
					;
					System.out.println("	stat null");
				} else {
					;// System.out.println(" stat not null");
						// System.out.println(stats.toString());
					for (int i = 0; i < stats.asHashTable().size(); i++) {
						System.out.println(stats.asHashTable().get(iterationNumber));
					}
				}
			}
			// if (simulationId > 1) stats = console.getStats();
			 * 		// levelTrace.setNb_Messages(this.gets
		this.proc.getServer().getConsole();
		 */
		this.nextPulse();
		
	
		//System.out.println(String.valueOf(iterationNumber) + " " + "detected");
		statisticsProc(iterationNumber, cloneDetected, posA,	posCloneA);

	}

	
}
/*
 * Step 0 - Fix some set of locations -- so this using static member type Step 1
 * - Broadcast location Step 1.5 - Recive the location claims and store them
 * Step 2 - Send location claims to witnesses with some probability --
 * Forwarding might take multiple pulses Step 2.5 - Malacious nodes drop
 * location claims.. -- Can be made smarter.. For the time being drop all Step 3
 * - Receive the location claims -- Synchornize using a static variable(Could
 * have been done by time..) Step 4 - Change node label in case of clone
 * detection
 */
