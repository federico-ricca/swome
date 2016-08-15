package org.swome.core;

import java.util.List;

public interface ArtefactProcessorStep<T> {

	public List<T> perform(T _artefact, Graph _graph);
}
