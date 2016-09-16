package generateGML;
import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class MainGreedyGraphsEdges {

	private static final int VALEUR_MIN = 0;
	private static final int VALEUR_MAX = 500;
	// static int nbNodes=1000;

	public static void main(String[] args) {

		int [] averageEdgeMaxLegtn={60};//3000,4000,5000,6000,7000,8000,9000,10000};

		int [] sizesGraph={1000};//3000,4000,5000,6000,7000,8000,9000,10000};
		for (int i = 0; i < sizesGraph.length; i++) {
			for (int j = 0; j < averageEdgeMaxLegtn[i]*2; j++) {
				GraphGML g= new GraphGML(sizesGraph[i],"square",40,j);
				g.save(getCurrentFolderPath()+"/GML4C/CrossThin/"+sizesGraph[i]+"GMLGreedy_"+VALEUR_MAX+"_"+VALEUR_MAX+"_"+sizesGraph[i]+"_"+j+"_.gml");
				System.out.println("GMLe_"+VALEUR_MAX+"_"+VALEUR_MAX+"_"+sizesGraph[i]+"_"+j+"_.gml");
				
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
