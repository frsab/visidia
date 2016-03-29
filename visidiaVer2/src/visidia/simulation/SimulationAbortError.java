package visidia.simulation;

// TODO: Auto-generated Javadoc
/**
 * An error that is thrown when an aborted thread try to access simulation API.
 */
public class SimulationAbortError extends Error {

	private static final long serialVersionUID = 1021261555309603500L;

	/**
	 * Instantiates a new simulation abort error.
	 */
	public SimulationAbortError() {
		super();
	}

	/**
	 * Instantiates a new simulation abort error.
	 * 
	 * @param cause the cause
	 */
	public SimulationAbortError(Throwable cause) {
		super(cause);
	}
	
}
