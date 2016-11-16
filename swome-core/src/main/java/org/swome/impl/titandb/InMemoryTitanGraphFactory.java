package org.swome.impl.titandb;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.swome.core.GraphFactory;

import com.thinkaurelius.titan.core.TitanFactory;

public class InMemoryTitanGraphFactory implements GraphFactory {

	@Override
	public Graph createGraph() {
		return TitanFactory.build().set("storage.backend", "inmemory").open();
	}

}
