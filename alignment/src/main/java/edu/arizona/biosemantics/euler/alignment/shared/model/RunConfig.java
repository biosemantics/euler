package edu.arizona.biosemantics.euler.alignment.shared.model;

import java.io.Serializable;

public class RunConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//euler parameters?
	
	private boolean simplifyAggregateView = false;
	
	public RunConfig() {
		
	}
	public boolean isSimplifyAggregateView() {
		return simplifyAggregateView;
	}

	public void setSimplifyAggregateView(boolean value) {
		this.simplifyAggregateView = value;
	}
	
}
