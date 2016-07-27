package visidia.examples.algo;

import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.StringMessage;
import visidia.simulation.process.messages.VectorMessage;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.Door;
import visidia.graph.Graph;
import visidia.graph.Vertex;
import visidia.graph.Edge;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Color;
import java.util.Random;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class Routing extends SynchronousAlgorithm {

	protected static Point posA;
	protected static Point posCloneA;
	public static final Double INF = new Double(1 << 20);
	protected static Double xMax = new Double(-INF);
	protected static Double yMax = new Double(-INF);
	protected static Double xMin = new Double(+INF);
	protected static Double yMin = new Double(+INF);
	private static Boolean compromised = new Boolean(false);
	private static Boolean graphBuilt = new Boolean(false);
	private static Vector<Edge> addedEdges = new Vector<Edge>();
	private static Boolean[][] connections = null;
	protected Random rand = new Random();
	protected static Integer iterationNumber = 25000;
	protected double xPos;
	protected double yPos;
	private Point pos;
	
	protected Vector<Point> neighborLocs;

	protected WitnessCache cache;
	protected Vector<SensorMessage> claims;
	
	protected static final Integer TOTAL_ITERATIONS_BY_CONFIGURATION = new Integer(500);

	protected static int [] idsCloneA=initTab(0,11);
	protected static int [] idsA=initTab(12,40);
	protected static int  currentIdCloneA=0;
	protected static int  currentIdA=0;
	
	
	

	protected static LevelTrace levelTrace = new LevelTrace();

	// Cordinates of the compromised region
	protected static Double x1Compr = new Double((0000.0 / 3.0) - 1.0e-6);
	protected static Double y1Compr = new Double((0000.0 / 3.0) - 1.0e-6);
	protected static Double x2Compr = new Double((1000.0 / 3.0) + 1.0e-6);
	protected static Double y2Compr = new Double((1000.0 / 3.0) + 1.0e-6);

	// Cordinates of the first clone region
	protected static Double x1Clone1 = new Double((0000.0 / 3.0) - 1.0e-6);
	protected static Double y1Clone1 = new Double((0000.0 / 3.0) - 1.0e-6);
	protected static Double x2Clone1 = new Double((1000.0 / 3.0) + 1.0e-6);
	protected static Double y2Clone1 = new Double((1000.0 / 3.0) + 1.0e-6);

	// Cordinates of the second clone region
	protected static Double x1Clone2 = new Double((2000.0 / 3.0) - 1.0e-6);
	protected static Double y1Clone2 = new Double((2000.0 / 3.0) - 1.0e-6);
	protected static Double x2Clone2 = new Double((3000.0 / 3.0) + 1.0e-6);
	protected static Double y2Clone2 = new Double((3000.0 / 3.0) + 1.0e-6);

	@Override
	public Object clone() {
		return new Routing();
	}

	private static int[] initTab(int first, int last) {
		if(last<first) return idsA;
		int[] tab= new int[(last-first)+1];		 
		for (int i =0 ; i <= (last-first); i++) {
			tab[i]=i+first;
		}
		return tab;
	}

	private static String realTimeDirectoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateGraphSize() {
		synchronized (xMax) {
			this.xMax = Math.max(this.xPos, xMax);
			this.yMax = Math.max(this.yPos, yMax);
			this.xMin = Math.min(this.xPos, xMin);
			this.yMin = Math.min(this.yPos, yMin);
		}
	}

	private void updatePosition() {
		this.pos = this.vertex.getPos();
		this.xPos = this.vertex.getPos().getX();
		this.yPos = this.vertex.getPos().getY();
	}

	protected Point getPosition() {
		return new Point(this.pos);
	}

	protected Point getRandPoint() {

		double xRand = this.xMin + (this.xMax - this.xMin) * rand.nextDouble();
		double yRand = this.yMin + (this.yMax - this.yMin) * rand.nextDouble();
		return new Point((((int) xRand) / 2) * 2 + 1, (((int) yRand) / 2) * 2 + 1);
	}

	protected double getDist(Point p1, Point p2) {
		return Math.sqrt((Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2)));
	}

	protected int getClosestDoor(Point p, Point lastNodePosition) {
		double minDist = Double.MAX_VALUE;// this.getDist(p,this.pos);
		double minAngleValue = 361;// Double.MAX_VALUE;//this.getDist(p,this.pos);

		int minDoor = -1;
		int srcDoor = -1;
		// System.out.println(this.getId() +"\n"+
		// lastNodePosition.getX() +" "+lastNodePosition.getY() +"\n"+
		// this.vertex.getPos().getX() +" "+this.vertex.getPos().getY() +"\n"+
		// p.getX() +" "+p.getY() +"\n"+
		// "\n");
		if (p.distance(this.vertex.getPos()) == 0)// stop routing if the message
													// arrived
			return -1;
		for (int i = 0; i < neighborLocs.size(); i++) {
			if (lastNodePosition.equals(neighborLocs.elementAt(i))) {
				srcDoor = i;
				// minDist = this.getDist(p,neighborLocs.elementAt(i));
			}
		}

		// trying Greedy Routing (GPSR)
		for (int i = 0; i < neighborLocs.size(); i++) {
			if (i != srcDoor) {
				if (this.getDist(p, neighborLocs.elementAt(i)) < minDist) {
					minDoor = i;
					minDist = this.getDist(p, neighborLocs.elementAt(i));
				}

			}

		}
		if (p.distance(this.vertex.getPos()) > minDist) { // success Greedy
															// Routing
			return minDoor;
		} else {// failure Greedy Routing
				// trying Perimeter Routing (GPSR)
				// -->we need a lastNode position

			if (srcDoor == -1) {
				return srcDoor;
			} else {
				for (int i = 0; i < neighborLocs.size(); i++) {
					if (i != srcDoor) {
						double currentAngle;
						try {

							currentAngle = angleBetweenTwoPointsWithFixedPoint(this.vertex.getPos(),
									neighborLocs.elementAt(srcDoor), neighborLocs.elementAt(i));
							// System.out.println(i+" : "+currentAngle);
							if (minAngleValue > currentAngle) {
								minDoor = i;
								minAngleValue = currentAngle;
							}
						} catch (NullVectorExceotion e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}

					}
				}
				return minDoor;

			}

		}

	}
	
	private void updateCurrentIdA() {
		int index=(iterationNumber%TOTAL_ITERATIONS_BY_CONFIGURATION)/TOTAL_ITERATIONS_BY_CONFIGURATION;


		currentIdA=idsA[index];
	}

	private void updateCurrentIdCloneA() {
		int index=iterationNumber/(TOTAL_ITERATIONS_BY_CONFIGURATION*idsA.length);
		currentIdCloneA=idsCloneA[index];

	}
	protected void setConfiguration(){
		//System.out.println(this.getId()+" "+iterationNumber+" "+currentIdA+" "+currentIdCloneA);
		
		if(this.getId()==1){
			updateCurrentIdCloneA();
			updateCurrentIdA();
			System.out.println(this.getId()+" "+iterationNumber+" "+currentIdA+" "+currentIdCloneA);
			
		}
		nextPulse();
		this.putProperty("label", new String("N"));
		
		if(this.getId()==currentIdCloneA){
			posCloneA = new Point(this.vertex.getPos());
			System.out.println("posCloneA"+posCloneA);
			this.putProperty("label", new String("P"));
		}
		
		if(this.getId()==currentIdA){
			posA = new Point(this.vertex.getPos());
			System.out.println("posA"+posA);
			this.putProperty("label", new String("P"));
		}
		
		nextPulse();
		
	}

	/**
	 * 
	 * @param fixed
	 * @param p1
	 * @param p2
	 * @return
	 * @throws NullVectorExceotion
	 */
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
	
	
	protected boolean isAn_A() {
		if(this.getId()==getA_id()){
			currentIdA=this.getId();
			;//System.out.println(this.getId()+"is a A"+((iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION)-1));
		}
			
		return(this.getId()==getA_id());
	}
	
	private int getA_id() {
		int configId=(iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION)-1;
		//if(this.getId()==1)
		//System.out.println(configNb+" "+idsA.length);
		int index=0;//((configNb*configNb)-(configNb*idsA.length))/idsA.length;
		index=(iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION)-(getCloneA_id()*idsA.length);//-configId;//-(configId*idsA.length);
	
			return index-1 ;//idsA[];//;
		
		
		//return (iterationNumber%(TOTAL_ITERATIONS_BY_CONFIGURATION*idsCloneA.length));
	}

	protected boolean isAn_cloneA( ) {
		if(this.getId()==getCloneA_id())
			currentIdCloneA=this.getId();
			;//System.out.println(this.getId()+"is a cloneA");
		return(this.getId()==getCloneA_id());
	}

	private int getCloneA_id( ) {
	//int configNb=iterationNumber/(TOTAL_ITERATIONS_BY_CONFIGURATION*idsA.length);
	
	
	//return (configNb/idsA.length);		
	return iterationNumber/(TOTAL_ITERATIONS_BY_CONFIGURATION*idsA.length);//dsCloneA[];//%idsCloneA.length];
		//return idsCloneA[iterationNumber/(TOTAL_ITERATIONS_BY_CONFIGURATION*idsCloneA.length)];
		//return (iterationNumber%TOTAL_ITERATIONS_BY_CONFIGURATION);
//		return 0;
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

	protected void sendRandNeighbor(SensorMessage msg) {
		if (neighborLocs.size() == 0)
			return;
		int dest = rand.nextInt(neighborLocs.size());
		msg.setDest(neighborLocs.elementAt(dest));
		this.sendTo(dest, new SensorMessage((SensorMessage) msg));
	}

	protected boolean forwardMessage(Point p, Message msg) {

		Point lastNodePosition = ((SensorMessage) msg).getLastNodePosition();
		((SensorMessage) msg).addNumIntoPath(this.getId());
		boolean detectInfiniteLoops = ((SensorMessage) msg).detectInfiniteLoops();
		if (detectInfiniteLoops) {
				return false;
			} else {
				int dest = this.getClosestDoor(p, lastNodePosition);
				if (dest == -1)
					return false;
				else {
					synchronized (levelTrace) {
						this.levelTrace.incrementNbMessage(1);
					}
				}
				this.sendTo(dest, new SensorMessage((SensorMessage) msg));
				return true;
			}
	}

	protected void checkInbox() {
		Door d = new Door();
		while (this.anyMsg()) {
			SensorMessage msg = (SensorMessage) this.receive(d);
			this.forwardMessage(msg.getDest(), msg);
		}
	}

	protected void setUpRouting() {
		this.updatePosition();
		this.updateGraphSize();
		int noOfNeighbors = this.getArity();
		this.neighborLocs = new Vector<Point>();
		for (int i = 0; i < noOfNeighbors; i++)
			this.neighborLocs.addElement(this.vertex.getNeighborByDoor(i).getPos());
	}

	@Override
	public void init() {
		this.setUpRouting();
		this.nextPulse();
		if (this.getId() == 0) {
			Point locationClaim = this.vertex.getPos();
			Point dest = new Point(600, 600);
			SensorMessage msg = new SensorMessage("saber", dest, locationClaim);
			this.forwardMessage(new Point(600, 600), msg);

		}
		nextPulse();
		while (true) {
			Point randP = this.getRandPoint();
			this.checkInbox();
			this.nextPulse();
		}

	}

	public void unCompromise() {
		synchronized (compromised) {
			if (compromised)
				compromised = false;
		}
	}

	public void compromise(int percent) {
		synchronized (compromised) {
			if (!compromised) {
				Graph g = this.getProc().getServer().getConsole().getGraph();
				for (int i = 0; i < this.getNetSize(); i++)
					g.getVertex(i).setLabel(new String("N"));
				this.clone(0);
				int N = ((this.getNetSize()) * percent) / 100;
				if ((this.iterationNumber + 1) % 500 == 0) {
					x1Compr += 1000.0 / 3.0;
					x2Compr += 1000.0 / 3.0;
					if (x1Compr > (1000.0 - 10.0)) {
						x1Compr = 0.0;
						x2Compr = 1000.0 / 3.0;
						y1Compr += 1000.0 / 3.0;
						y2Compr += 1000.0 / 3.0;
					}
					if (y1Compr > (1000.0 - 10.0)) {
						y1Compr = 0.0;
						y2Compr = 1000.0 / 3.0;
						x1Clone2 += 1000.0 / 3.0;
						x2Clone2 += 1000.0 / 3.0;
					}
					if (x1Clone2 > (1000.0 - 10.0)) {
						x1Clone2 = 0.0;
						x2Clone2 = 1000.0 / 3.0;
						y1Clone2 += 1000.0 / 3.0;
						y2Clone2 += 1000.0 / 3.0;
					}
					if (y1Clone2 > (1000.0 - 10.0)) {
						y1Clone2 = 0.0;
						y2Clone2 = 1000.0 / 3.0;
						x1Clone1 += 1000.0 / 3.0;
						x2Clone1 += 1000.0 / 3.0;
					}
					if (x1Clone1 > (1000.0 - 10.0)) {
						x1Clone1 = 0.0;
						x2Clone1 = 1000.0 / 3.0;
						y1Clone1 += 1000.0 / 3.0;
						y2Clone1 += 1000.0 / 3.0;
					}
				}
				Vector<Integer> temp = new Vector<Integer>();
				for (int i = 0; i < this.getNetSize() && N > 0; i++) {
					Vertex v = g.getVertex(i);
					Double vx = new Double(v.getPos().getX());
					Double vy = new Double(v.getPos().getY());
					if (v.getLabel().toString().equals(new String("P")))
						continue;
					if (vx > x1Compr && vy > y1Compr && vx < x2Compr && vy < y2Compr) {
						temp.addElement(i);
					}
				}
				int C = 0;
				if (temp.size() > 0) {
					C = rand.nextInt(temp.size());
				}
				;
				N = ((temp.size()) * percent) / 100;
				while (N > 0 && temp.size() != 0) {
					Vertex v = g.getVertex(temp.elementAt(C));
					temp.remove(C);
					if (!v.getLabel().toString().equals(new String("N")))
						continue;
					Double vx = new Double(v.getPos().getX());
					Double vy = new Double(v.getPos().getY());
					if (vx > x1Compr && vy > y1Compr && vx < x2Compr && vy < y2Compr) {
						v.setLabel(new String("M"));
						N -= 1;
					}
					if (temp.size() > 0) {
						C = rand.nextInt(temp.size());
					}
					;
				}
				compromised = true;
			}
		}
	}

	public void clone(double proximity) {
		Graph g = this.getProc().getServer().getConsole().getGraph();
		int C1, C2;
		C1 = rand.nextInt(this.getNetSize());
		while (!(g.getVertex(C1).getPos().getX() > x1Clone1 && g.getVertex(C1).getPos().getX() < x2Clone1
				&& g.getVertex(C1).getPos().getY() > y1Clone1 && g.getVertex(C1).getPos().getY() < y2Clone1)) {
			C1 = rand.nextInt(this.getNetSize());
		}
		C2 = rand.nextInt(this.getNetSize());
		while (!(g.getVertex(C2).getPos().getX() > x1Clone2 && g.getVertex(C2).getPos().getX() < x2Clone2
				&& g.getVertex(C2).getPos().getY() > y1Clone2 && g.getVertex(C2).getPos().getY() < y2Clone2)) {
			C2 = rand.nextInt(this.getNetSize());
			while (C1 == C2)
				C2 = rand.nextInt(this.getNetSize());
		}
		g.getVertex(C1).setLabel(new String("P"));
		g.getVertex(C2).setLabel(new String("P"));
	}

	public void randomizePos() {
		Point p = this.getRandPoint();
		this.vertex.getView().setPosition((int) p.getX(), (int) p.getY());
		this.updatePosition();
		this.updateGraphSize();
	}

	public void fillProximity(int proximity) {
		synchronized (graphBuilt) {
			if (connections == null) {
				connections = new Boolean[this.getNetSize()][this.getNetSize()];
			}
		}
		int i = this.getId();
		Graph g = this.getProc().getServer().getConsole().getGraph();
		for (int j = i + 1; j < this.getNetSize(); j++) {
			Vertex v = g.getVertex(j);
			if (this.getDist(this.vertex.getPos(), v.getPos()) < proximity) {
				connections[i][j] = true;
				synchronized (addedEdges) {
					addedEdges.addElement(this.vertex.linkTo(v, false));
				}
			} else {
				connections[i][j] = false;
			}
		}
	}

	protected void receiveClaims() {
		Door d = new Door();
		while (this.anyMsg()) {
			SensorMessage msg = (SensorMessage) this.receive(d);
			msg.setLastNodePosition((this.vertex.getNeighborByDoor(d.getNum())).getPos());
			claims.addElement(msg);
			cache.addClaim(msg.getLabel(), msg.getClaim());
		}
	}

	protected void receiveClaims(boolean store) {
		Door d = new Door();
		while (this.anyMsg()) {
			SensorMessage msg = (SensorMessage) this.receive(d);
			msg.setLastNodePosition((this.vertex.getNeighborByDoor(d.getNum())).getPos());
			claims.addElement(msg);
			if (store) {
				cache.addClaim(msg.getLabel(), msg.getClaim());
			}
		}
	}

	public WitnessCache getCache() {
		return cache;
	}

	public void setCache(WitnessCache cache) {
		this.cache = cache;
	}

	public Vector<SensorMessage> getClaims() {
		return claims;
	}

	public void setClaims(Vector<SensorMessage> claims) {
		this.claims = claims;
	}

	protected void statisticsProc(Integer iterationNumber, Boolean cloneDetected,  Point posA,Point posCloneA) {
		 //System.out.println( "statisticsProc"+iterationNumber+" "+		 cloneDetected+" "+ idA+" "+idCloneA+" "+posA+" "+posCloneA);
		synchronized (levelTrace) {
			levelTrace.update(this, this.currentIdA, this.currentIdCloneA, posA, posCloneA);
		}
		this.nextPulse();

		if (this.getId() == 1) {
			String s = levelTrace.show(iterationNumber, cloneDetected);
			//System.out.println(s);
			saveTrace(s);
			this.nextPulse();
			// levelTrace.saveToFile(true);
			levelTrace.initialize();
		}
		this.nextPulse();

	}

	private void saveTrace(String s) {
		BufferedWriter out =null;
		try {
			out = new BufferedWriter(new FileWriter(new File(this.getClass().getName()+this.getNetSize()+".txt"),true));
			out.write(s+"\n");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}			
	}


	/*
	 * CAN BE USED FOR RANDOMISING POSITIONS OF NODES IN THE GRAPH public void
	 * buildGraph(int proximity){ synchronized(graphBuilt){ if(!graphBuilt){
	 * Graph g = this.proc.getServer().getConsole().getGraph(); addedEdges = new
	 * Vector<Edge>(); for(int i=0;i<this.getNetSize();i++){ Vertex v1 =
	 * g.getVertex(i); for(int j=i+1;j<this.getNetSize();j++){ Vertex v2 =
	 * g.getVertex(j); if( this.getDist(v1.getPos(),v2.getPos()) < proximity ){
	 * / /System.out.println("Linking "+String.valueOf(v1.getId())+" "
	 * +String.valueOf (v2.getId()));
	 * addedEdges.addElement(v1.linkTo(v2,false)); } } } graphBuilt = true; } }
	 * }
	 * 
	 * public void destroyGraph(){ synchronized(graphBuilt){ if(graphBuilt){
	 * for(int i=0;i<addedEdges.size();i++){ if( addedEdges.elementAt(i) !=
	 * null) addedEdges.elementAt(i).remove();
	 * //System.out.println("Unlinking"); } addedEdges.clear(); graphBuilt =
	 * false; } } }
	 */
}
