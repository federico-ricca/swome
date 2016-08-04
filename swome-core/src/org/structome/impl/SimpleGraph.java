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
package org.structome.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.structome.core.Artefact;
import org.structome.core.Graph;
import org.structome.core.Relation;
import org.structome.core.RelationArtefactPair;

public class SimpleGraph<N extends Artefact, E extends Relation> implements Graph<N, E> {

	private Map<String, N> nodes = new HashMap<String, N>();
	private Map<String, Set<RelationArtefactPair<E, N>>> outboundRelations = new HashMap<String, Set<RelationArtefactPair<E, N>>>();

	@Override
	public Collection<N> artefacts() {
		return nodes.values();
	}

	@Override
	public void addArtefact(final N _artefact) {
		nodes.put(_artefact.getId(), _artefact);
	}

	@Override
	public boolean hasRelations(String _id) {
		return outboundRelations.get(_id) != null;
	}

	@Override
	public Collection<RelationArtefactPair<E, N>> getRelationsFor(String _id) {
		return outboundRelations.get(_id);
	}

	@Override
	public N getArtefact(String _id) {
		return nodes.get(_id);
	}

	@Override
	public void addRelation(E _relation, N _source, N _dest) {
		String _sourceId = _source.getId();

		Set<RelationArtefactPair<E, N>> _relations = outboundRelations.get(_sourceId);

		if (_relations == null) {
			_relations = new HashSet<RelationArtefactPair<E, N>>();

			outboundRelations.put(_sourceId, _relations);
		}

		RelationArtefactPair<E, N> _edgeEntry = new RelationArtefactPair<E, N>(_relation, _dest);

		_relations.add(_edgeEntry);
	}

	@Override
	public void addDirectedRelation(E _relation, N _source, N _dest) {
		this.addRelation(_relation, _source, _dest);
	}
}
