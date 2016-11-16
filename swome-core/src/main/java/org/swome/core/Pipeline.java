package org.swome.core;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.swome.extraction.ResourceProcessor;

public class Pipeline {
	private Graph graph;
	private GraphFactory graphFactory;

	public Pipeline(GraphFactory graphFactory) {
		this.graphFactory = graphFactory;
	}

	public void run(ResourceProcessor processor) {
		this.initGraph();

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
