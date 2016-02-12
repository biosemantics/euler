package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree;

import java.io.Serializable;

import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;

public class Node implements Serializable {
	
	public String id;
	public String name;
	public DiagnosticValue diagnosticValue = DiagnosticValue.MEDIUM;
	
	public Node() { }
	
	public Node(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

