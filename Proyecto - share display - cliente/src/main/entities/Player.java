package main.entities;

import java.awt.Dimension;
import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private Dimension dimension;

	private String name;
	private boolean ready;
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", dimension=" + dimension + "]";
	}
}
