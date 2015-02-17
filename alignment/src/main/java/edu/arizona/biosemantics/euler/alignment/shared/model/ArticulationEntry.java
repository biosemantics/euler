package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public class ArticulationEntry implements Serializable {
		private Taxon taxonB;
		private Taxon taxonA;
		public ArticulationEntry() { }
		
		public ArticulationEntry(Taxon taxonB, Taxon taxonA) {
			super();
			this.taxonB = taxonB;
			this.taxonA = taxonA;
		}
		public ArticulationEntry(Articulation articulation) {
			this.taxonA = articulation.getTaxonA();
			this.taxonB = articulation.getTaxonB();
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((taxonA == null) ? 0 : taxonA.hashCode());
			result = prime * result
					+ ((taxonB == null) ? 0 : taxonB.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ArticulationEntry other = (ArticulationEntry) obj;
			if (taxonA == null) {
				if (other.taxonA != null)
					return false;
			} else if (!taxonA.equals(other.taxonA))
				return false;
			if (taxonB == null) {
				if (other.taxonB != null)
					return false;
			} else if (!taxonB.equals(other.taxonB))
				return false;
			return true;
		}
			
	}