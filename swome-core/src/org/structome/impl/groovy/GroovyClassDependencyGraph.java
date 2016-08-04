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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.structome.core.Graph;
import org.structome.core.RelationArtefactPair;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class GroovyClassDependencyGraph implements Graph<GroovyClassArtefact, ClassReferenceRelation> {
	private DirectedSparseMultigraph<GroovyClassArtefact, ClassReferenceRelation> graph;
	private HashMap<String, GroovyClassArtefact> artefactMap;

	public GroovyClassDependencyGraph() {
		graph = new DirectedSparseMultigraph<GroovyClassArtefact, ClassReferenceRelation>();
		artefactMap = new HashMap<String, GroovyClassArtefact>();
	}

	@Override
	public Collection<GroovyClassArtefact> artefacts() {
		return graph.getVertices();
	}

	@Override
	public void addDirectedRelation(ClassReferenceRelation _relation, GroovyClassArtefact _source,
			GroovyClassArtefact _dest) {
		graph.addEdge(_relation, _source, _dest, EdgeType.DIRECTED);
	}

	@Override
	public void addRelation(ClassReferenceRelation _relation, GroovyClassArtefact _source,
			GroovyClassArtefact _dest) {
		graph.addEdge(_relation, _source, _dest, EdgeType.UNDIRECTED);
	}

	@Override
	public void addArtefact(GroovyClassArtefact _artefact) {
		graph.addVertex(_artefact);
		artefactMap.put(_artefact.getId(), _artefact);
	}

	@Override
	public Collection<RelationArtefactPair<ClassReferenceRelation, GroovyClassArtefact>> getRelationsFor(
			String _id) {
		Collection<RelationArtefactPair<ClassReferenceRelation, GroovyClassArtefact>> _relations = new ArrayList<RelationArtefactPair<ClassReferenceRelation, GroovyClassArtefact>>();

		GroovyClassArtefact _targetArtefact = artefactMap.get(_id);

		Collection<ClassReferenceRelation> _edges = graph.getIncidentEdges(_targetArtefact);

		for (ClassReferenceRelation _edge : _edges) {
			Pair<GroovyClassArtefact> _pair = graph.getEndpoints(_edge);

			if (_targetArtefact.equals(_pair.getFirst())) {
				_relations.add(new RelationArtefactPair<ClassReferenceRelation, GroovyClassArtefact>(_edge,
						_pair.getSecond()));
			} /*else if (_targetArtefact.equals(_pair.getSecond())) {
				_relations.add(new RelationArtefactPair<ClassReferenceRelation, GroovyClassArtefact>(_edge,
						_pair.getFirst()));
			}*/
		}

		return _relations;
	}

	@Override
	public boolean hasRelations(String _id) {
		return !graph.getIncidentEdges(artefactMap.get(_id)).isEmpty();
	}

	@Override
	public GroovyClassArtefact getArtefact(String _id) {
		return artefactMap.get(_id);
	}
}
