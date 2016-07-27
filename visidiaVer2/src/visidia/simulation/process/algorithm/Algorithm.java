package visidia.simulation.process.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.misc.property.PropertyTable;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.SimulationConstants.PropertyStatus;
import visidia.simulation.process.MessageProcess;
import visidia.simulation.process.criterion.CompoundCriterion;
import visidia.simulation.process.criterion.DoorCriterion;
import visidia.simulation.process.criterion.MessageCriterion;
import visidia.simulation.process.criterion.MessagePacketCriterion;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.step.MarkedStep;
import visidia.simulation.process.step.PropertyStep;
import visidia.simulation.process.step.Step;
import visidia.simulation.process.step.SyncStep;

// TODO: Auto-generated Javadoc
/**
 * This is the abstract base class representing an algorithm in visidia.
 * 
 * It is the API to be used to implement new algorithms (extending Algorithm class).
 */
public abstract class Algorithm implements Cloneable, Runnable, Serializable {

	private static final long serialVersionUID = -2918434583960346926L;

	/** The process. */
	private transient MessageProcess proc = null;

	protected transient Vertex vertex;
	protected transient ArrayList<Integer> activeDoors;
	protected transient PropertyTable properties; 

	protected transient boolean working = true ;
	protected transient boolean globalTermDetected = false;

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public abstract Object clone();

	/**
	 * This method is executed by the node. The user has to overwrite it to
	 * implement its own algorithm using message passing.
	 */
	public abstract void init();

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return "No description for this algorithm.";
	}

	/**
	 * Gets the message type list.
	 * 
	 * @return the message type list
	 */
	public Collection<MessageType> getMessageTypeList() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(MessageType.defaultMessageType);
		return typesList;
	}

	/*******************/
	/* GENERAL METHODS */
	/*******************/

	/**
	 * Returns the node arity (its degree, or number of neighbors).
	 * 
	 * @return the arity
	 */
	protected final int getArity() {
		if (getProc() == null) return 0;
		getProc().runningControl();
		return getProc().getVertex().degree();
	}

	/**
	 * Returns the node identity.
	 * 
	 * @return the id
	 */
	protected final int getId() {
		if (getProc() == null) return 0;
		getProc().runningControl();
		return getProc().getVertex().getId();
	}

	/**
	 * Gets the net size. Provided for convenience, since an algorithm is not supposed to know graph global information.
	 * 
	 * @return the net size
	 */
	protected final int getNetSize() {
		try {
			return getProc().getServer().getConsole().getGraph().order();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Gets the oriented doors of the current node. Both incoming and outgoing doors are considered.
	 * 
	 * @return the oriented doors
	 */
	protected final Enumeration<Integer> getOrientedDoors() {
		if (getProc() == null) return null;
		Vertex vertex = getProc().getVertex();
		Vector<Integer> oriented = new Vector<Integer>();
		int degree = vertex.degree();

		for (int i = 0; i < degree; ++i) {
			Vertex v = vertex.getNeighborByDoor(i);
			if (vertex.getEdge(v).isOriented())
				oriented.add(new Integer(i));
		}

		return (oriented.size() == 0 ? null : oriented.elements());
	}

	/**
	 * Returns true if the door corresponds to an edge pointing to the current node.
	 * Also returns true if the edge is not oriented.
	 * 
	 * @param door the door
	 * 
	 * @return true, if is incoming door
	 */
	protected final boolean isIncomingDoor(int door) {
		if (getProc() == null) return false;
		Vertex vertex = getProc().getVertex();
		Vertex v = vertex.getNeighborByDoor(door);
		Edge edge = vertex.getEdge(v);
		return (!edge.isOriented() || edge.getDestination().equals(vertex));
	}

	/**
	 * Returns true if the door corresponds to an edge leaving the current node.
	 * Also returns true if the edge is not oriented.
	 * 
	 * @param door the door
	 * 
	 * @return true, if is outgoing door
	 */
	protected final boolean isOutgoingDoor(int door) {
		if (getProc() == null) return false;
		Vertex vertex = getProc().getVertex();
		Vertex v = vertex.getNeighborByDoor(door);
		Edge edge = vertex.getEdge(v);
		return (!edge.isOriented() || edge.getOrigin().equals(vertex));
	}

	/**
	 * Returns true if the vertex has no active neighbor, that is, it is either not connected 
	 * to any other vertex or all of its neighbors have terminated the execution of the algorithm
	 * 
	 * @return true, true if the vertex is isolated
	 */
	protected final boolean isIsolated(){
		return activeDoors.isEmpty();
	}

	/*******************************/
	/* WHITEBOARD&PROPERTY METHODS */
	/*******************************/

	/**
	 * Put tagged property.
	 *
	 * @param key the key
	 * @param value the value
	 * @param tag the tag
	 */
	private void putTaggedProperty(String key, Object value, int tag) {
		if (getProc() == null) return;
		try {
			VisidiaProperty prop = new VisidiaProperty(key, value, tag);
			getProc().setNodeProperty(getProc().getId(), prop);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	/**
	 * Adds a property to this node. If <code>value</code> is null the property is removed.
	 *
	 * @param key the key
	 * @param value the value
	 * @param status the status
	 */
	protected final void putProperty(String key, Object value, int status) {
		putTaggedProperty(key, value, VisidiaProperty.getPropertyTagFromStatus(status));
	}

	/**
	 * Put property.
	 *
	 * @param key the key
	 * @param value the value
	 */
	protected final void putProperty(String key, Object value) {
		if (getProc() == null) return;
		Vertex v = getProc().getVertex();
		VisidiaProperty prop = v.getVisidiaProperty(key);
		if (prop == null) putTaggedProperty(key, value, VisidiaProperty.Tag.USER_PROPERTY);
		else putTaggedProperty(key, value, prop.getTag());
	}

	/**
	 * Gets the node property value associated to the key.
	 * 
	 * @param key the key
	 * 
	 * @return the property value, or null if the key does not exist.
	 */
	protected final Object getProperty(String key) {
		if (getProc() == null) return null;
		return getProc().getNodeProperty(key);
	}

	/**
	 * Gets the node's sharable property value associated to the key.
	 * 
	 * @param key the key
	 * 
	 * @return the property value, or null if the key does not exist.
	 */
	protected final Object getLocalProperty(Object key) {
		return properties.getValueOf(key);
	}

	/**
	 * Put property.
	 *
	 * @param key the key
	 * @param value the value
	 */
	protected final void setLocalProperty(Object key, Object value) {
		if (getProc() == null) return;
		properties.setValue(key, value);
		try {
			VisidiaProperty prop = new VisidiaProperty(key, value, VisidiaProperty.Tag.USER_PROPERTY);
			getProc().setNodeProperty(getProc().getId(), prop);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	/**
	 * Put property.
	 *
	 * @param key the key
	 * @param value the value
	 * @param tag the tag
	 */
	protected final void setLocalProperty(Object key, Object value, int tag) {
		properties.setValue(key, value, tag);
		if (tag == PropertyStatus.DISPLAYED) {
			putTaggedProperty((String)key, value, tag);
		}
	}


	protected final void setLocalProperty(PropertyTable propertyTable) {

		if (propertyTable != null) {
			Set<Object> keys = propertyTable.getPropertyKeys();
			Iterator iterator = keys.iterator();

			while (iterator.hasNext()) {
				Object key = iterator.next();
				setLocalProperty(key, propertyTable.getValueOf(key));
			}
		}

	}

	/**
	 * Gets the edge property value.
	 * 
	 * @param door the door
	 * @param key the key
	 * 
	 * @return the property value, or null if the key does not exist.
	 */
	protected final Object getEdgeProperty(int door, Object key) {
		if (getProc() == null) return null;
		return getProc().getEdgeProperty(getProc().getVertex(), door, key);
	}

	/**
	 * Sets the edge property.
	 * 
	 * @param door the door
	 * @param key the key
	 * @param value the value
	 */
	protected final void setEdgeProperty(int door, Object key, Object value) {
		if (getProc() == null) return;
		getProc().setEdgeProperty(getProc().getVertex(), door, key, value);
	}

	/**
	 * Sets the edge property.
	 * 
	 * @param door the door
	 * @param key the key
	 * @param value the value
	 * @param tag the tag
	 */
	protected final void setEdgeProperty(int door, Object key, Object value, boolean displayable) {
		if (getProc() == null) return;
		getProc().setEdgeProperty(getProc().getVertex(), door, key, value, displayable);
	}

	/*************************/
	/* VISUALIZATION METHODS */
	/*************************/

	/**
	 * Sets the door state.
	 * 
	 * @param st the new state
	 * @param door the door
	 */

	protected final void setDoorState(EdgeState st, int door) {
		if (getProc() == null) return;
		getProc().runningControl();
		try {
			getProc().changeEdgeState(getProc().getVertex(), door, st);

			if(st.getClass()== MarkedState.class)
				addMarkStep(door);

		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	protected final void addStep(Step st, Object obj,Object obj2){
		if (getProc() == null) return;
		getProc().runningControl();
		try {
			getProc().executeStep(getProc().getVertex(), obj, obj2, st);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}

	}

	protected final void addMarkStep(int door){

		addStep(new MarkedStep(), door,null);
	}

	protected final void addPropertyStep(int door, Object property){

		addStep(new PropertyStep(), door, property);
	}

	protected final void addPropertyStep(Object property){

		addStep(new PropertyStep(), null, property);
	}

	protected final void addSync_LC0_Step(int door){

		addStep(new SyncStep(SyncStep.LC0), door, null);
	}

	protected final void addSync_LC1_Step(int door){

		addStep(new SyncStep(SyncStep.LC1), door, null);
	}

	protected final void addSync_LC2_Step(int door){

		addStep(new SyncStep(SyncStep.LC2), door, null);
	}

	/*************************/
	/* COMMUNICATION METHODS */
	/*************************/

	/**
	 * Sends the message message on target door.
	 * 
	 * @param door the door
	 * @param msg the message
	 * 
	 * @return true, if the message has been sent
	 */
	protected boolean sendTo(int door, Message msg) {
		if (getProc() == null) return false;
		getProc().runningControl();
		boolean b;
		try {
			b = getProc().sendMessageTo(door, msg);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
		return b;
	}

	/**
	 * Sends the message message to all neighbors.
	 * 
	 * @param msg the message
	 */
	protected final void sendAll(Message msg) {
		int arity = this.getArity();
		for (int i = 0; i < arity; i++) {
			this.sendTo(i, msg);
		}
	}

	/**
	 * Gets the first message arriving on target door.
	 * The algorithm is blocked until reception.
	 * 
	 * @param door the door
	 * 
	 * @return the message
	 */
	protected final Message receiveFrom(int door) {
		if (getProc() == null) return null;
		getProc().runningControl();
		Message msg = null;
		try {
			msg = getProc().getNextMessage(null, new DoorCriterion(door));
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
		return msg;
	}

	/**
	 * Gets the first message arriving on target door that matches the criterion.
	 * The algorithm is blocked until reception.
	 * 
	 * @param door the door
	 * @param mc the message criterion
	 * 
	 * @return the message
	 */
	protected final Message receiveFrom(int door, MessageCriterion mc) {
		getProc().runningControl();

		DoorCriterion dc = new DoorCriterion(door);
		MessagePacketCriterion mpc = new MessagePacketCriterion(mc);
		CompoundCriterion c = new CompoundCriterion();
		c.add(dc);
		c.add(mpc);

		Message msg = null;
		try {
			msg = getProc().getNextMessage(null, c);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}

		return msg;
	}

	/**
	 * Gets the first message arriving on the node. The algorithm
	 * is blocked until reception. The door on which the message
	 * is arrived is stored.
	 * 
	 * @param door the door
	 * 
	 * @return the message
	 */
	protected Message receive(Door door) {
		getProc().runningControl();
		Message msg = null;
		try {
			msg = getProc().getNextMessage(door, null);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}

		return msg;
	}

	/*************************************/
	/* METHODS NOT REFERENCED IN THE API */
	/*        (INTERNAL USE ONLY)        */
	/*************************************/

	/**
	 * Sets the message process.
	 * 
	 * @param proc the new message process
	 */
	public void setMessageProcess(MessageProcess proc) {
		this.setProc(proc);
	}

	/**
	 * This method is used at the thread instantiation. This method launches the init method.
	 */
	public final void run() {

		vertex = getProc().getVertex();
		properties = new PropertyTable();

		this.init();
		try {
			getProc().terminatedAlgorithm();
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	public MessageProcess getProc() {
		return proc;
	}

	public void setProc(MessageProcess proc) {
		this.proc = proc;
	}

}
