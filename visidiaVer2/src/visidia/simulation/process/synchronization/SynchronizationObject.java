package visidia.simulation.process.synchronization;

import java.util.Vector;

/**
 * The class mother of Synchronization objects
 */
public class SynchronizationObject {

	public Vector<Integer> synDoors = new Vector<Integer>();

	protected boolean edgMark[];

	protected boolean connected[];

	protected Vector<Integer> synchroCenters = new Vector<Integer>();// for LC1

	public int center;

	public int synState;

	public int arity = 0;

	public boolean run = true;

	/* Basic */
	/**
	 * this method is used to initialize the structures.
	 * 
	 * @param ar the arity.
	 */
	public void init(int ar) {
		this.arity = ar;
		this.connected = new boolean[this.arity];
		this.edgMark = new boolean[this.arity];
		for (int i = 0; i < this.arity; i++) {
			this.edgMark[i] = false;
			this.connected[i] = true;
		}
	}

	/**
	 * clears the structures.
	 */
	public void reset() {
		this.synDoors.clear();
		this.synchroCenters.clear();
	}

	public Object clone() {
		return new SynchronizationObject();
	}

	public String toString() {
		return ("<state=" + this.synState + "SynDoors" + this.synDoors + ">");
	}

	/* Synchro State Accessors */
	/**
	 * Sets the synchronisation state of the node.
	 * 
	 * @param synstate
	 *            possible values are defined in class SynCT
	 */
	public void setState(int synstate) {
		this.synState = synstate;
	}

	/**
	 * Returns true if the node is in elected state, returns false otherwise.
	 */
	public boolean isElected() {
		return (this.synState == SynCT.IAM_THE_CENTER);
	}

	/**
	 * Returns true if the node is in the star, returns false otherwise.
	 */
	public boolean isInStar() {
		return (this.synState == SynCT.IN_THE_STAR);
	}

	/**
	 * Returns true if the node is in not the star, returns false otherwise.
	 */
	public boolean isNotInStar() {
		return (this.synState == SynCT.NOT_IN_THE_STAR);
	}

	/**
	 * Adds a new synchronized neighbour to synDoors (in synob).
	 */
	public void addSynchronizedDoor(int i) {
		this.synDoors.add(new Integer(i));
	}

	/**
	 * Sets the mark of the neighbour to "mark"
	 * 
	 * @param neighbour
	 *            the neighbour door.
	 * @param mark
	 *            the new mark state.
	 */
	public void setMark(int neighbour, boolean mark) {
		this.edgMark[neighbour] = mark;
	}

	public boolean getMark(int neighbour) {
		return this.edgMark[neighbour];
	}

	/**
	 * Returns the number of the door of synchronized neighbour in position i.
	 * 
	 * @param i
	 *            position in synDoors.
	 */
	public int getDoor(int i) {
		return ((Integer) this.synDoors.get(i)).intValue();
	}

	public boolean isConnected(int i) {
		return this.connected[i];
	}

	public boolean setConnected(int i, boolean b) {
		return this.connected[i] = b;
	}

	/*
	 * par defaut on ne gere pas la Terminaison, Alors pour ne pas refaire des
	 * codes ou on ne gere pas la Term
	 */
	public boolean hasFinished(int neighbour) {
		// System.out.println("ATTENTION");
		return false;
	}

	public boolean allFinished() {
		// System.out.println("ATTENTION");
		return false;
	}

	public void setGlobEnd(boolean b) {
		// System.out.println("ATTENTION");
	}

	public void setFinished(int neighbour, boolean b) {
		// System.out.println("ATTENTION");
	}

	/* LC1 Methodes */
	/**
	 * vide l'ensemble des centres des etoites de synchronisation.
	 */
	public void resetCenters() {
		this.synchroCenters.clear();
	}

	/**
	 * Add a new center of synchronization.
	 */
	public void addCenter(int i) {
		this.synchroCenters.add(new Integer(i));
	}

	/**
	 * returns the centers of stars.
	 */
	public Vector<Integer> getCenters() {
		return this.synchroCenters;
	}

}
