package visidia.examples.algo;

import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessageType;

public class MobileSensorMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2871487010304032587L;
	private Point dest;
	private Point lastNodePosition;
	private ArrayList<Integer> path= new ArrayList<Integer>();
	private Point locationClaim;
	private Integer UID;
	private String UIDsource;
	
	
	private static Random r = new Random();

	public MobileSensorMessage(String uid2, Point srcPt, Point destPt, Point factoryPos) {
		this.setUIDsource(uid2);
		this.setDest(destPt);
		this.setLastNodePosition(srcPt);
		this.locationClaim=factoryPos;
		this.UID = new Integer(r.nextInt());
	}

	public MobileSensorMessage(MobileSensorMessage msg) {

		this.dest = msg.dest;
		this.lastNodePosition = msg.lastNodePosition;
		this.path = (ArrayList<Integer>) msg.path.clone();
		this.locationClaim = msg.locationClaim;
		UID = msg.UID;
		UIDsource = msg.UIDsource;
	}

	public MobileSensorMessage() {
		// TODO Auto-generated constructor stub
	}

	public MobileSensorMessage(MobileSensorMessage msg, Point destFinal) {
		this.dest = destFinal;
		this.lastNodePosition = msg.lastNodePosition;
		this.path = (ArrayList<Integer>) msg.path.clone();
		this.locationClaim = msg.locationClaim;
		UID = msg.UID;
		UIDsource = msg.UIDsource;
	}

	public Point getDest(){
		return new Point(dest);
	}

	public void setDest(Point dest){
		this.dest = new Point(dest);
	}

	/*public MobileSensorMessage(String data, Point dest,Point locationClaim) {
		this.dest = new Point(dest);
		this.locationClaim = new Point(locationClaim);
		this.UID = new Integer(r.nextInt());
	}

	public MobileSensorMessage(String data, Point dest,Point locationClaim,MessageType type) {
		this.setType(type);
		this.dest = new Point(dest);
		this.locationClaim = new Point(locationClaim);
		this.UID = new Integer(r.nextInt());
	}

	public MobileSensorMessage(String data, Point dest,Point locationClaim,MessageType type,Integer walkCounter) {
		this.dest = new Point(dest);
		this.locationClaim = new Point(locationClaim);
		this.UID = new Integer(r.nextInt());
	}
	
	public MobileSensorMessage(MobileSensorMessage msg){
		this.setType(msg.getType());
		this.lastNodePosition=msg.lastNodePosition;
		this.path=msg.path;
		this.dest = new Point(msg.dest);
		this.locationClaim = new Point(msg.locationClaim);
		this.UID = new Integer(msg.UID);
	}

	public MobileSensorMessage(String data, Point dest,Point src,Point locClaim) {
		this.lastNodePosition=src;
		this.locationClaim = locClaim;
		this.dest = dest;//new Point(-1,-1);
		this.UID = new Integer(r.nextInt());
	}

	public MobileSensorMessage(MobileSensorMessage msg, Point destFinal) {
		this.setType(msg.getType());
		this.lastNodePosition=msg.lastNodePosition;
		//this.path=msg.path;
		//this.data = new String(msg.data);
		this.dest = destFinal;
		this.locationClaim = new Point(msg.locationClaim);
		this.UID = new Integer(msg.UID);
	}*/

	public Integer getUID(){
		return new Integer(this.UID);
	}

	public Point getClaim(){
		return new Point(locationClaim);
	}

	

	

	

	

	
	public boolean detectInfiniteLoops(){
		int pathLength=this.path.size();
		//String s="";for (int i = 0; i < pathLength; i++) {s+=path.get(i);	s+=" ";	} System.out.println("pathLength="+pathLength+"path.size()"+path.size()+"   "+s);

		////System.out.println("detectInfiniteLoops "+this.path.size());
		if(pathLength<4){
			////System.out.println("xxxx"+this.path.size());
		}
		else{
			//for (int i = pathLength-2; i >0 ; i--);
				////System.out.println("path.get("+i+")="+path.get(i));
			for (int i = pathLength-2; i >0 ; i--) {//////System.out.println(path.get(pathLength-1)+"  "+(path.get(i)));
				if(path.get(pathLength-1).equals(path.get(i))){//System.out.println(path.get(pathLength-1)+"=="+(path.get(i)));//////System.out.println("compare "+path.get(pathLength-2)+"  "+(path.get(i-1)));
					if(path.get(pathLength-2).equals(path.get(i-1))){////System.out.println("	"+path.get(pathLength-2)+"=="+(path.get(i-1)));
						return true;	
					}
					else{
						////System.out.println(path.get(pathLength-2)+"=/="+(path.get(i-1)));

					}
					
				}
				else{
					////System.out.println(path.get(pathLength-1)+"=/="+(path.get(i)));
				}
				
			}
		}

		
		return false;
	}
	@Override
	public Object clone() {
		return new MobileSensorMessage(this);//.data,this.dest,this.locationClaim,this.getType(),this.walkCounter);
	}
	
	@Override
	public String toString() {
		//if( dest.equals(new Point(-1,-1)) ) return "LocClaim";
		//else return String.valueOf(this.dest.getX())+","+String.valueOf(this.dest.getY())+","+String.valueOf(this.walkCounter);
		//else 
			return String.valueOf(this.UID);
	}

	public Point getLastNodePosition() {
		return lastNodePosition;
	}

	public void setLastNodePosition(Point lastNodePosition) {
		this.lastNodePosition = lastNodePosition;
	}

	public ArrayList<Integer> getPath() {
		return path;
	}

	public void setPath(ArrayList<Integer> path) {
		this.path = path;
	}

	public void addNumIntoPath(int id) {
		Integer idObj=new Integer(id);
		
		if(path.add(idObj)){
			////System.out.println("addNumIntoPath "+ id);
			
		}else{
			////System.out.println("addNumIntoPath "+ id+ "failure ");
		}
		String s="";
		for (int i = 0; i <0/* path.size()*/; i++)
		{
			s+=path.get(i);
			s+=" ";
		}
		//System.out.println("path.size()"+path.size()+"   "+s);

		
	}

	@Override
	public String getData() {
		return this.getUID().toString();
	}

	public String getUIDsource() {
		return UIDsource;
	}

	public void setUIDsource(String uid) {
		UIDsource = uid;
	}

}
