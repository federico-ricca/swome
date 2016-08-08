package org.swome.impl.titandb;

import org.swome.core.Graph;
import org.swome.core.GraphFactory;

import com.thinkaurelius.titan.core.TitanFactory;

public class InMemoryTitanGraphFactory implements GraphFactory {

	@Override
	public Graph createGraph() {
		return new TitanGraphAdapter(TitanFactory.build()
				.set("storage.backend", "inmemory").open());
	}

}
