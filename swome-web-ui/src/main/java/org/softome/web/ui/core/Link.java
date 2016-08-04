package org.softome.web.ui.core;

public class Link {
	private int source;
	private int target;
	private int weight;

	public Link(int source, int target, int w) {
		this.setSource(source);
		this.setTarget(target);
		this.setWeight(w);
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
