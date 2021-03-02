package org.jboss.tools.hibernate.runtime.v_6_0.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.Value;
import org.jboss.tools.hibernate.runtime.common.AbstractPropertyFacade;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.common.IFacadeFactory;
import org.jboss.tools.hibernate.runtime.spi.IPersistentClass;
import org.jboss.tools.hibernate.runtime.spi.IProperty;
import org.jboss.tools.hibernate.runtime.spi.IValue;
import org.junit.Before;
import org.junit.Test;

public class PropertyFacadeTest {

	private static final IFacadeFactory FACADE_FACTORY = new FacadeFactoryImpl();
	
	private Property propertyTarget = null;
	private IProperty propertyFacade = null;

	@Before
	public void before() {
		propertyTarget = new Property();
		propertyFacade = new AbstractPropertyFacade(FACADE_FACTORY, propertyTarget) {};
	}
	
	@Test
	public void testGetValue() throws Exception {
		Field field = AbstractPropertyFacade.class.getDeclaredField("value");
		field.setAccessible(true);
		assertNull(field.get(propertyFacade));
		assertNull(propertyFacade.getValue());
		Value valueTarget = createValue();
		propertyTarget.setValue(valueTarget);
		IValue valueFacade = propertyFacade.getValue();
		assertNotNull(valueFacade);
		assertSame(valueFacade, field.get(propertyFacade));
		assertSame(valueTarget, ((IFacade)valueFacade).getTarget());
	}
	
	@Test
	public void testSetName() {
		assertNotEquals("foo", propertyTarget.getName());
		propertyFacade.setName("foo");
		assertEquals("foo", propertyTarget.getName());
	}
	
	@Test
	public void testSetPersistentClass() throws Exception {
		Field field = AbstractPropertyFacade.class.getDeclaredField("persistentClass");
		field.setAccessible(true);
		assertNull(field.get(propertyFacade));
		assertNull(propertyTarget.getPersistentClass());
		PersistentClass persistentClassTarget = new RootClass(null);
		IPersistentClass persistentClassFacade = 
				FACADE_FACTORY.createPersistentClass(persistentClassTarget);
		propertyFacade.setPersistentClass(persistentClassFacade);
		assertSame(persistentClassFacade, field.get(propertyFacade));
		assertSame(persistentClassTarget, propertyTarget.getPersistentClass());
	}
	
	@Test
	public void testGetPersistentClass() throws Exception {
		Field field = AbstractPropertyFacade.class.getDeclaredField("persistentClass");
		field.setAccessible(true);
		assertNull(field.get(propertyFacade));
		assertNull(propertyFacade.getPersistentClass());
		PersistentClass persistentClassTarget = new RootClass(null);
		propertyTarget.setPersistentClass(persistentClassTarget);
		IPersistentClass persistentClassFacade = propertyFacade.getPersistentClass();
		assertNotNull(persistentClassFacade);
		assertSame(persistentClassFacade, field.get(propertyFacade));
		assertSame(persistentClassTarget, ((IFacade)persistentClassFacade).getTarget());
	}
	
	@Test
	public void testIsComposite() {
		propertyTarget.setValue(createValue());
		assertFalse(propertyFacade.isComposite());
		MetadataBuildingContext metadataBuildingContext = createMetadataBuildingContext();
		Component component = new Component(metadataBuildingContext, new Table(), new RootClass(metadataBuildingContext));
		propertyTarget.setValue(component);
		assertTrue(propertyFacade.isComposite());
	}

	private Value createValue() {
		return (Value)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] { Value.class }, 
				new InvocationHandler() {	
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
		});
	}
	
	private MetadataBuildingContext createMetadataBuildingContext() {
		return (MetadataBuildingContext)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] { MetadataBuildingContext.class }, 
				new InvocationHandler() {	
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
		});
	}
}
