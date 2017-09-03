package org.swome.core;

import java.util.List;

import org.apache.tinkerpop.gremlin.structure.Graph;

public interface ArtefactProcessorStep<T> {

	public List<T> perform(T _artefact, Graph _graph);
}
