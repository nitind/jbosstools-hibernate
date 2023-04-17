package org.jboss.tools.hibernate.orm.runtime.exp.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.hibernate.tool.orm.jbt.wrp.TypeFactoryWrapper;
import org.hibernate.tool.orm.jbt.wrp.Wrapper;
import org.hibernate.tool.orm.jbt.wrp.WrapperFactory;
import org.hibernate.type.Type;
import org.jboss.tools.hibernate.orm.runtime.exp.internal.util.GenericFacadeFactory;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.IType;
import org.jboss.tools.hibernate.runtime.spi.ITypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ITypeFactoryTest {
	
	private ITypeFactory typeFactoryFacade = null;
	
	@BeforeEach
	public void beforeEach() {
		typeFactoryFacade = (ITypeFactory)GenericFacadeFactory.createFacade(
				ITypeFactory.class, 
				WrapperFactory.createTypeFactoryWrapper());
	}
	
	@Test
	public void testConstruction() {
		assertNotNull(typeFactoryFacade);
		assertSame(TypeFactoryWrapper.INSTANCE, ((IFacade)typeFactoryFacade).getTarget());
	}
	
	@Test
	public void testGetBooleanType() {
		IType typeFacade = typeFactoryFacade.getBooleanType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("boolean", typeTarget.getName());
	}
	
	@Test
	public void testGetByteType() {
		IType typeFacade = typeFactoryFacade.getByteType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("byte", typeTarget.getName());
	}
	
	@Test
	public void testGetBigIntegerType() {
		IType typeFacade = typeFactoryFacade.getBigIntegerType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("big_integer", typeTarget.getName());
	}
	
	@Test
	public void testGetShortType() {
		IType typeFacade = typeFactoryFacade.getShortType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("short", typeTarget.getName());
	}
	
	@Test
	public void testGetCalendarType() {
		IType typeFacade = typeFactoryFacade.getCalendarType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("calendar", typeTarget.getName());
	}
	
	@Test
	public void testGetCalendarDateType() {
		IType typeFacade = typeFactoryFacade.getCalendarDateType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("calendar_date", typeTarget.getName());
	}
	
	@Test
	public void testGetIntegerType() {
		IType typeFacade = typeFactoryFacade.getIntegerType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("integer", typeTarget.getName());
	}
	
	@Test
	public void testGetBigDecimalType() {
		IType typeFacade = typeFactoryFacade.getBigDecimalType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("big_decimal", typeTarget.getName());
	}
	
	@Test
	public void testGetCharacterType() {
		IType typeFacade = typeFactoryFacade.getCharacterType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("character", typeTarget.getName());
	}
	
	@Test
	public void testGetClassType() {
		IType typeFacade = typeFactoryFacade.getClassType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("class", typeTarget.getName());
	}
	
	@Test
	public void testGetCurrencyType() {
		IType typeFacade = typeFactoryFacade.getCurrencyType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("currency", typeTarget.getName());
	}
	
	@Test
	public void testGetDateType() {
		IType typeFacade = typeFactoryFacade.getDateType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("date", typeTarget.getName());
	}
	
	@Test
	public void testGetDoubleType() {
		IType typeFacade = typeFactoryFacade.getDoubleType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("double", typeTarget.getName());
	}
	
	@Test
	public void testGetFloatType() {
		IType typeFacade = typeFactoryFacade.getFloatType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("float", typeTarget.getName());
	}
	
	@Test
	public void testGetLocaleType() {
		IType typeFacade = typeFactoryFacade.getLocaleType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("locale", typeTarget.getName());
	}
	
	@Test
	public void testGetLongType() {
		IType typeFacade = typeFactoryFacade.getLongType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("long", typeTarget.getName());
	}
	
	@Test
	public void testGetStringType() {
		IType typeFacade = typeFactoryFacade.getStringType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("string", typeTarget.getName());
	}
	
	@Test
	public void testGetTextType() {
		IType typeFacade = typeFactoryFacade.getTextType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("text", typeTarget.getName());
	}
	
	@Test
	public void testGetTimeType() {
		IType typeFacade = typeFactoryFacade.getTimeType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("time", typeTarget.getName());
	}
	
	@Test
	public void testGetTimestampType() {
		IType typeFacade = typeFactoryFacade.getTimestampType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("timestamp", typeTarget.getName());
	}
	
	@Test
	public void testGetTimezoneType() {
		IType typeFacade = typeFactoryFacade.getTimezoneType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("timezone", typeTarget.getName());
	}
	
	@Test
	public void testGetTrueFalseType() {
		IType typeFacade = typeFactoryFacade.getTrueFalseType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("true_false", typeTarget.getName());
	}
	
	@Test
	public void testGetYesNoType() {
		IType typeFacade = typeFactoryFacade.getYesNoType();
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("yes_no", typeTarget.getName());
	}
	
	@Test
	public void testGetNamedType() {
		IType typeFacade = typeFactoryFacade.getNamedType(String.class.getName());
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("string", typeTarget.getName());
	}

	@Test
	public void testGetBasicType() {
		IType typeFacade = typeFactoryFacade.getBasicType(String.class.getName());
		Type typeTarget = (Type)((Wrapper)((IFacade)typeFacade).getTarget()).getWrappedObject();
		assertEquals("string", typeTarget.getName());
	}
}
