package visidia.examples.algo;

import java.awt.Point;

public class Cache  implements Comparable{
	private String UID_src;
	private String UID_msg;
	private Point posSrc;
	public String getUID_src() {
		return UID_src;
	}
	public void setUID_src(String uID_src) {
		UID_src = uID_src;
	}
	public String getUID_msg() {
		return UID_msg;
	}
	public void setUID_msg(String uID_msg) {
		UID_msg = uID_msg;
	}
	public Point getPosSrc() {
		return posSrc;
	}
	public void setPosSrc(Point posSrc) {
		this.posSrc = posSrc;
	}
	public Cache(String uID_src, String uID_msg, Point posSrc) {
		super();
		UID_src = uID_src;
		UID_msg = uID_msg;
		this.posSrc = posSrc;
	}
	@Override
	public int compareTo(Object arg0) {
		return (this.getUID_msg().compareTo(((Cache) arg0).getUID_msg()));
	}
	

}
