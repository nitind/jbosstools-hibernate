package org.jboss.tools.hibernate.runtime.common;

import java.util.List;

import org.jboss.tools.hibernate.runtime.spi.ICriteria;

public abstract class AbstractCriteriaFacade 
extends AbstractFacade 
implements ICriteria {

	public AbstractCriteriaFacade(
			IFacadeFactory facadeFactory, 
			Object target) {
		super(facadeFactory, target);
	}

	@Override
	public void setMaxResults(int intValue) {
		Util.invokeMethod(
				getTarget(), 
				"setMaxResults", 
				new Class[] { int.class }, 
				new Object[] { intValue });
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> list() {
		return (List<Object>)Util.invokeMethod(
				getTarget(), 
				"list", 
				new Class[] {}, 
				new Object[] {});
	}

}