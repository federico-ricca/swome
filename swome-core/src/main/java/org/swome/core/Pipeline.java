package org.swome.core;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.swome.extraction.ResourceProcessor;

public class Pipeline {
	private Graph graph;
	private GraphFactory graphFactory;

	public Pipeline(GraphFactory graphFactory) {
		this.graphFactory = graphFactory;
		this.initGraph();
	}

	public void run(ResourceProcessor processor) {
		for (Resource r : processor) {
			System.out.println(r.getName());
			System.out.println(r.getProperties());
			
		}
	}

	private void initGraph() {
		if (graph == null) {
			graph = graphFactory.createGraph();
		}
	}

	public Graph getGraph() {
		return graph;
	}

}
