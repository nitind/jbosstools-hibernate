package org.jboss.tools.hibernate.runtime.v_5_5.internal;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.hibernate.Filter;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.JDBCReaderFactory;
import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.JDBCReader;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ProgressListener;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableFilter;
import org.hibernate.cfg.reveng.dialect.MetaDataDialect;
import org.hibernate.engine.query.spi.HQLQueryPlan;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.hbm2x.HibernateConfigurationExporter;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.util.ReflectHelper;
import org.jboss.tools.hibernate.runtime.common.AbstractService;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.common.IFacadeFactory;
import org.jboss.tools.hibernate.runtime.common.Util;
import org.jboss.tools.hibernate.runtime.spi.HibernateException;
import org.jboss.tools.hibernate.runtime.spi.IArtifactCollector;
import org.jboss.tools.hibernate.runtime.spi.ICfg2HbmTool;
import org.jboss.tools.hibernate.runtime.spi.IColumn;
import org.jboss.tools.hibernate.runtime.spi.IConfiguration;
import org.jboss.tools.hibernate.runtime.spi.IEnvironment;
import org.jboss.tools.hibernate.runtime.spi.IExporter;
import org.jboss.tools.hibernate.runtime.spi.IHQLCodeAssist;
import org.jboss.tools.hibernate.runtime.spi.IHQLQueryPlan;
import org.jboss.tools.hibernate.runtime.spi.IHibernateMappingExporter;
import org.jboss.tools.hibernate.runtime.spi.INamingStrategy;
import org.jboss.tools.hibernate.runtime.spi.IOverrideRepository;
import org.jboss.tools.hibernate.runtime.spi.IPersistentClass;
import org.jboss.tools.hibernate.runtime.spi.IProgressListener;
import org.jboss.tools.hibernate.runtime.spi.IProperty;
import org.jboss.tools.hibernate.runtime.spi.IReverseEngineeringSettings;
import org.jboss.tools.hibernate.runtime.spi.IReverseEngineeringStrategy;
import org.jboss.tools.hibernate.runtime.spi.ISchemaExport;
import org.jboss.tools.hibernate.runtime.spi.ISessionFactory;
import org.jboss.tools.hibernate.runtime.spi.ITable;
import org.jboss.tools.hibernate.runtime.spi.ITableFilter;
import org.jboss.tools.hibernate.runtime.spi.ITypeFactory;
import org.jboss.tools.hibernate.runtime.spi.IValue;
import org.jboss.tools.hibernate.runtime.v_5_5.internal.util.ConfigurationMetadataDescriptor;
import org.jboss.tools.hibernate.runtime.v_5_5.internal.util.DummyMetadataDescriptor;
import org.jboss.tools.hibernate.runtime.v_5_5.internal.util.JdbcMetadataConfiguration;
import org.jboss.tools.hibernate.runtime.v_5_5.internal.util.JpaConfiguration;
import org.xml.sax.EntityResolver;

public class ServiceImpl extends AbstractService {

	private static final String HIBERNATE_VERSION = "5.5";
	
	private IFacadeFactory facadeFactory = new FacadeFactoryImpl();

	@Override
	public IConfiguration newAnnotationConfiguration() {
		return newDefaultConfiguration();
	}

	@Override
	public IConfiguration newJpaConfiguration(
			String entityResolver, 
			String persistenceUnit,
			Map<Object, Object> overrides) {
		return facadeFactory.createConfiguration(
				new JpaConfiguration(persistenceUnit, overrides));
	}

	@Override
	public IConfiguration newDefaultConfiguration() {
		getUsageTracker().trackNewConfigurationEvent(HIBERNATE_VERSION);
		return facadeFactory.createConfiguration(new Configuration());
	}

	@Override
	public IHibernateMappingExporter newHibernateMappingExporter(
			IConfiguration hcfg, File file) {
		return facadeFactory.createHibernateMappingExporter(
				new HibernateMappingExporterExtension(
						facadeFactory,
						hcfg,
						file));
	}

	@Override
	public ISchemaExport newSchemaExport(IConfiguration hcfg) {
		SchemaExportFacadeImpl result = 
			(SchemaExportFacadeImpl)facadeFactory.createSchemaExport(new SchemaExport());
		result.setConfiguration(hcfg);
		return result;
	}

	@Override
	public IHQLCodeAssist newHQLCodeAssist(IConfiguration hcfg) {
		IHQLCodeAssist result = null;
		if (hcfg instanceof ConfigurationFacadeImpl) {
			result = facadeFactory.createHQLCodeAssist(
					new HQLCodeAssist(((ConfigurationFacadeImpl)hcfg).getMetadata()));
		}
		return result;
	}

	@Override
	public IConfiguration newJDBCMetaDataConfiguration() {
		return facadeFactory.createConfiguration(new JdbcMetadataConfiguration());
	}

	@Override
	public IExporter createExporter(String exporterClassName) {
		Exporter exporter = (Exporter)Util.getInstance(
				exporterClassName, 
				facadeFactory.getClassLoader());
		if (HibernateConfigurationExporter.class.isAssignableFrom(exporter.getClass())) {
			exporter.setMetadataDescriptor(new DummyMetadataDescriptor());
		} else {
			exporter.setMetadataDescriptor(
					new ConfigurationMetadataDescriptor(
							(ConfigurationFacadeImpl)newDefaultConfiguration()));
		}
		return facadeFactory.createExporter(exporter);
	}

	@Override
	public IArtifactCollector newArtifactCollector() {
		return facadeFactory.createArtifactCollector(new ArtifactCollector());
	}

	@Override
	public IHQLQueryPlan newHQLQueryPlan(String query, boolean shallow, ISessionFactory sessionFactory) {
		SessionFactoryImpl factory = 
				(SessionFactoryImpl) ((IFacade)sessionFactory).getTarget();
		Map<String, Filter> enabledFilters = Collections.emptyMap(); 
		HQLQueryPlan queryPlan = new HQLQueryPlan(query, shallow, enabledFilters, factory);
		return facadeFactory.createHQLQueryPlan(queryPlan);
	}

	@Override
	public ITypeFactory newTypeFactory() {
		// target for ITypeFactory is a dummy Object
		return facadeFactory.createTypeFactory();
	}

	@Override
	public INamingStrategy newNamingStrategy(String strategyClassName) {
		try {
			return facadeFactory.createNamingStrategy(
					Class.forName(strategyClassName).newInstance());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public IOverrideRepository newOverrideRepository() {
		return facadeFactory.createOverrideRepository(new OverrideRepository());
	}

	@Override
	public ITableFilter newTableFilter() {
		return facadeFactory.createTableFilter(new TableFilter());
	}

	@Override
	public IReverseEngineeringStrategy newDefaultReverseEngineeringStrategy() {
		return facadeFactory.createReverseEngineeringStrategy(
				new DefaultReverseEngineeringStrategy());
	}

	@Override
	public IReverseEngineeringSettings newReverseEngineeringSettings(
			IReverseEngineeringStrategy res) {
		return facadeFactory.createReverseEngineeringSettings(
				new ReverseEngineeringSettings(
						(ReverseEngineeringStrategy)((IFacade)res).getTarget()));
	}

	@Override
	public Map<String, List<ITable>> collectDatabaseTables(
			Properties properties, 
			IReverseEngineeringStrategy strategy,
			final IProgressListener progressListener) {
		Map<String, List<ITable>> result = new HashMap<String, List<ITable>>();
		JDBCReader jdbcReader = 
				JDBCReaderFactory.newJDBCReader(
						properties, 
						(ReverseEngineeringStrategy)((IFacade)strategy).getTarget(),
						buildServiceRegistry(properties));
		MetaDataDialect metadataDialect = jdbcReader.getMetaDataDialect();
		DefaultDatabaseCollector databaseCollector = new DefaultDatabaseCollector(metadataDialect);
		ProgressListener progressListenerWrapper = new ProgressListener() {			
			@Override
			public void startSubTask(String name) {
				progressListener.startSubTask(name);
			}
		};
		jdbcReader.readDatabaseSchema(
				databaseCollector, 
				properties.getProperty(Environment.DEFAULT_CATALOG), 
				properties.getProperty(Environment.DEFAULT_SCHEMA),
				progressListenerWrapper);
		Iterator<?> iterator = databaseCollector.getQualifierEntries();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>)iterator.next();
			ArrayList<ITable> list = new ArrayList<ITable>();
			for (Object table : (Iterable<?>)entry.getValue()) {
				list.add(facadeFactory.createTable(table));
			}
			result.put((String)entry.getKey(), list);
		}
		return result;
	}
	
	@Override
	public IReverseEngineeringStrategy newReverseEngineeringStrategy(
			String strategyName,
			IReverseEngineeringStrategy delegate) {
		assert delegate instanceof IFacade;
		ReverseEngineeringStrategy delegateTarget = 
				(ReverseEngineeringStrategy)((IFacade)delegate).getTarget();
		Object target = 
				newReverseEngineeringStrategy(strategyName, delegateTarget);
		return facadeFactory.createReverseEngineeringStrategy(target);
	}

	@Override
	public String getReverseEngineeringStrategyClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICfg2HbmTool newCfg2HbmTool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProperty newProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITable newTable(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IColumn newColumn(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String newDialect(Properties properties, Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getDriverManagerConnectionProviderClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEnvironment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newSimpleValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newPrimitiveArray(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newArray(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newBag(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newList(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newMap(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newSet(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newManyToOne(ITable table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newOneToMany(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue newOneToOne(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPersistentClass newSingleTableSubclass(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPersistentClass newJoinedSubclass(IPersistentClass persistentClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPersistentClass newSpecialRootClass(IProperty ormElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPersistentClass newRootClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInitialized(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getJPAMappingFilePaths(String persistenceUnitName, EntityResolver entityResolver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getClassWithoutInitializingProxy(Object reflectedObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	private ServiceRegistry buildServiceRegistry(Properties properties) {
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
		builder.applySettings(properties);
		return builder.build();
	}

	private Object newReverseEngineeringStrategy(final String className, Object delegate) {
        try {
            Class<?> clazz = ReflectHelper.classForName(className);
            Class<?> revEngClass =  ReflectHelper.classForName(
            		"org.hibernate.cfg.reveng.ReverseEngineeringStrategy");
			Constructor<?> constructor = 
					clazz.getConstructor(
							new Class[] { revEngClass });
            return constructor.newInstance(new Object[] { delegate });
        }
        catch (NoSuchMethodException e) {
			try {
				ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
				Class<?> clazz = null;
				if ( contextClassLoader != null ) {
					clazz = contextClassLoader.loadClass(className);
				} else {
					clazz = Class.forName( className );
				}
				if (clazz != null) {
					return clazz.newInstance();
				} else {
					throw new HibernateException("Class " + className + " could not be found.");
				}
			}
			catch (Exception eq) {
				throw new HibernateException(eq);
			}
		}
        catch (Exception e) {
			throw new HibernateException(e);
		}
    }

}
