package org.swome.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DefaultArtefactProcessor<T extends Artefact, R extends Artefact>
		implements ArtefactProcessor<T> {
	private List<ArtefactProcessorStep<Artefact>> artefactSteps = new ArrayList<ArtefactProcessorStep<Artefact>>();

	public void addStep(ArtefactProcessorStep<Artefact> _step) {
		artefactSteps.add(_step);
	}

	@Override
	public void process(Collection<T> _artefacts, Graph _graph) {
		ArtefactFactory<T, R> _factory = this.getArtefactFactory();

		for (T _artefact : _artefacts) {
			try {
				_factory.createArtefact(
						_artefact,
						(_resultArtefact) -> {
							List<Artefact> results = this.mainStep(
									_resultArtefact, _graph);

							_graph.addArtefacts(results);

							this.performAdditionalArtefactSteps(
									_resultArtefact, _graph);
						});

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		this.traverseGraph(_graph);
	}

	public void performAdditionalArtefactSteps(R _artefact, Graph _graph) {
		for (ArtefactProcessorStep<Artefact> _step : artefactSteps) {
			List<Artefact> results = _step.perform(_artefact, _graph);

			_graph.addArtefacts(results);
		}
	}

	public abstract List<Artefact> mainStep(R _artefact, Graph _graph);

	public abstract ArtefactFactory<T, R> getArtefactFactory();

	public abstract void traverseGraph(Graph _graph);
}
