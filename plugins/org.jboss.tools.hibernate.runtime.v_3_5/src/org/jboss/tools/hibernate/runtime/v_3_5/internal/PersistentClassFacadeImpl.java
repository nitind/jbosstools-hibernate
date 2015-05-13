package org.jboss.tools.hibernate.runtime.v_3_5.internal;

import org.hibernate.mapping.PersistentClass;
import org.jboss.tools.hibernate.runtime.common.AbstractPersistentClassFacade;
import org.jboss.tools.hibernate.runtime.spi.IFacadeFactory;

public class PersistentClassFacadeImpl extends AbstractPersistentClassFacade {
	
	public PersistentClassFacadeImpl(
			IFacadeFactory facadeFactory,
			PersistentClass persistentClass) {
		super(facadeFactory, persistentClass);
	}

	public PersistentClass getTarget() {
		return (PersistentClass)super.getTarget();
	}

	@Override
	public boolean isDiscriminatorValueNull() {
		return getTarget().isDiscriminatorValueNull();
	}

	@Override
	public boolean isExplicitPolymorphism() {
		return getTarget().isExplicitPolymorphism();
	}

	@Override
	public boolean isForceDiscriminator() {
		return getTarget().isForceDiscriminator();
	}

	@Override
	public boolean isInherited() {
		return getTarget().isInherited();
	}

	@Override
	public boolean isJoinedSubclass() {
		return getTarget().isJoinedSubclass();
	}

	@Override
	public boolean isLazy() {
		return getTarget().isLazy();
	}

	@Override
	public boolean isLazyPropertiesCacheable() {
		return getTarget().isLazyPropertiesCacheable();
	}

	@Override
	public boolean isMutable() {
		return getTarget().isMutable();
	}

	@Override
	public boolean isPolymorphic() {
		return getTarget().isPolymorphic();
	}

	@Override
	public boolean isVersioned() {
		return getTarget().isVersioned();
	}

	@Override
	public int getBatchSize() {
		return getTarget().getBatchSize();
	}

	@Override
	public String getCacheConcurrencyStrategy() {
		return getTarget().getCacheConcurrencyStrategy();
	}

	@Override
	public String getCustomSQLDelete() {
		return getTarget().getCustomSQLDelete();
	}

	@Override
	public String getCustomSQLInsert() {
		return getTarget().getCustomSQLInsert();
	}

	@Override
	public String getCustomSQLUpdate() {
		return getTarget().getCustomSQLUpdate();
	}

	@Override
	public String getDiscriminatorValue() {
		return getTarget().getDiscriminatorValue();
	}

	@Override
	public String getLoaderName() {
		return getTarget().getLoaderName();
	}

	@Override
	public int getOptimisticLockMode() {
		return getTarget().getOptimisticLockMode();
	}

	@Override
	public String getTemporaryIdTableDDL() {
		return getTarget().getTemporaryIdTableDDL();
	}

	@Override
	public String getTemporaryIdTableName() {
		return getTarget().getTemporaryIdTableName();
	}

	@Override
	public String getWhere() {
		return getTarget().getWhere();
	}

}
