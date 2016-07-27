package visidia.simulation.process.algorithm;

import java.util.Collection;
import java.util.LinkedList;

import visidia.rule.Neighbor;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.rule.Rule;
import visidia.rule.Star;
import visidia.simulation.SimulationConstants;
import visidia.simulation.process.MessageProcess;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.BooleanMessage;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.messages.NeighborMessage;
import visidia.simulation.process.messages.StringMessage;
import visidia.simulation.process.synchronization.SynCT;
import visidia.simulation.process.synchronization.SynchronizationAlgorithm;
import visidia.simulation.process.synchronization.SynchronizationObject;
import visidia.simulation.process.synchronization.SynchronizationObjectTerminationRules;

/**
 * Simulateur des regles de reecritures
 * 
 */
public abstract class RuleAlgorithm extends Algorithm {

	private static final long serialVersionUID = -430550548420193500L;

	/**
	 * The relabeling System to simulate
	 */
	transient protected RelabelingSystem relSys = new RelabelingSystem();

	/** The synchronization used for simulation ** */
	public int synType;// type de synchronisation

	// for local synchronization
	// Notice that if synal is null, that instance is
	// an instance of a synchronization algorithm
	transient protected SynchronizationAlgorithm synal = null;
	transient protected SynchronizationObject synob;

	/**
	 * default constructor.
	 */
	public RuleAlgorithm() {
		super();
	}

	/**
	 * constructor.from a relabeling system.
	 * 
	 * @param r the relabeling system to simulate.
	 */
	public RuleAlgorithm(RelabelingSystem r) {
		super();
		this.setRelSys(r);
	}

	public RuleAlgorithm(RuleAlgorithm algo) {
		this.relSys = algo.relSys;
		//this.synType = algo.synType;
		if (algo.synal != null) {
			this.synal = (SynchronizationAlgorithm) algo.synal.clone();
			//this.synob = this.synal.getSynchronizationObject();
		}
	}

	/**
	 * Sets the message process.
	 * 
	 * @param proc the new message process
	 */
	public void setMessageProcess(MessageProcess proc) {
		this.setProc(proc);
		if (this.synal != null)
			this.synal.setProc(proc);
	}

	public final SynchronizationAlgorithm getSynchronizationAlgorithm() {
		return synal;
	}	

	public abstract Object clone();

	public String toString() {
		return "Abstract Rule" + super.toString();
	}

	/**
	 * return collection of Message Types. message types are defined in
	 * misc.MSG_TYPES. this method is common to all rules simulators.
	 */
	public final Collection<MessageType> getMessageTypeList() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(SimulationConstants.Messages.SYNC);
		typesList.add(SimulationConstants.Messages.LABE);
		typesList.add(SimulationConstants.Messages.TERM);
		if (this.synType == SynCT.LC1) {
			typesList.add(SimulationConstants.Messages.MARK);
		}
		if (this.synType == SynCT.RDV) {
			typesList.add(SimulationConstants.Messages.DUEL);
		}
		return typesList;
	}


	/**
	 * method used when copying (or cloning ) the properties of an
	 * algorithm.
	 * 
	 * @param a
	 */
	/*
	public void copy(RuleAlgorithm a) {
		super.copy(a);
		this.setRelSys(a.getRelSys());
	}
	 */
	// **************** COMMON WITH VISIDIA Distribuee **********************/

	/**
	 * used to print. (for tests).
	 */
	public final String print() {
		return (" RSAlgo: opt=" + this.relSys.userPreferences.toString()
				+ "\n RS=" + this.relSys.toString());
	}

	/**
	 * this method receives from all synchronized neighbors their states, and
	 * update the information in the synob. this method works with all
	 * synchronization algorithms.
	 */
	public final void updateNeigborhoodInfo() {
		int door;
		int i; // i receive the update
		((SynchronizationObjectTerminationRules) synob).setCenterState(this.getProperty("label").toString());

		for (i = 0; i < ((SynchronizationObjectTerminationRules) synob).neighborhood.arity(); i++) {
			door = ((SynchronizationObjectTerminationRules) synob).neighborhood.neighbourDoor(i);
			if (synob.isConnected(door)) {
				Message m = this.receiveFrom(door);
				if (m != null) {
					StringMessage msg = (StringMessage) m;
					Neighbor n = new Neighbor(msg.data(), synob.getMark(door), door);
					((SynchronizationObjectTerminationRules) synob).neighborhood.setState(i, n);
				} else {
					synob.setConnected(i, false);
				}
			}
		}
	}

	/**
	 * send the states (described in synob) to synchronized neighbors. this
	 * method is used by the center after a transformation. Warning: this
	 * method is available only for RDV or LC2 synchronization types. it should
	 * be redefined for LC1.
	 */
	public final void sendUpdate() {
		Neighbor n;
		String c = ((SynchronizationObjectTerminationRules) synob).neighborhood.centerState();
		int i;
		this.setMyState(c);
		// System.out.println("in sendup neigh="+synob.neighbourhood);
		for (i = 0; i < ((SynchronizationObjectTerminationRules) synob).neighborhood.arity(); i++) {
			n = ((SynchronizationObjectTerminationRules) synob).neighborhood.neighbor(i);
			this.setDoorState(new MarkedState(n.mark()), n.doorNum());
			synob.setMark(n.doorNum(), n.mark());
			if (synob.isConnected(n.doorNum())) {
				switch (this.synType) {
				case SynCT.LC1:
					this.sendTo(n.doorNum(), new BooleanMessage(n.mark(), SimulationConstants.Messages.MARK));
					break;
				default:
					this.sendTo(n.doorNum(), new NeighborMessage(n, SimulationConstants.Messages.LABE));
				break;
				}
			}
		}
	}

	/**
	 * send the state (label) to the neighbor. Warning: only RDV and LC2 use
	 * this method ( redefined for LC1).
	 * 
	 * @param neighbour the neighbor door.
	 */
	/*
	public void sendState(int neighbor) {
		if (synob.isConnected(neighbor)) {
			this.sendTo(neighbor, new StringMessage(this.getState()));
		}
	}
	 */
	/**
	 * set the relabeling system.
	 * 
	 * @param rs the new relabeling system.
	 */
	public final void setRelSys(RelabelingSystem rs) {
		this.relSys = rs;
	}

	public final RelabelingSystem getRelSys() {
		return this.relSys;
	}

	// Works with RDV, LC1, and LC2 and supports various options
	/**
	 * It's the algorithm of simulation of relabeling system. Works with RDV,
	 * LC1, and LC2 and supports various options
	 */
	@Override
	public void init() {
		int ruleToApply;
		int round = 0;
		int i = 0;
		// initialisation du synob et de synal
		synob = synal.getSynchronizationObject();		
		synob.init(this.getArity());
		
		//TODO: check if the following line is useful
		//this.synal.set(this);

		while (synob.run) {
			round++;
			synob.reset();
			if (synob.allFinished()) {
				synob.run = false;
			} else {
				this.synal.trySynchronize();
			}
			((SynchronizationObjectTerminationRules) synob).refresh();

			if (synob.isElected()) {
				// Elected node
				// exchanging states
				this.updateNeigborhoodInfo();
				// choosing a rule to apply
				ruleToApply = this.relSys.checkForRule((Star) ((SynchronizationObjectTerminationRules) synob).neighborhood.clone());
				// applying the rule
				if (ruleToApply != -1) {
					int kindOfRule = this.applyRule(ruleToApply);
					this.sendUpdate();
					this.synal.breakSynchro();
					if (this.relSys.userPreferences.manageTerm) {
						this.endRuleAction(kindOfRule);
					}
				} else {
					this.sendUpdate();
					this.synal.breakSynchro();
				}

			} else if (synob.isNotInStar()) {
				if (synob.allFinished()) {
					for (i = 0; i < synob.arity; i++) {
						if (!synob.hasFinished(i)) {
							this.sendTo(i, new IntegerMessage(SynCT.GLOBAL_END, SimulationConstants.Messages.TERM));
						}
					}
					synob.run = false;
				}
			} else if (synob.isInStar()) {
				this.sendMyState();
				// i receive the update
				this.receiveAndUpdateMyState();
				if (synob.allFinished()) {
					for (i = 0; i < synob.arity; i++) {
						if (!synob.hasFinished(i)) {
							this.sendTo(i, new IntegerMessage(SynCT.GLOBAL_END, SimulationConstants.Messages.TERM));
						}
					}
					synob.run = false;
				}
			}
		}
	}

	/**
	 * this method do actions depending of the kind of the rule.
	 * 
	 * @param kindOfRule possible values defined in class SynCT.
	 */
	public final void endRuleAction(int kindOfRule) {
		switch (kindOfRule) {
		case (SynCT.GLOBAL_END): {
			// System.out.println("\n!->TERMINATION GLOBAL: Node"+getId()+"says:
			// Global END !!! *****");
			for (int i = 0; i < synob.arity; i++) {
				if (!synob.hasFinished(i)) {
					synob.setFinished(i, true);
					this.sendTo(i, new IntegerMessage(SynCT.GLOBAL_END, SimulationConstants.Messages.TERM));
					// Message de term doit etre recu au debut de la synchron
				}
			}
			synob.run = false;
			break;
		}
		case (SynCT.LOCAL_END): {
			// System.out.println("\n!-> TERMINATION LOCAL: Node"+getId()+"says:
			// I have finished by by *****");
			for (int i = 0; i < synob.arity; i++) {
				if (!synob.hasFinished(i)) {
					this.sendTo(i, new IntegerMessage(SynCT.LOCAL_END, SimulationConstants.Messages.TERM));
				}
			}
			synob.run = false;
			break;
		}
		}
	}

	/* for RDV LC2,but LC1 should redefine it */
	/**
	 * send the label to the star center if he is connected. in LC1 they are
	 * many centers, so this methode is redefined in LC1Rule.
	 */
	public final void sendMyState() {
		// System.out.println("LC2 ou rdv");
		if (synob.isConnected(synob.center)) {
			this.sendTo(synob.center, new StringMessage(this.getProperty("label").toString(), SimulationConstants.Messages.LABE));
		}
	}

	/**
	 * for RDV LC2, only LC1 should redefine it
	 * 
	 */
	public final void receiveAndUpdateMyState() {
		if (synob.isConnected(synob.center)) {
			Message m = this.receiveFrom(synob.center);
			// System.out.println ("untel " + getId() + " a recu de " +
			// synob.center + " " +
			// synob.isConnected(0) + " " + synob.isConnected(1));
			if (m != null) {
				NeighborMessage msg = (NeighborMessage) m;
				this.setMyState(msg.label());
				// marking the Arrow;
				this.setDoorState(new MarkedState(msg.mark()), synob.center);
				synob.setMark(synob.center, msg.mark());
			}
		}
	}

	/**
	 * get user preferences
	 * 
	 * @return the RSOptions
	 */
	public final RSOptions getRSOptions() {
		return this.relSys.userPreferences;
	}

	/**
	 * return the label of the node.
	 */
	public final String getState() {
		return this.getProperty("label").toString();
	}

	public final void setMyState(String newState) {
		this.putProperty("label", newState);
	}

	/**
	 * this method applies the rule on position i. the contexts is in synob,
	 * modifications are also done in synob.
	 * 
	 * @param i position of the rule.
	 * @return the rule type
	 */
	public final int applyRule(int i) {
		int retour;

		Rule r = (Rule) this.relSys.getRule(i).clone();
		retour = r.getType();
		Star b = new Star(r.befor());
		Star a = (Star) r.after().clone();

		if (((Star) ((SynchronizationObjectTerminationRules) synob).neighborhood.clone()).contains(b)) {
			a.setDoors(b);
			((SynchronizationObjectTerminationRules) synob).neighborhood.setStates(a);
		} else {
			// lever exception
			// System.out.println("regle non app");
		}
		return retour;
	}

	/**
	 * get help about the relabeling system
	 */
	public final String getDescription() {
		return this.relSys.getDescription();
	}

	// PFA2003
	/*
	public boolean isRunning() {
		return synob.run;
	}
	 */
}
