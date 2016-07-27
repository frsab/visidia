package visidia.examples.algo;

import java.util.UUID;
import java.awt.Point;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import visidia.simulation.process.algorithm.SensorSyncAlgorithm;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.StringMessage;

public class RedMobile extends SensorSyncAlgorithm {

	/**
	 * 
	 */
	private static UUID uniqueKey; //= UUID.randomUUID(); 
	  
	private static final long serialVersionUID = 8437452624823359954L;
	private static final int NoWitnessPoints = 1;
	private static double THRESHOLD = 0.90;
	public static Boolean cloneDetected = new Boolean(false);
	public static final Double INF = new Double(1 << 20);
	protected static Double xMax = new Double(-INF);
	protected static Double yMax = new Double(-INF);
	protected static Double xMin = new Double(+INF);
	protected static Double yMin = new Double(+INF);
	protected Vector<Point> neighborLocs;
	private static Vector<Point> witnessPoints = new Vector<Point>();
	protected Random rand = new Random();
	private WitnessCache cache= new WitnessCache();
	Stack<MobileSensorMessage> claims = new Stack<MobileSensorMessage>();
	private String UID;
	private Point lastPos = new Point();
	private Random randMove = new Random();
	private Point pos;

	@Override
	public Object clone() {
		return new RedMobile();
	}

	@Override
	public void init() {
		
		generateUID();
		this.nextPulse();		
		setConfiguration();
		this.nextPulse();
		setUpRouting();
		this.nextPulse();
		fixWitnessPoints();
		this.nextPulse();
		makeFirstMessage();
		this.nextPulse();

		while (!cloneDetected) {

			move(randMove);
			this.nextPulse();
			updateNeighborLocs();
			this.nextPulse();
			sendMessage();
			this.nextPulse();
			receiveMessage();
			this.nextPulse();
		}
	}

	private void generateUID() {
		//synchronized (uniqueKey) {
			uniqueKey = UUID.randomUUID();
			this.setUID(uniqueKey.toString());
		//}
	}

	private void updateNeighborLocs() {
		neighborLocs.removeAllElements();
		int arity = this.getArity();
		for (int i = 0; i < arity; i++)
			this.neighborLocs.addElement(this.vertex.getNeighborByDoor(i).getPos());
	}

	private void receiveMessage() {
		Door d = new Door();
		while (this.anyMsg()) {

			MobileSensorMessage msg = (MobileSensorMessage) this.receive(d);
			//System.out.println(this.getId()+" "+msg);
			if (msg.getDest().equals(new Point(-1, -1))) {
				//System.out.println("witnessPoints size=" + witnessPoints.size());
				for (Point destFinal : witnessPoints) {
					this.claims.push(new MobileSensorMessage(msg, destFinal));
				}
				cache.addClaim(msg);
			} else {
				this.claims.push(new MobileSensorMessage(msg));
			}

		}

	}

	private void sendMessage() {
		MobileSensorMessage m = null;
		if (getArity() >1) {

			/*
			 * if (!this.claims.empty()) m = (SensorMessage) claims.pop();
			 */
			while (!this.claims.empty()) {
				m = (MobileSensorMessage) claims.pop();
				/*
				 * //System.out.println(this.getId()+"message=" + m);
				 * m.setLastNodePosition(this.vertex.getPos());
				 * //System.out.println(this.getId()+"message=" + m);
				 */
				if (m.getDest().equals(new Point(-1, -1))) {
					sendAll(m);
				}
				/*
				 * if if (m.getLastNodePosition().equals(new Point(-1, -1))) for
				 * (int i = 0; i < this.getArity(); i++) { this.sendTo(i, m); }
				 * 
				 * else
				 */
				else {
					snedGPSR(m);

				}

			}
		}
	}

	private void snedGPSR(MobileSensorMessage msg) {
		Point finalDestRrouting = msg.getDest();
		int destDoor = 
				this.getClosestDoor(finalDestRrouting, msg.getLastNodePosition());
		if (destDoor != -1) {
			this.sendTo(destDoor, new MobileSensorMessage((MobileSensorMessage) msg));
		}
	}

	private int getClosestDoor(Point finalDestRrouting, Point lastNodePosition) {
		/*************************************************************/
		if (finalDestRrouting.distance(this.vertex.getPos()) == 0)// stop
																	// routing
																	// if the
																	// message//
																	// arrived
			return -1;
		/***************** GREEDY ROUTING *************************/

		//int arity = this.getArity();
		int srcDoor = getSrcDoor(lastNodePosition);
		int nearestNodeDoor = getNearestNodeDoor(srcDoor, finalDestRrouting);
		if (nearestNodeDoor != -1)
			return nearestNodeDoor;// success Greedy Routing

		/***************** PERIMETER ROUTING *************************/
		else {// failure Greedy Routing -->trying Perimeter Routing (GPSR)-->we
				// need a lastNode position

			if (srcDoor == -1) {
				return srcDoor;
			} else {
				int minAngleDoor = getMinAngleDoor(srcDoor, finalDestRrouting, lastNodePosition);

				return minAngleDoor;

			}

		}
	}

	private int getMinAngleDoor(int srcDoor, Point finalDestRrouting, Point lastNodePosition) {
		double minAngleValue = 361;// Double.MAX_VALUE;//this.getDist(p,this.pos);
		int minAngleDoor = -1;

		for (int i = 0; i < neighborLocs.size(); i++) {
			if (i != srcDoor) {
				double currentAngle = 361;
				try {
					currentAngle = angleBetweenTwoPointsWithFixedPoint(this.vertex.getPos(),
							neighborLocs.elementAt(srcDoor), neighborLocs.elementAt(i));
					// //System.out.println(i+" : "+currentAngle);
					if (minAngleValue > currentAngle) {
						minAngleDoor = i;
						minAngleValue = currentAngle;
					}
				} catch (NullVectorExceotion e) {
					currentAngle = 361;
				}

			}
		}
		return minAngleDoor;
	}

	private Double angleBetweenTwoPointsWithFixedPoint(Point fixed, Point p1, Point p2) throws NullVectorExceotion {
		double a1 = angleVector(fixed, p1);
		double a2 = angleVector(fixed, p2);
		Double angle = null;
		angle = a2 - a1;
		if (angle >= 0)
			angle = 360 - angle;
		else if (angle < 0)
			angle *= -1;
		return angle;
	}

	private double angleVector(Point o, Point m) throws NullVectorExceotion {

		long x = (long) (m.getX() - o.getX());
		long y = (long) (m.getY() - o.getY());
		double t = 0;
		if (x == 0) {
			if (y > 0) {
				return Math.toDegrees(Math.PI / 2);
			} else if (y < 0) {
				return Math.toDegrees(-(Math.PI / 2));
			} else {
				throw new NullVectorExceotion("zero vector");
			}
		} else {
			t = Math.atan2(y, x);
		}
		return Math.toDegrees(t);
	}

	private int getNearestNodeDoor(int srcDoor, Point finalDestRrouting) {
		int nearestNodeDoor = -1;
		double minDist = Double.MAX_VALUE;
		double distanceFromFinalDest = this.vertex.getPos().distance(finalDestRrouting);

		for (int i = 0; i < neighborLocs.size(); i++) {
			if (i != srcDoor) {
				minDist = Math.min(minDist, finalDestRrouting.distance(neighborLocs.elementAt(i)));
				nearestNodeDoor = i;
			}
		}
		if (minDist > distanceFromFinalDest)
			return -1;
		return nearestNodeDoor;

	}

	private int getSrcDoor(Point lastNodePosition) {
		int srcDoor = -1;
		for (int i = 0; i < neighborLocs.size(); i++) {
			if (lastNodePosition.equals(neighborLocs.elementAt(i))) {
				srcDoor = i;
				// minDist = this.getDist(p,neighborLocs.elementAt(i));
			}
		}
		return srcDoor;

	}

	private void makeFirstMessage() {
		String label = this.getProperty("label").toString();

		if ((label.equals("P")) || (label.equals("N"))) {
			String uid = this.getUID();
			Point srcPt=new Point(-1, -1);
			Point destPt=new Point(-1, -1);
			Point  factoryPos=this.vertex.getPos();
			claims.push(new MobileSensorMessage(uid, srcPt,destPt,factoryPos ));

			//claims.addElement();
		}
	}

	private void move(Random rand) {
		int itMayMove = rand.nextInt(5); // Entre 0 et 1
		if (this.vertex.getPos().equals(lastPos))
			;// //System.out.println(lastPos + "inchangée" +
				// this.vertex.getPos());
		else if ((this.vertex.getPos().getX() != lastPos.getX()) && (this.vertex.getPos().getY() != lastPos.getY()))
			;// //System.out.println(this.getId() + "oblique" + lastPos + " " +
				// this.vertex.getPos());

		lastPos = new Point(this.vertex.getPos());
		// //System.out.println("sortir de l'etat sleep"+this.getId()+"
		// "+nombre+" "+this.vertex.getPos().x+" "+this.vertex.getPos().y+"
		// ");
		if (itMayMove != 0)
			this.move(this.getId());
		else
			;// //System.out.println("move with " + itMayMove);

	}


	private void fixWitnessPoints() {
		synchronized (witnessPoints) {
			if (this.witnessPoints.size() < this.NoWitnessPoints) {
				this.witnessPoints.addElement(this.getRandPoint());
			}
		}
	}

	protected Point getRandPoint() {

		double xRand = this.xMin + (this.xMax - this.xMin) * rand.nextDouble();
		double yRand = this.yMin + (this.yMax - this.yMin) * rand.nextDouble();
		return new Point((((int) xRand) / 2) * 2 + 1, (((int) yRand) / 2) * 2 + 1);
	}

	
	private void setUpRouting() {
		this.updatePosition();
		this.updateGraphSize();
		int noOfNeighbors = this.getArity();
		this.neighborLocs = new Vector<Point>();
		for (int i = 0; i < noOfNeighbors; i++)
			this.neighborLocs.addElement(this.vertex.getNeighborByDoor(i).getPos());

	}

	private void updateGraphSize() {
		synchronized (xMax) {
			this.xMax = Math.max(this.pos.getX(), xMax);
		}
		synchronized (yMax) {
			this.yMax = Math.max(this.pos.getY(), yMax);
		}
		synchronized (xMin) {
			this.xMin = Math.min(this.pos.getX(), xMin);
		}
		synchronized (yMin) {
			this.yMin = Math.min(this.pos.getY(), yMin);
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

	private void updatePosition() {
		this.pos = this.vertex.getPos();

	}

	private void setConfiguration() {
		this.nextPulse();

		if (this.getId() == 1) {
			this.putProperty("label", new String("P"));
		} else if (this.getId() == 2) {
			this.putProperty("label", new String("N"));
		}
		nextPulse();
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}
}
