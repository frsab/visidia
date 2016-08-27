package generateGML;

public class MainCross4Topology {
	private static final int VALEUR_MIN = 0;
	private static final int VALEUR_MAX = 500;
	public static void main(String[] args) {
		int [] sizesGraph={10,50,100,500,1000,2000};//,3000,4000,5000,6000,7000,8000,9000,10000};
		for (int i = 0; i < sizesGraph.length; i++) {
			for (int version = 0; version < 1; version++) {
				System.out.println(sizesGraph[i]);
				GraphGML g= new GraphGML(sizesGraph[i],"Cross4",40);
				g.save("GML_Cross4_Topology"+VALEUR_MAX+"_"+VALEUR_MAX+"_"+sizesGraph[i]+"_"+version+"_.gml");
			}
		}
	}
}
