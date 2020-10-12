package org.jboss.tools.hibernate.runtime.v_6_0.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.api.export.ArtifactCollector;
import org.hibernate.tool.api.export.Exporter;
import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.internal.export.cfg.CfgExporter;
import org.hibernate.tool.internal.export.common.DefaultArtifactCollector;
import org.hibernate.tool.internal.export.common.GenericExporter;
import org.jboss.tools.hibernate.runtime.common.AbstractArtifactCollectorFacade;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.IArtifactCollector;
import org.jboss.tools.hibernate.runtime.spi.IGenericExporter;
import org.jboss.tools.hibernate.runtime.v_6_0.internal.util.ConfigurationMetadataDescriptor;
import org.junit.Before;
import org.junit.Test;

public class ExporterFacadeTest {
	
	private static final FacadeFactoryImpl FACADE_FACTORY = new FacadeFactoryImpl();
	
	private Exporter exporterTarget = null;
	private ExporterFacadeImpl exporterFacade = null;
	
	@Before
	public void before() {
		exporterTarget = new TestExporter();
		exporterFacade = new ExporterFacadeImpl(FACADE_FACTORY, exporterTarget);
	}
	
	@Test
	public void testCreation() {
		assertNotNull(exporterFacade);
	}
	
	@Test
	public void testSetConfiguration() throws Exception {
		exporterTarget = new CfgExporter();
		exporterFacade = new ExporterFacadeImpl(FACADE_FACTORY, exporterTarget);
		Properties properties = new Properties();
		Configuration configurationTarget = new Configuration();
		configurationTarget.setProperties(properties);
		ConfigurationFacadeImpl configurationFacade = new ConfigurationFacadeImpl(FACADE_FACTORY, configurationTarget);
		exporterFacade.setConfiguration(configurationFacade);	
		assertSame(properties, ((CfgExporter)exporterTarget).getCustomProperties());
		Object object = exporterTarget.getProperties().get(ExporterConstants.METADATA_DESCRIPTOR);
		assertNotNull(object);
		assertTrue(object instanceof ConfigurationMetadataDescriptor);
		ConfigurationMetadataDescriptor configurationMetadataDescriptor = (ConfigurationMetadataDescriptor)object;
		Field field = ConfigurationMetadataDescriptor.class.getDeclaredField("configurationFacade");
		field.setAccessible(true);
		object = field.get(configurationMetadataDescriptor);
		assertNotNull(object);
		assertTrue(object instanceof ConfigurationFacadeImpl);
		assertSame(object, configurationFacade);
	}
	
	@Test
	public void testSetArtifactCollector() {
		ArtifactCollector artifactCollectorTarget = new DefaultArtifactCollector();
		IArtifactCollector artifactCollectorFacade = 
				new AbstractArtifactCollectorFacade(FACADE_FACTORY, artifactCollectorTarget) {};
		exporterFacade.setArtifactCollector(artifactCollectorFacade);
		assertSame(
				exporterTarget.getProperties().get(ExporterConstants.ARTIFACT_COLLECTOR), 
				artifactCollectorTarget);
	}
	
	@Test
	public void testSetOutputDirectory() {
		File file = new File("");
		exporterFacade.setOutputDirectory(file);
		assertSame(
				exporterTarget.getProperties().get(ExporterConstants.DESTINATION_FOLDER),
				file);		
	}
	
	@Test
	public void testSetTemplatePath() {
		String[] templatePath = new String[] {};
		exporterFacade.setTemplatePath(templatePath);
		assertSame(
				exporterTarget.getProperties().get(ExporterConstants.TEMPLATE_PATH),
				templatePath);
	}
	
	@Test
	public void testStart() throws Exception {
		assertFalse(((TestExporter)exporterTarget).started);
		exporterFacade.start();
		assertTrue(((TestExporter)exporterTarget).started);
	}
	
	@Test
	public void testGetProperties() {
		Properties properties = new Properties();
		assertNotNull(exporterFacade.getProperties());
		assertNotSame(properties, exporterFacade.getProperties());
		((TestExporter)exporterTarget).properties = properties;
		assertSame(properties, exporterFacade.getProperties());
	}
	
	@Test
	public void testGetGenericExporter() {
		IGenericExporter genericExporter = exporterFacade.getGenericExporter();
		assertNull(genericExporter);
		exporterTarget = new GenericExporter();
		exporterFacade = new ExporterFacadeImpl(FACADE_FACTORY, exporterTarget);
		genericExporter = exporterFacade.getGenericExporter();
		assertNotNull(genericExporter);
		assertSame(exporterTarget, ((IFacade)genericExporter).getTarget());
	}
	
	private static class TestExporter implements Exporter {		

		Properties properties = new Properties();
		boolean started = false;

		@Override
		public Properties getProperties() {
			return properties;
		}

		@Override
		public void start() {
			started = true;
		}
		
	}

}
