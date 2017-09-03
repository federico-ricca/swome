package org.swome.impl.jung2;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.swome.core.GraphFactory;

public class Jung2GraphFactory implements GraphFactory {

	@Override
	public Graph createGraph() {
		return new Jung2GraphAdapter();
	}

}
