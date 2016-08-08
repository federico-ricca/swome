package org.swome.impl.titandb;

import java.util.Collection;

import org.swome.core.Artefact;
import org.swome.core.Graph;
import org.swome.core.Relation;
import org.swome.core.RelationArtefactPair;

import com.thinkaurelius.titan.core.TitanGraph;

public class TitanGraphAdapter implements Graph {
	private TitanGraph graphDelegate;

	public TitanGraphAdapter(TitanGraph graphDelegate) {
		this.graphDelegate = graphDelegate;
	}

	@Override
	public Collection<Artefact> artefacts() {
		return null;
	}

	@Override
	public void addRelation(Relation _relation, Artefact _source, Artefact _dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDirectedRelation(Relation _relation, Artefact _source,
			Artefact _dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addArtefact(Artefact _artefact) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<RelationArtefactPair<Relation, Artefact>> getRelationsFor(
			String _id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRelations(String _id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Artefact getArtefact(String _id) {
		// TODO Auto-generated method stub
		return null;
	}

}
