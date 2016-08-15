package org.swome.impl.jung2;

import org.swome.core.Graph;
import org.swome.core.GraphFactory;

public class Jung2GraphFactory implements GraphFactory {

	@Override
	public Graph createGraph() {
		return new Jung2GraphAdapter();
	}

}
