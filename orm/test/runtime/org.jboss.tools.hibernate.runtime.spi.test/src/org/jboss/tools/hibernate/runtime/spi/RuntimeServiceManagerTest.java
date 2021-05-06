package org.jboss.tools.hibernate.runtime.spi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.prefs.Preferences;

public class RuntimeServiceManagerTest {
	
	private static String testPreferencesName = "org.jboss.tools.hibernate.runtime.spi.test.services";
	
	private RuntimeServiceManager runtimeServiceManager = null;
	
	@Before
	public void before() throws Exception {
		Constructor<RuntimeServiceManager> constructor = 
				RuntimeServiceManager.class.getDeclaredConstructor(new Class[] {});
		constructor.setAccessible(true);
		runtimeServiceManager = constructor.newInstance(new Object[] {});
	}
	
	@Test
	public void testConstruction() throws Exception {
		Field servicePreferencesField = RuntimeServiceManager.class.getDeclaredField("servicePreferences");
		servicePreferencesField.setAccessible(true);
		Preferences preferences = (Preferences)servicePreferencesField.get(runtimeServiceManager);
		Assert.assertEquals("org.jboss.tools.hibernate.runtime.spi.services", preferences.name());
		Field servicesMapField = RuntimeServiceManager.class.getDeclaredField("servicesMap");
		servicesMapField.setAccessible(true);
		Assert.assertNotNull(servicesMapField.get(runtimeServiceManager));
		Field allVersionsField = RuntimeServiceManager.class.getDeclaredField("allVersions");
		allVersionsField.setAccessible(true);
		Assert.assertNotNull(allVersionsField.get(runtimeServiceManager));
		Field enabledVersionsField = RuntimeServiceManager.class.getDeclaredField("enabledVersions");
		enabledVersionsField.setAccessible(true);
		Assert.assertNotNull(enabledVersionsField.get(runtimeServiceManager));
	}

	@Test
	public void testGetInstance() throws Exception {
		Field instanceField = RuntimeServiceManager.class.getDeclaredField("INSTANCE");
		instanceField.setAccessible(true);
		Object instance = instanceField.get(null);
		Assert.assertNotNull(instance);
		Assert.assertSame(instance, RuntimeServiceManager.getInstance());
	}
	
	@Test
	public void testGetDefaultService() throws Exception {
		Field servicesMapField = RuntimeServiceManager.class.getDeclaredField("servicesMap");
		servicesMapField.setAccessible(true);
		Map<String, IService> servicesMap = new HashMap<String, IService>();
		IService bazService = createService();
		servicesMap.put("baz", bazService);
		Field allVersionsField = RuntimeServiceManager.class.getDeclaredField("allVersions");
		allVersionsField.setAccessible(true);
		allVersionsField.set(runtimeServiceManager, new String[] { "foo", "bar", "baz" });
	}
	
	@Test
	public void testGetAllVersions() throws Exception {
		Field allVersionsField = RuntimeServiceManager.class.getDeclaredField("allVersions");
		allVersionsField.setAccessible(true);
		String[] allVersions = new String[] { "foo", "bar" };
		allVersionsField.set(runtimeServiceManager, allVersions);
		Assert.assertArrayEquals(allVersions, runtimeServiceManager.getAllVersions());
		Assert.assertNotSame(allVersions, runtimeServiceManager.getAllVersions());
	}
	
	@Test
	public void testGetDefaultVersion() throws Exception {
		Field allVersionsField = RuntimeServiceManager.class.getDeclaredField("allVersions");
		allVersionsField.setAccessible(true);
		allVersionsField.set(runtimeServiceManager, new String[] { "foo", "bar", "baz" });
		Assert.assertEquals("baz", runtimeServiceManager.getDefaultVersion());
	}
	
	@Test
	public void testFindService() throws Exception {
		Field servicesMapField = RuntimeServiceManager.class.getDeclaredField("servicesMap");
		servicesMapField.setAccessible(true);
		Map<String, IService> servicesMap = new HashMap<String, IService>();
		IService fooService = createService();
		IService barService = createService();
		servicesMap.put("foo", fooService);
		servicesMap.put("bar", barService);
		servicesMapField.set(runtimeServiceManager, servicesMap);
		Assert.assertSame(fooService, runtimeServiceManager.findService("foo"));
		Assert.assertSame(barService, runtimeServiceManager.findService("bar"));
	}
	
	@Test
	public void testIsServiceEnabled() throws Exception {
		Assert.assertFalse(runtimeServiceManager.isServiceEnabled("foobar"));
		Field enabledVersionsField = RuntimeServiceManager.class.getDeclaredField("enabledVersions");
		enabledVersionsField.setAccessible(true);
		Set<String> enabledVersions = new HashSet<String>();
		enabledVersions.add("foobar");
		enabledVersionsField.set(runtimeServiceManager, enabledVersions);
		Assert.assertTrue(runtimeServiceManager.isServiceEnabled("foobar"));
	}
	
	@Test
	public void testEnableService() throws Exception {
		Field enabledVersionsField = RuntimeServiceManager.class.getDeclaredField("enabledVersions");
		enabledVersionsField.setAccessible(true);	
		Set<String> enabledVersions = new HashSet<String>();
		enabledVersionsField.set(runtimeServiceManager, enabledVersions);
		Preferences preferences = InstanceScope.INSTANCE.getNode(testPreferencesName);
		Field preferencesField = RuntimeServiceManager.class.getDeclaredField("servicePreferences");
		preferencesField.setAccessible(true);
		preferencesField.set(runtimeServiceManager, preferences);
		Assert.assertFalse(preferences.getBoolean("foobar", false));
		Assert.assertFalse(enabledVersions.contains("foobar"));
		runtimeServiceManager.enableService("foobar", true);
		Assert.assertTrue(preferences.getBoolean("foobar", false));
		Assert.assertTrue(enabledVersions.contains("foobar"));
		runtimeServiceManager.enableService("foobar", false);
		Assert.assertFalse(preferences.getBoolean("foobar", false));
		Assert.assertFalse(enabledVersions.contains("foobar"));
	}
	
	private IService createService() {
		return (IService)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] { IService.class }, 
				new InvocationHandler() {				
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
				});
	}
	
}
