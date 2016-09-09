package generateGML;

import java.io.IOException;

public class MainCross4Topology {
	private static final int VALEUR_MIN = 0;
	private static final int VALEUR_MAX = 500;
	public static void main(String[] args) {
		int [] sizesGraph={500};//,1000,2000,5000};//,3000,4000,5000,6000,7000,8000,9000,10000};
		for (int i = 0; i < sizesGraph.length; i++) {
			for (int version = 0; version < 1; version++) {
				System.out.println(sizesGraph[i]);
				GraphGML g= new GraphGML(sizesGraph[i],"Cross4",40);
				g.save(getCurrentFolderPath()+"/GML4C/CrossThin/"+sizesGraph[i]+"/GML_Cross4_Topology"+VALEUR_MAX+"_"+VALEUR_MAX+"_"+sizesGraph[i]+"_"+version+"_.gml");
			}
		}
	}
	private static String getCurrentFolderPath() {
		String current = null;
		try {
			current = new java.io.File( "." ).getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("Current dir:"+current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir:" +currentDir);
		return current;
	}
}
