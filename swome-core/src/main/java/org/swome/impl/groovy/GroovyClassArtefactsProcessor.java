package org.swome.impl.groovy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.swome.core.Artefact;
import org.swome.core.ArtefactFactory;
import org.swome.core.ArtefactRegistry;
import org.swome.core.DefaultArtefactProcessor;
import org.swome.core.FileArtefact;
import org.swome.core.Graph;
import org.swome.core.Relation;

public class GroovyClassArtefactsProcessor extends
		DefaultArtefactProcessor<FileArtefact, GroovyClassArtefact> {
	private Set<String> relations = new HashSet<String>();

	@Override
	public List<Artefact> mainStep(GroovyClassArtefact _artefact, Graph _graph) {
		_graph.addArtefact(_artefact);

		return Collections.emptyList();
	}

	@Override
	public ArtefactFactory<FileArtefact, GroovyClassArtefact> getArtefactFactory() {
		return new GroovyClassArtefactFactory(new PluginBasedGroovyCodeVisitor(
				new ArtefactRegistry<GroovyClassArtefact>()));
	}

	@Override
	public void traverseGraph(Graph _graph) {
		for (Artefact _artefact : _graph.artefacts()) {
			if (_artefact instanceof GroovyClassArtefact) {
				GroovyClassArtefact _groovyClassArtefact = (GroovyClassArtefact) _artefact;

				String _superClassId = _groovyClassArtefact.getSuperClassId();

				if (_superClassId != null) {
					GroovyClassArtefact _superClassArtefact = (GroovyClassArtefact) _graph
							.getArtefact(_superClassId);

					if (_superClassArtefact != null) {
						_graph.addDirectedRelation(new Relation("superclass"),
								_artefact, _superClassArtefact);
					}
				}

				for (MethodDefinition _methodDef : _groovyClassArtefact
						.getMethods()) {
					Artefact _methodArtefact = _graph
							.getArtefact(_groovyClassArtefact.getId() + ":"
									+ _methodDef.getSignature());

					Collection<MethodCallReference> _methodCalls = _methodDef
							.getMethodCallContext().getMethodCallReferences();

					for (MethodCallReference _ref : _methodCalls) {
						GroovyClassArtefact _dest = (GroovyClassArtefact) _graph
								.getArtefact(_ref.getTargetClassReference()
										.getFQCN());

						if (_dest != null) {
							Artefact _destMethodArtefact = _graph
									.getArtefact(_dest.getId() + ":"
											+ _ref.getTargetMethod());

							String _relationType = "method-call";
							
							if (_ref.getTargetMethod().isConstructor()) {
								_destMethodArtefact = _dest;
								_relationType = "constructor-call";
							}
							

							String _key = "method-call" + "-"
									+ _artefact.getId() + "-" + _dest.getId();

							if (!relations.contains(_key)) {
								relations.add(_key);

								_graph.addDirectedRelation(new Relation(
										_relationType), _methodArtefact,
										_destMethodArtefact);
							}
						}
					}
				}
			}

		}
	}

}
