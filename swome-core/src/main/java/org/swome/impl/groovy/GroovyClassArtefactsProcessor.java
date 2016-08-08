package org.swome.impl.groovy;

import org.swome.core.ArtefactFactory;
import org.swome.core.DefaultArtefactProcessor;
import org.swome.core.FileArtefact;
import org.swome.core.Graph;

public class GroovyClassArtefactsProcessor extends
		DefaultArtefactProcessor<FileArtefact, GroovyClassArtefact> {

	@Override
	public void mainStep(GroovyClassArtefact _artefact, Graph _graph) {
		_graph.addArtefact(_artefact);
	}

	@Override
	public ArtefactFactory<FileArtefact, GroovyClassArtefact> getArtefactFactory() {
		return new GroovyClassArtefactFactory(
				new PluginBasedGroovyCodeVisitor());
	}
}
