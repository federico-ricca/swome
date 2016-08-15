package org.swome.core;

public interface SingleArtefactProcessor<T extends Artefact> {
	public void process(T _artefact);
}
