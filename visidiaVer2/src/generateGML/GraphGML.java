package generateGML;
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
		else if (type.equals("H")){
			addHTopologyNodes(nbNodes);
			//addEdges(nbNodes, averageNeighbor);
		}
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

	private void addHTopologyNodes(int nbNodes) {
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
		
		/*for (int j = i; j < nbNodes; j++) {
			int x = random(0, VALEUR_MAX);
			int y = random(0, VALEUR_MAX);
			if( inHTopology(x,y)){
				nodes.add(new Node(i, x, y, new String("C")));
				i++;
				System.out.println(i);
			}
			
		}*/
		
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
		//float seille[]={62,44,34,33,27,25,23,21,21,21,11,11,11,11,11,11,11,11}; //pour graph de 500x500
		float seille[]={34,33,27,25,23,21,21,21,11,11,11,11,11,11,11,11}; //pour graph de 1000x1000
		
		int nbEdgesMax = Math.min(nbEdgesClique(nbNodes), (int) (nbNodes * averageNeighbor) / 2);
		System.out.println(nbNodes + " " + nbEdgesMax);
		boolean full = false;
		Edge currentEdge =null; 
		for (int i = 0; i < nbNodes - 1; i++) {
			for (int j = i; j < nbNodes; j++) {
				currentEdge=new Edge(nodes.get(i), nodes.get(j));
				if(true){
					full = edges.size() >= nbEdgesMax;
					maxValueEdges = Math.max(maxValueEdges,	currentEdge.getValue());
					minValueEdges = Math.min(minValueEdges,currentEdge.getValue()	);
					if (!full) {

						edges.add(new Edge(nodes.get(i), nodes.get(j)));

					} else {
						if (edges.get(edges.size() - 1).getValue() > currentEdge.getValue()) {
							Collections.sort(edges);
							// System.out.println(edges.get(edges.size()-1).getValue()+">"+currentEdge.getValue());
							//System.out.println((edges.get(edges.size() - 1)).getValue());
							edges.remove(edges.size() - 1);
							edges.add(edges.size() - 1, currentEdge);
			
						}
					}
				}

			}

		}
		System.out.println(" "+(edges.get(edges.size() - 1)).getValue()
				+" "+(edges.get(edges.size() - 1)).getValue()
				+" "+(edges.get(edges.size() - 2)).getValue()
				+" "+(edges.get(edges.size() - 3)).getValue()
				+" ************"
				//+" "+(edges.get(1000)).getValue()
				//+" "+(edges.get(10000)).getValue()
				//+" "+(edges.get(15000)).getValue()
				
				+" ************"
				);

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
		
		nodes.add(new Node(i,50,50,"B"));i++;
		nodes.add(new Node(i,50,0,"B"));i++;
		nodes.add(new Node(i,100,0,"B"));i++;
		nodes.add(new Node(i,250,250,"B"));i++;
		
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
		} catch (IOException exception) {
			System.out.println("Erreur lors de Lecture/ecriture fichier : " + exception.getMessage());
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
