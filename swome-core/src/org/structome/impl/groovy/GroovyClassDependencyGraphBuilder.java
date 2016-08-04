/*************************************************************************** 
   Copyright 2015 Federico Ricca

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 ***************************************************************************/
package org.structome.impl.groovy;

import java.util.Collection;

import org.structome.core.ArtefactFactory;
import org.structome.core.Graph;
import org.structome.core.GraphBuilder;

public class GroovyClassDependencyGraphBuilder implements
		GraphBuilder<GroovyClassArtefact, ClassReferenceRelation, GroovyFileArtefact> {

	@Override
	public Graph<GroovyClassArtefact, ClassReferenceRelation> buildFrom(GroovyFileArtefact _source,
			ArtefactFactory<GroovyClassArtefact, GroovyFileArtefact> _factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph<GroovyClassArtefact, ClassReferenceRelation> buildFrom(
			Collection<GroovyFileArtefact> _sources,
			ArtefactFactory<GroovyClassArtefact, GroovyFileArtefact> _factory) {
		GroovyClassDependencyGraph _graph = new GroovyClassDependencyGraph();

		for (GroovyFileArtefact _source : _sources) {
			try {
				GroovyClassArtefact _classArtefact = _factory.createArtefact(_source);
				_graph.addArtefact(_classArtefact);
			} catch (Exception _exception) {

			}
		}

		for (GroovyClassArtefact _artefact : _graph.artefacts()) {
			String _superClassId = _artefact.getSuperClassId();

			if (_superClassId != null) {
				GroovyClassArtefact _superClassArtefact = _graph.getArtefact(_superClassId);

				if (_superClassArtefact != null) {
					_graph.addDirectedRelation(new SuperclassClassReferenceRelation(), _artefact, _superClassArtefact);
				}
			}
			
			Collection<MethodCallReference> _methodCalls = _artefact.getMethodCallReferences();
			
			for (MethodCallReference _ref : _methodCalls) {
				GroovyClassArtefact _dest = _graph.getArtefact(_ref.getTargetClassId());
				
				if (_dest != null) {
					_graph.addDirectedRelation(new MethodCallRelation(_ref.getTargetMethod()), _artefact, _dest);
				}
			}
		}

		return _graph;
	}

}
