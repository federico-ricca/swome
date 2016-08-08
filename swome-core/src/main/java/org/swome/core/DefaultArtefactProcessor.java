package org.swome.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DefaultArtefactProcessor<T extends Artefact, R extends Artefact>
		implements ArtefactProcessor<T> {
	private List<ArtefactProcessorStep<Artefact>> steps = new ArrayList<ArtefactProcessorStep<Artefact>>();

	public void addStep(ArtefactProcessorStep<Artefact> _step) {
		steps.add(_step);
	}

	@Override
	public void process(Collection<T> _artefacts, Graph _graph) {
		ArtefactFactory<T, R> _factory = this.getArtefactFactory();

		for (T _artefact : _artefacts) {
			try {
				R _resultArtefact = _factory.createArtefact(_artefact);
				this.mainStep(_resultArtefact, _graph);

				this.performAdditionalSteps(_resultArtefact, _graph);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void performAdditionalSteps(R _artefact, Graph _graph) {
		for (ArtefactProcessorStep<Artefact> _step : steps) {
			_step.perform(_artefact, _graph);
		}
	}

	public abstract void mainStep(R _artefact, Graph _graph);

	public abstract ArtefactFactory<T, R> getArtefactFactory();
}
