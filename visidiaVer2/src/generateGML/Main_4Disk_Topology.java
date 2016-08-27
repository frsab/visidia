package generateGML;
import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Main_4Disk_Topology {

	private static final int VALEUR_MIN = 0;
	private static final int VALEUR_MAX = 500;
	// static int nbNodes=1000;

	public static void main(String[] args) {
		int [] sizesGraph={600};//,3000,4000,5000,6000,7000,8000,9000,10000};
		for (int i = 0; i < sizesGraph.length; i++) {
			for (int version = 0; version < 1; version++) {
				System.out.println(sizesGraph[i]);
				GraphGML g= new GraphGML(sizesGraph[i],"4D",40);
				g.save("GGML_4D_Topology"+VALEUR_MAX+"_"+VALEUR_MAX+"_"+sizesGraph[i]+"_"+version+"_.gml");
			}
		}
	}





	

}
