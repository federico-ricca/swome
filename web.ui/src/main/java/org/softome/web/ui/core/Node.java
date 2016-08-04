package org.softome.web.ui.core;

public class Node {
	private String name;
	private String group;

	public Node(String name, String group) {
		this.setName(name);
		this.setGroup(group);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
