package visidia.examples.algo;

import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.StringMessage;
import visidia.simulation.process.messages.VectorMessage;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.Door;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Color;
import java.util.Random;
import java.awt.Point;
import java.util.Arrays;


public class LSM_Walk_pts extends Routing{
	private WitnessCache cache;
	private Vector<SensorMessage> claims;
	private boolean isMalacious;
	private static Boolean receiving = true;
	private static Vector<Point> WitnessPoints = new Vector<Point>();
	private static Boolean cloneDetected = new Boolean(false);

	//Determines the number of walk steps
	private Integer walkCounter = new Integer(10);
	//Set to true so that comporomised and cloned nodes drop locaiotn claims
	private static Boolean dropLocationClaims = new Boolean(false);
	//Set to a value between 0 and 1. If set to x => probability of transmission = 1-x
	private static double THRESHOLD = 0.90;
	//Node ID fo clone A
	private static Integer cloneA = new Integer(28);
	//Starting node id for clone A'
	private static Integer startId = new Integer(23);


	@Override
	public Object clone(){
		return new LSM_Walk_pts();
	}

	private void broadcastLoc(){
		String label = this.getProperty("label").toString();
		// Dest Point(-1,-1) is used for broadcast
		if(!label.equals(new String("N")) && !label.equals(new String("L")) && !label.equals(new String("M")) ){
			this.sendAll(new SensorMessage(label,new Point(-1,-1),this.getPosition()));
		}
	}
	
	private void receiveClaims(){
		Door d = new Door();
		while(this.anyMsg()){
			SensorMessage msg = (SensorMessage)this.receive(d);	
			claims.addElement(msg);
			cache.addClaim(msg.getLabel(),msg.getClaim());
		}
	}

	private void receiveClaims(boolean store){
		Door d = new Door();
		while(this.anyMsg()){
			SensorMessage msg = (SensorMessage)this.receive(d);	
			claims.addElement(msg);
			if(store){cache.addClaim(msg.getLabel(),msg.getClaim());}
		}
	}
	
	private boolean shouldISend(){
		double val = this.rand.nextDouble();
		if( val > this.THRESHOLD ){
			return true;
		}else{
			return false;
		}
	}

	private void transmitClaims(){
		if(this.isMalacious && this.dropLocationClaims) return;
		synchronized(WitnessPoints){
			for(SensorMessage msg : this.claims){
				if(this.shouldISend()){
					Point dest = this.getRandPoint();
					msg.setDest(dest);
					msg.setCounter(this.walkCounter);
					this.forwardMessage(dest,msg);
				}
			}
		}
	}

	private void processClaims(){
		Door d = new Door();
		synchronized(receiving){
			for(SensorMessage msg: claims){
				if(this.isMalacious && this.dropLocationClaims) continue;
				//Differs here for RED and LSM -- LSM = > We add all messages to cache.
				cache.addClaim(msg.getLabel(),msg.getClaim());
				if(this.forwardMessage(msg.getDest(),msg)){
					receiving |= true;
				}else if(msg.getCounter() != 0){
					msg.decCounter();
					this.sendRandNeighbor(msg);
					receiving |= true;
				}
				// We set the walk length = 1+reqd len
				// Whenever a message can't be forwarded we decrease walk length
				// When it hits zero we discard the walk length
			}
		}
	}

	@Override
	public void init(){
		//Temporary Fix
		if(this.getId() == 0) iterationNumber += 1;

                // the 4 following lines for the version of LSM_WALK with points evluation
		if(this.iterationNumber/500 + startId == this.getId() || this.getId() == cloneA){
			this.putProperty("label", new String("P"));
		}else{
			this.putProperty("label", new String("N"));
		}

		// Step -1
		this.cache = new WitnessCache();
		this.claims = new Vector<SensorMessage>();
		this.receiving = true;
		this.cloneDetected = false;

		this.setUpRouting();this.nextPulse();
		

		this.setUpRouting();
		this.nextPulse();

		String label = this.getProperty("label").toString();
		if(label.equals(new String("N")) || label.equals(new String("L")) ){
			this.isMalacious = false;
		}else{
			this.isMalacious = true;
		}
		this.nextPulse();

		
		//Step 1
		this.broadcastLoc();
		this.nextPulse();

		//Step 1.5
		this.receiveClaims();
		this.nextPulse();

		//Step 2,2.5
		this.transmitClaims();
		this.nextPulse();

		//Step 2.5,3
		while(receiving){
			claims.clear();
			this.receiveClaims(false);
			this.nextPulse();
			receiving = false;
			this.nextPulse();
			this.processClaims();
			this.nextPulse();
		}

		//Step 4
		if(this.cache.cloneDetected() && !isMalacious){
			this.putProperty("label", new String("L"));
			synchronized(cloneDetected){
				if(!cloneDetected){
					System.out.println(String.valueOf(iterationNumber)+" "+"detected");
					cloneDetected = true;
				}
			}
		}
		this.nextPulse();

	}
}
/*
Step 0 - Fix some set of locations -- so this using static member type
Step 1 - Broadcast location
Step 1.5 - Recive the location claims and store them
Step 2 - Send location claims to witnesses with some probability -- Forwarding might take multiple pulses
Step 2.5 - Malacious nodes drop location claims.. -- Can be made smarter.. For the time being drop all
Step 3 - Receive the location claims -- Synchornize using a static variable(Could have been done by time..)
Step 4 - Change node label in case of clone detection
*/
