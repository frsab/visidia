package generateGML;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GraphGML {

	private static final int VALEUR_MIN = 0;
	private static final int VALEUR_MAX = 500;
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Edge> edges = new ArrayList<Edge>();
	private double maxValueEdges = 0;
	private double minValueEdges = Double.MAX_VALUE;

	public GraphGML(int nbNodes, String type, int averageNeighbor) {
		System.out.println("nbNodes" + nbNodes);
		if (type.equals("square")) {
			addNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);

		}
		else if (type.equals("HThin")){
			addHThinTopologyNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);
		}
		else if (type.equals("HThin100")){
			addHThin100TopologyNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);
		}
		else if (type.equals("CrossThin100")){
			addCrossThin100TopologyNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);
		}
		else if (type.equals("4D")){
			add4DTopologyNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);
		}		
		else if (type.equals("Cross4")){
			addCross4TopologyNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);
		}
		else if (type.equals("H")){
			add_H_TopologyNodes(nbNodes);
			addEdges(nbNodes, averageNeighbor);
		}
		
	}

	private void addCrossThin100TopologyNodes(int nbNodes) {
		int i = 0;
		Node nodeA1=new Node(i,100,100,"T");nodes.add(nodeA1);i++;
		Node nodeA2=new Node(i,400,400,"T");nodes.add(nodeA2);i++;

		while (i<nbNodes) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			Node node=new Node(i, x, y, new String("C"));
			if((!node.equals(nodeA1))&&(!node.equals(nodeA2))&& inCross100Topology(node.getX(),node.getY())){
				nodes.add(node);
				i++;
			}
		}
		
	}

	private boolean inCross100Topology(int x, int y) {
		if((x>=0&&x<=50)||(x>=450 && x<=500)){
			return true;
		}
		else if((x>=50 && x<=200)||(x>=300 && x<=450)){
			if((y>=0 && y<=200)||(y>=300 && y<=500))
				return true;
		}
		else if((x>=200 && x<=300)){
			if((y>=0 && y<=50)||(y>=450 && y<=500))
				return true;
					
		}
		return false;
		
	}

	private void addHThin100TopologyNodes(int nbNodes) {
		int i = 0;
		Node nodeA=new Node(i,250,150,"T");nodes.add(nodeA);i++;
	//	Node nodeA2=new Node(i,400,400,"T");nodes.add(nodeA2);i++;
		for (int j = 0; j <= 10; j++) {
			for (int jj = 0; jj <= 10; jj++) {
				Node node=new Node(i,j*50,jj*50,"F");;
				if((!node.equals(nodeA))&& inH100Topology(node.getX(),node.getY())){
					nodes.add(node);
					i++;
				}
			}
		}

		while (i<nbNodes) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			Node node=new Node(i, x, y, new String("C"));
			if((!node.equals(nodeA))&& inH100Topology(node.getX(),node.getY())){
				nodes.add(node);
				i++;
			}
		}
	}

	private boolean inH100Topology(int x, int y) {
		if((x>=0&&x<=50)||(x>=450 && x<=500)){
			return true;
		}
		else if((x>=50 && x<=150)||(x>=350 && x<=450)){
			if((y>=0 && y<=50)||(y>=450 && y<=500))
				return true;
		}
		else if((x>=150 && x<=350)){
			if((y>=0 && y<=200)||(y>=300 && y<=500))
				return true;
			//System.out.println(x+"  "+y);
		}
		return false;
	}

	private void add_H_TopologyNodes(int nbNodes) {
		int i = 0;
		Node nodeA=new Node(i,100,100,"T");
		nodes.add(nodeA);i++;
		
		for (int j = 0; j <= 10; j++) {
			for (int jj = 0; jj <= 10; jj++) {
				Node node=new Node(i,j*50,jj*50,"F");;
				if((!node.equals(nodeA))&& !inHTopology(node.getX(),node.getY())){
					nodes.add(node);
					i++;
				}
			}
		}
		while (i<nbNodes) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			Node node=new Node(i, x, y, new String("C"));
			if((!node.equals(nodeA))&& !inHTopology(node.getX(),node.getY())){
				nodes.add(node);
				i++;
			}
		}		
		
	}

	private void addCross4TopologyNodes(int nbNodes) {
		int i = 0;
		System.out.println("T");
		System.out.println(i);
		
		Node node0=new Node(i,0,0,"T");
		nodes.add(node0);i++;		
		
		Node node1=new Node(i,500,0,"T");
		nodes.add(node1);i++;		
		
		Node node2=new Node(i,0,500,"T");
		nodes.add(node2);i++;		
		
		Node node3=new Node(i,500,500,"T");
		nodes.add(node3);i++;
		
		while (i<=nbNodes) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			Node node=new Node(i, x, y, new String("C"));
			if(!inCross4Topology(node.getX(),node.getY())){
				nodes.add(node);
				i++;
			}
		}		
		
	}

	private boolean inCross4Topology(int x, int y) {
		if (x<100 || x>400){
			return false;
		}
		else if (x>=200  && x<=300){
			if(y>=100 && y<=400){
				return true;
			}
			else {
				return false;
			}
		}else{
			if(y>=200 && y<=300){
				return true;
			}
			else {
				return false;
			}
			
		}

	}

	private void add4DTopologyNodes(int nbNodes) {
		int i = 0;
		System.out.println("T");
		System.out.println(i);
		Node nodeA=new Node(i,50,50,"T");
		nodes.add(nodeA);i++;
		System.out.println("F");
		for (int j = 0; j <= 10; j++) {
			for (int jj = 0; jj <= 10; jj++) {
				Node node=new Node(i,j*50,jj*50,"F");;
				if((!node.equals(nodeA))&& in4DTopology(node.getX(),node.getY())){
					System.out.println(i);
					nodes.add(node);
					i++;
				}
			}
		}
		while (i<nbNodes) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			Node node=new Node(i, x, y, new String("C"));
			if((!node.equals(nodeA))&& in4DTopology(node.getX(),node.getY())){
				nodes.add(node);
				i++;
			}
		}		
		
	}

	private boolean in4DTopology(int x, int y) {

		Point p= new Point(x,y);

		Point p1= new Point(150,150);
		Point p2= new Point(350,150);
		Point p3= new Point(150,350);
		Point p4= new Point(350,350);
		double d1=p.distance(p1);
		double d2=p.distance(p2);
		double d3=p.distance(p3);
		double d4=p.distance(p4);
		if((d1>= 50)&(d2 >= 50)&(d3 >= 50)&(d4 >= 50)){
			return true;
		}
		return false;
	}

	public GraphGML(int nbNodes, String type, int averageNeighbor, int posCloneA) {
		System.out.println("nbNodes" + nbNodes);
		if (type.equals("square")) {
			addNodes(nbNodes,posCloneA);
			addEdges(nbNodes, averageNeighbor);

		}
		
	}

	private void addNodes(int nbNodes, int posCloneA) {
		int i = 0;
		nodes.add(new Node(i,100,100,"A"));i++;
		
		nodes.add(new Node(i,100+posCloneA,100+posCloneA,"B"));i++;
		nodes.add(new Node(i,0,0,"C"));i++;
		
		
		
		for (int j = i; j < nbNodes; j++) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			nodes.add(new Node(i, x, y, new String("C")));
			i++;
		}
		
	}

	private void addHThinTopologyNodes(int nbNodes) {
		int i = 0;
		Node nodeA=new Node(i,100,100,"T");
		nodes.add(nodeA);i++;
		
		for (int j = 0; j <= 10; j++) {
			for (int jj = 0; jj <= 10; jj++) {
				Node node=new Node(i,j*50,jj*50,"F");;
				if((!node.equals(nodeA))&& inHTopology(node.getX(),node.getY())){
					nodes.add(node);
					i++;
				}
			}
		}
		while (i<nbNodes) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			Node node=new Node(i, x, y, new String("C"));
			if((!node.equals(nodeA))&& inHTopology(node.getX(),node.getY())){
				nodes.add(node);
				i++;
			}
		}		
	}

	private boolean inHTopology(int x, int y) {
		if((x>=0&&x<=100)||(x>=400 && x<=500))
			return true;
		if((x>=100 && x<=200)||(x>=300 && x<=400)){
			if((y>=0 && y<=100)||(y>=400 && y<=500))
				return true;
		}
		if((x>=199 && x<=301)){
			if((y>=0 && y<=200)||(y>=300 && y<=500))
				return true;
			System.out.println(x+"  "+y);
		}
		return false;
	}

	private void addEdges(int nbNodes, int averageNeighbor) {
		double seille[]={90,60,55,50,34,33,27,25,23,21,21,21,11,11,11,11,11,11,11,11}; //pour graph de 500x500
		//float seille[]={34,33,27,25,23,21,21,21,11,11,11,11,11,11,11,11}; //pour graph de 1000x1000
		
		int nbEdgesMax = Math.min(nbEdgesClique(nbNodes), (int) (nbNodes * averageNeighbor) / 2);
		//System.out.println(nbNodes + " " + nbEdgesMax);
		boolean full = false;
		Edge currentEdge =null; 
		double maxEdgeLength=(new Point(0,0)).distance(new Point(VALEUR_MAX,VALEUR_MAX));
		//double maxEdgeLength=0;
		switch (nbNodes) {
		case 500:maxEdgeLength=90;
			
		break;		
		case 1000:maxEdgeLength=60;
			
		break;		
		case 2000:maxEdgeLength=40;
			
		break;		
		case 5000:maxEdgeLength=30;
			break;		

		default:
			break;
		}
		for (int i = 0; i < nbNodes -1; i++) {
			for (int j = i+1; j < nbNodes; j++) {
				currentEdge=new Edge(nodes.get(i), nodes.get(j));
				//System.out.println(currentEdge.getValue()+"i= "+i+" j= "+j+" edges.size()"+edges.size());
				if(currentEdge.getValue()<maxEdgeLength){
					maxValueEdges = Math.max(maxValueEdges,	currentEdge.getValue());
					minValueEdges = Math.min(minValueEdges,currentEdge.getValue()	);
					if (edges.size() < nbEdgesMax){
						edges.add(new Edge(nodes.get(i), nodes.get(j)));
					} else {
						Collections.sort(edges);
						maxEdgeLength=edges.get(edges.size()-1).getValue();
					//	System.out.println(maxEdgeLength);
						if (edges.get(edges.size() - 1).getValue() > currentEdge.getValue()) {
							//Collections.sort(edges);
							// System.out.println(edges.get(edges.size()-1).getValue()+">"+currentEdge.getValue());
							//System.out.println((edges.get(edges.size() - 1)).getValue());
							edges.remove(edges.size() - 1);
							edges.add(edges.size() - 1, currentEdge);
						}
					}
				}
			}
		}
		System.out.println(" full currentEdge.getValue() "+currentEdge.getValue()
		+	" maxValue Edge="+edges.get(edges.size()-1).getValue()	
		+" maxEdgeLength"+maxEdgeLength
		+"nbEdges"+edges.size());

		System.out.println("edges.size()"+edges.size());
		/*System.out.println(" "+(edges.get(edges.size() - 1)).getValue()
				+" "+(edges.get(edges.size() - 1)).getValue()
				+" "+(edges.get(edges.size() - 2)).getValue()
				+" "+(edges.get(edges.size() - 3)).getValue()
				+" ************"
				//+" "+(edges.get(1000)).getValue()
				//+" "+(edges.get(10000)).getValue()
				//+" "+(edges.get(15000)).getValue()
				
				+" ************"
				);*/

	}

	private int nbEdgesClique(int nbNodes) {
		int k = (nbNodes - 1) / 2;
		if (((nbNodes - 1) % 2) == 1) {
			return (2 * k * (k + 1));
		} else {
			return (k * (2 * k + 1));
		}

	}

	private void addNodes(int nbNodes) {
		int i = 0;
		nodes.add(new Node(i,0,0,"A"));i++;
		nodes.add(new Node(i,100,0,"A"));i++;
		nodes.add(new Node(i,250,0,"A"));i++;
		nodes.add(new Node(i,250,250,"A"));i++;
		nodes.add(new Node(i,50,50,"A"));i++;
		nodes.add(new Node(i,200,200,"A"));i++;
		nodes.add(new Node(i,250,250,"A"));i++;
        
		

		
		nodes.add(new Node(i,50,50,"B"));i++;
		nodes.add(new Node(i,50,0,"B"));i++;
		nodes.add(new Node(i,100,0,"B"));i++;
		nodes.add(new Node(i,250,250,"B"));i++;
         nodes.add(new Node(i, 350, 350,"B"));i++;
         nodes.add(new Node(i, 500, 350,"B"));i++;
         nodes.add(new Node(i, 0, 500,"B"));i++;
         nodes.add(new Node(i, 250, 500,"B"));i++;
         nodes.add(new Node(i, 500, 500,"B"));i++;


		
		for (int j = i; j < nbNodes; j++) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			nodes.add(new Node(i, x, y, new String("C")));
			i++;
		}

	}

	private int random(int mini, int max) {
		return (int) (Math.random() * (max - mini + 1)) + mini;

	}

	public void save(String fileName) {
		File f = new File(fileName);

		try {
			FileWriter fw = new FileWriter(f);
			fw.write("graph [\n       comment \"Generated by ViSiDiA\"\ndirected 0\n");
			for (int i = 0; i < nodes.size(); i++) {
				print(fw, nodes.get(i));
			}
			for (int i = 0; i < edges.size(); i++) {/* i < nodes.size()&& */
				print(fw, edges.get(i));
			}
			fw.write("]");
			fw.close();
		} catch (IOException e) {
			        
	        System.out.println("Erreur lors de Lecture/ecriture fichier : "+fileName+  "   " + e.getMessage()+" "+e.getCause());
		}

	}

	private void print(FileWriter fw, Edge edge) {
		try {
			fw.write("   edge [\n");
			fw.write("      source " + edge.getSrc().getId() + "\n");
			fw.write("      target " + edge.getTgt().getId() + "\n");
			fw.write("      label \"\"\n");
			fw.write("      weight 1.0\n");
			fw.write("   ]" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void print(FileWriter fw, Node node) {

		try {
			fw.write("   node [\n");
			fw.write("      id " + node.getId() + "\n");
			fw.write("      label \"" + node.getType() + "\"\n");
			fw.write("      graphics [\n");
			fw.write("         x " + node.getX() + "\n");
			fw.write("         y " + node.getY() + "\n");
			fw.write("      ]\n");
			fw.write("   ]\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
