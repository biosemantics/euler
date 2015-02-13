package edu.arizona.biosemantics.euler.log;

import edu.arizona.biosemantics.common.log.AbstractStringifyInjection;
import edu.arizona.biosemantics.common.log.IPrintable;

public aspect StringifyInjection extends AbstractStringifyInjection {
	declare parents : edu.arizona.biosemantics.euler.* implements IPrintable;
}
