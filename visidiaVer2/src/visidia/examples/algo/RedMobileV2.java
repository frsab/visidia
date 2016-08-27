package visidia.examples.algo;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;
import java.util.Vector;

import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.messages.StringMessage;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.process.algorithm.SensorSyncAlgorithm;
import visidia.simulation.process.messages.StringMessage;

public class RedMobileV2 extends SynchronousAlgorithm {
	private static UUID uniqueKey;   
	private static final long serialVersionUID = 8437452624823359954L;
	private static final int NoWitnessPoints = 1;
	private static double THRESHOLD = 0.90;
	public static Boolean cloneDetected = new Boolean(false);
	public static final Double INF = new Double(1 << 20);
	protected static Double xMax = new Double(-INF);
	protected static Double yMax = new Double(-INF);
	protected static Double xMin = new Double(+INF);
	protected static Double yMin = new Double(+INF);
	protected HashMap<Integer,Point> neighborLocs;
	private static Vector<Point> witnessPoints = new Vector<Point>();
	protected Random rand = new Random();
	private WitnessCacheMobile cache= new WitnessCacheMobile();
	Stack<MobileSensorMessage> claims = new Stack<MobileSensorMessage>();
	private String UID;
	private Point lastPos = new Point();


	private static String originalUID_Node;

	@Override
	public Object clone() {
				return new RedMobileV2();
	}

	@Override
	public void init() {

	}

}
