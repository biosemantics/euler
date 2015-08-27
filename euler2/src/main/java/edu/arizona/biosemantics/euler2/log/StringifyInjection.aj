package edu.arizona.biosemantics.euler2.log;

import edu.arizona.biosemantics.common.log.AbstractStringifyInjection;
import edu.arizona.biosemantics.common.log.IPrintable;

public aspect StringifyInjection extends AbstractStringifyInjection {
	declare parents : edu.arizona.biosemantics.euler2.* implements IPrintable;
}
