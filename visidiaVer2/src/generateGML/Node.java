package generateGML;
import java.awt.Point;


public class Node {
	private int id;
	private Point position;
	private String type;
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Node(int id,int x, int y, String type) {
		//System.out.println(id+" "+x+" "+y+" "+type);
		this.id=id;
		position=new Point(x,y);
		this.type=type;

	}

	public String getqId() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return (int) position.getX();

	}	
	public int getY() {
		return (int) position.getY();

	}
    @Override
    public boolean equals(Object obj) {
    	return this.getPosition().equals(((Node) obj).getPosition());
      
       
    }


}
