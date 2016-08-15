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
package org.swome.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.swome.core.Artefact;
import org.swome.core.Graph;
import org.swome.core.Relation;
import org.swome.core.RelationArtefactPair;

public class SimpleGraph implements Graph {

	private Map<String, Artefact> nodes = new HashMap<String, Artefact>();
	private Map<String, Set<RelationArtefactPair>> outboundRelations = new HashMap<String, Set<RelationArtefactPair>>();

	@Override
	public Collection<Artefact> artefacts() {
		return nodes.values();
	}

	@Override
	public void addArtefact(final Artefact _artefact) {
		nodes.put(_artefact.getId(), _artefact);
	}

	@Override
	public boolean hasRelations(String _id) {
		return outboundRelations.get(_id) != null;
	}

	@Override
	public Collection<RelationArtefactPair> getRelationsFor(String _id) {
		return outboundRelations.get(_id);
	}

	@Override
	public Artefact getArtefact(String _id) {
		return nodes.get(_id);
	}

	@Override
	public void addRelation(Relation _relation, Artefact _source, Artefact _dest) {
		String _sourceId = _source.getId();

		Set<RelationArtefactPair> _relations = outboundRelations.get(_sourceId);

		if (_relations == null) {
			_relations = new HashSet<RelationArtefactPair>();

			outboundRelations.put(_sourceId, _relations);
		}

		RelationArtefactPair _edgeEntry = new RelationArtefactPair(_relation,
				_dest);

		_relations.add(_edgeEntry);
	}

	@Override
	public void addDirectedRelation(Relation _relation, Artefact _source,
			Artefact _dest) {
		this.addRelation(_relation, _source, _dest);
	}
}
