package visidia.simulation.process.algorithm;

import visidia.simulation.Console;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.command.NewPulseCommand;
import visidia.simulation.process.criterion.DoorPulseCriterion;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessagePacket;

// TODO: Auto-generated Javadoc
/**
 * This is the abstract base class representing a synchronous algorithm in visidia.
 * 
 * It is the API to be used to implement new synchronous algorithms (extending SynchronousAlgorithm class).
 * 
 * 
 * A synchronous algorithm (written by the user) must have the following
 * skeleton <br>
 * BEGIN<br>
 * FOR EVERY PHASE i DO: <br>
 * 1 * actions to do in phase i : <br>
 * action 0 : ... <br>
 * action 1 : ... <br>
 * <br>
 * ... <br>
 * <br>
 * action k : sendTo(...) receive(...) getMsg...(...), anyMsg...(),
 * putProperty() ... ... ;<br>
 * <br>
 * ... <br>
 * ... <br>
 * <br>
 * final action : <br>
 * <br>
 * 2 * nextPulse(); // the nodes declares that he has finished the actions to do
 * in phase i <br> // it is blocked until all other nodes have finished their
 * actions in phase i <br> // if the node v does not declare the end of its
 * current phase then all other nodes <br> // will be blocked until the node v
 * execute the nextPulse() method.<br> // the receive method automatically
 * execute the nextPulse() method if no message <br> // have been found.<br>
 * END DO <br>
 * END <br>
 * <br>
 * The main task of this class when sending a message is to set the <br>
 * message clock with the value returned by the getPulse method of <br>
 * class Algorithm. <br>
 * 
 * the receive(...) and getMsg...(...) methods are more sophisticated and <br>
 * the user should take a look. Note that he can always do without by using the
 * <br>
 * existMessage(...) and getMessage(...) methods of class SyncAlgorithm
 */
public abstract class SynchronousAlgorithm extends Algorithm {

	private static final long serialVersionUID = -6943925552687958218L;

	/***************************/
	/* SYNCHRONIZATION METHODS */
	/***************************/

	/**
	 * Gets the current pulse.
	 * 
	 * @return the pulse
	 */
	public final int getPulse() {
		return this.getProc().getServer().getConsole().getPulse();
	}

	/**
	 * Make the node calling this function finish it's pulse. The node wait's
	 * until all the nodes have called this function. The last node calling
	 * the nextPulse function free the other and make the simulator to
	 * continue to a new pulse.
	 * 
	 * @return the countNextPulse
	 */
	public final int nextPulse() {
		int AmITheLastOne = 1; //if just AmITheLastOne; -> error might not have been initialized
		try {
			Console console = this.getProc().getServer().getConsole();
			Object lockSync = console.getLockSyncObject();
			synchronized (lockSync) {
				int countNextPulse = (console.getCountNextPulse() + 1) % (console.getNbProcesses() - console.getTerminatedThreadCount());
				console.setCountNextPulse(countNextPulse);
				AmITheLastOne = countNextPulse;
				if (countNextPulse == 0) {
					try {
						NewPulseCommand cmd = new NewPulseCommand(getProc().getId(), this.getPulse());
						getProc().getServer().sendToConsole(cmd);
					} catch (Exception e) {
					}
					console.setPulse(this.getPulse() + 1);
				}
				lockSync.wait();
			}
		} catch (Exception e) {
		}
		return AmITheLastOne;
	}

	/*************************/
	/* COMMUNICATION METHODS */
	/*************************/

	/**
	 * Send the message message on target door.
	 * 
	 * @param door the door
	 * @param msg the message
	 * 
	 * @return true, if the message has been sent
	 */
	protected boolean sendTo(int door, Message msg) {
		msg.setMsgClock(this.getPulse());
		return super.sendTo(door, msg);
	}

	/**
	 * The most general and powerful method to handle reception of messages. It
	 * returns the message which matches the criterion dpc in the message queue
	 * of the node. If none then return null. The user should take a look at the
	 * DoorPulseCriterion class to learn more about how to initialize the dpc
	 * argument. <br>
	 * Note that if the Pulse field of dpc is set to be this.getPulse(), then
	 * the user can retrieve the messages that have been sent at the current
	 * phase and which arrives before its end. This is not allowed in the
	 * anyMsg... method (a verification is made). In fact, we consider that the
	 * only messages a node can retrieve are those sent in the previous pulses.
	 * We do not make any verification here, in order to let the user implement
	 * ad-hoc instructions if he want. Thus, use this method carefully.
	 */
	protected final Message getNextMessage(DoorPulseCriterion dpc) {
		try {
			this.getProc().runningControl();
			return this.getNextMessageCriterion(dpc);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	/**
	 * return the first message arrived in the previous pulse and write the door
	 * number in the Door object. If there is no message then return null.
	 */
	protected final Message receive(Door door) {
		DoorPulseCriterion dpc = new DoorPulseCriterion(this.getPulse() - 1);
		Message msg = this.getNextMessage(dpc);
		if (msg != null) {
			door.setNum(dpc.getDoor());
			return msg;
		}
		return msg;
	}

	/**
	 * return true if there exists a message that matches the dpc criterion in
	 * the message queue of the node. The same remark than in the
	 * getNextMessage(...) method, holds when the pulse in dpc is set to be
	 * this.getPulse()
	 */
	protected final boolean existMessage(DoorPulseCriterion dpc) {
		try {
			this.getProc().runningControl();
			return !this.getProc().emptyVQueue(dpc);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
	}

	/**
	 * return true if the node has received any message which has been sent in
	 * the previous pulse
	 * 
	 */
	protected final boolean anyMsg() {
		DoorPulseCriterion dpc = new DoorPulseCriterion(this.getPulse() - 1);
		return this.existMessage(dpc);
	}

	/*************************************/
	/* METHODS NOT REFERENCED IN THE API */
	/*        (INTERNAL USE ONLY)        */
	/*************************************/

	/**
	 * Gets the next message corresponding to the criterion.
	 * 
	 * @param dpc the door pulse criterion
	 * 
	 * @return the next message corresponding to the criterion if any; else null
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	private Message getNextMessageCriterion(DoorPulseCriterion dpc) throws InterruptedException {
		MessagePacket msgPacket = (MessagePacket) this.getProc().getNextMessagePacketNoWait(dpc);

		if (msgPacket != null) {
			dpc.setDoor(msgPacket.receiverDoor());
			dpc.setPulse(msgPacket.message().getMsgClock());
			return msgPacket.message();
		}

		return null;
	}

}
