package visidia.io.gml.parser;

import java.util.Properties;

public class GMLNode {

	private Integer id;

	private String label;

	private GMLNodeGraphics graphics;

	private Properties properties;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public void setGraphics(GMLNodeGraphics graphics) {
		this.graphics = graphics;
	}

	public GMLNodeGraphics getGraphics() {
		return this.graphics;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return this.properties;
	}

}
