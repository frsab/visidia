package visidia.examples.algo;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.awt.Point;


public class WitnessCacheMobile{
	// Label - Point
	private HashMap<String,Cache> cache;
	private boolean detection = false;
	

	public WitnessCacheMobile(){
		cache = new HashMap<String,Cache>();
		
	}

	public void clearCache(){
		cache.clear();
		detection = false;
	}

	public boolean cloneDetected(){
		return detection;
	}


	public int size() {
		return cache.size();
	}
	public void addClaim(MobileSensorMessage msg) {
		String label=msg.getUIDsource();
		if( !cache.containsKey(label) ){
			//cache.put(label, msg.getClaim());		g uID_src, String uID_msg, Point posSrc
			cache.put(label, new Cache(msg.getUIDsource(),msg.getUID().toString(),msg.getClaim()));	
		}
		else{
			Point cacheElementPos = cache.get(label). getPosSrc();
			if(cacheElementPos.equals(msg.getClaim())){
				detection=true;
			}
			
		}
		/*Iterator it = cache.entrySet().iterator();		    
		while (it.hasNext()) {		        
			Map.Entry pair = (Map.Entry)it.next();
		    if(msg.getClaim().equals(pair.getValue())){
		    	it.remove(); // avoids a ConcurrentModificationException
		    	detection = true;
				clonedLabels.addElement(label);
				//System.out.println("detected");
				RedMobile.cloneDetected=true;
				return true;
		    }
		}*/
		
	}
}
