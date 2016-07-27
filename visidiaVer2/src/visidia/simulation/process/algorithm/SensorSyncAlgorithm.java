package visidia.simulation.process.algorithm;

import java.util.Enumeration;

import visidia.graph.Sensor;
import visidia.graph.SensorGraph;
import visidia.graph.SupportVertex;
import visidia.graph.Vertex;
import visidia.simulation.Console;
import visidia.simulation.SimulationConstants.SimulationStatus;
import visidia.simulation.command.AddEdgeCommand;
import visidia.simulation.command.DisplaySensorNumberCommand;
import visidia.simulation.command.MoveSensorCommand;
import visidia.simulation.command.RemoveEdgeCommand;
import visidia.simulation.command.SetSensorNumberCommand;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * This is the abstract base class representing a synchronous algorithm for sensors in visidia.
 * 
 * It is the API to be used to implement new synchronous algorithms for sensors (extending SensorSyncAlgorithm class).
 */
public abstract class SensorSyncAlgorithm extends SynchronousAlgorithm {

	private static final long serialVersionUID = -1759265985678508606L;

	/**
	 * Moves a sensor to a support vertex chosen by the mover.
	 * 
	 * @param sensorId the sensor id
	 */
	public final void move(int sensorId) {
		//waiting for all the Vertex to want to move.
		this.nextPulseStopDisplay();
		//moving
		SensorGraph sensorGraph = (SensorGraph) this.getProc().getServer().getConsole().getGraph();
		Sensor sensor = (Sensor) sensorGraph.getVertex(sensorId);
		SupportVertex svertexFrom = sensor.getSupportVertex();
		SensorMover mover = sensor.getSensorMover();
		try {
			if (mover == null) mover = sensorGraph.getGlobalSensorMover();
			mover.move(sensor);
			SupportVertex svertexTo = sensor.getSupportVertex();

			MoveSensorCommand cmd = new MoveSensorCommand(sensor.getId(), svertexFrom.getId(), svertexTo.getId());
			getProc().getServer().sendToConsole(cmd);
		} catch (Exception e) {
		}
		//waiting for all the Vertex to finish moving.
		int last = this.nextPulse();
		if (last == 0) {
			//Update the Edges modified by the sensors'movements.
			this.updateGraphEdges();
		}
		//all the algorithm wait for the one which do the updateEdges
		this.nextPulse();
	}

	/**
	 * Moves a sensor after algorithm end.
	 * 
	 * @param sensorId the sensor id
	 */
	public final void moveAfterEnd(int sensorId) {
		Console console = getProc().getServer().getConsole();
		int terminatedAlgoStillMovingCount = console.getTerminatedAlgoStillMovingCount() + 1;
		console.setTerminatedAlgoStillMovingCount(terminatedAlgoStillMovingCount);

		while((console.getGraph().order() - console.getTerminatedThreadCount() - terminatedAlgoStillMovingCount) > 0 && (console.getStatus() != SimulationStatus.ABORTED)) {
			move(sensorId);
		}
	}
	
	/*************************************/
	/* METHODS NOT REFERENCED IN THE API */
	/*        (INTERNAL USE ONLY)        */
	/*************************************/

	/**
	 * Sensor number display on/off event.
	 * 
	 * @param display the display
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	private void pushSensorNumberDisplayOnOffEvent(boolean display) throws InterruptedException {
		DisplaySensorNumberCommand cmd = new DisplaySensorNumberCommand(display);
		getProc().getServer().sendToConsole(cmd);
	}

	/**
	 * Is used in sensor simulation.
	 * Make the same thing as nextPulse and stop the display of the number of sensor on a node in the gui.
	 */
	private void nextPulseStopDisplay() {
		if (nextPulse() == 0)
			try {
				this.pushSensorNumberDisplayOnOffEvent(false);
			} catch (InterruptedException e) {
			}
	}

	/**
	 * Update graph edges after sensors move.
	 */
	private void updateGraphEdges() {
		// For each sensor S, we compute the distance (in number of edges on support graph) between S and each other sensor.
		// For each pair of sensors:
		// If the distance is > threshold and if sensors were connected, they are detached.
		// If the distance is <= threshold and if the sensors were not connected, they are attached.
		// If nothing is done.

		// get sensors
		SensorGraph sensorGraph = (SensorGraph) getProc().getServer().getConsole().getGraph();
		int nbSensors = sensorGraph.order();
		Sensor[] sensors = new Sensor[nbSensors];
		int cpt = 0;
		Enumeration<Vertex> elements = sensorGraph.getVertices();
		while (elements.hasMoreElements()) sensors[cpt++] = (Sensor) elements.nextElement();
			
		// update graph edges
		int sensorCommunicationDistance = sensorGraph.getSensorCommunicationDistance();

		for (int i = 0; i < nbSensors; ++i) {
			Sensor sensor1 = sensors[i];
			int[] distances = sensorGraph.getSupportGraph().computeDistancesFrom(sensor1.getSupportVertex());
			for (int j = i+1; j < nbSensors; ++j) {
				Sensor sensor2 = sensors[j];
				boolean connectedSensors = (sensor1.getEdge(sensor2) != null);

				if (distances[sensor2.getSupportVertex().getId()] > sensorCommunicationDistance) {
					if (connectedSensors) {
						RemoveEdgeCommand cmd = new RemoveEdgeCommand(sensor1.getId(), sensor2.getId());
						getProc().getServer().sendToConsole(cmd);
					}
				} else {
					if (!connectedSensors) {
						AddEdgeCommand cmd = new AddEdgeCommand(sensor1.getId(), sensor2.getId());
						getProc().getServer().sendToConsole(cmd);
					}					
				}
			}

			SupportVertex supportVertex = sensor1.getSupportVertex();
			if (supportVertex.isTheLastSensor(sensor1)) {
				int nbHostedSensors = supportVertex.getNbHostedSensors();
				if (nbHostedSensors > 1) {
					SetSensorNumberCommand cmd = new SetSensorNumberCommand(supportVertex.getId());
					getProc().getServer().sendToConsole(cmd);
				}
			}
		}

		try { 
			//message to allow to display the number of sensors on nodes
			pushSensorNumberDisplayOnOffEvent(true);
		} catch(InterruptedException e) {
		}
	}

}
