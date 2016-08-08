package org.swome.core;

public interface ArtefactProcessorStep<T> {

	public T perform(T _artefact, Graph _graph);
}
