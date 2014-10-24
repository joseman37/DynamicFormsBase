package mx.jmgs.dynamicformsbase.util;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import mx.jmgs.dynamicformsbase.dyna.xml.FieldSelectItem;
import mx.jmgs.dynamicformsbase.dyna.xml.Select;

import org.junit.Test;

public class JsfUtilTest {

	@Test
	public void getBeanFieldTest() {
		System.out.println("getBeanFieldTest**************");
		Select select = new Select();
		FieldSelectItem item = new FieldSelectItem();
		item.setLabel("a");
		select.getSelectItems().add(item);
		
		System.out.println(Arrays.toString(Select.class.getDeclaredFields()));
		
		
		try {
			List<FieldSelectItem>  items = JsfUtil.callBeanGetter(select, "getSelectItems");
			System.out.println(items.get(0).getLabel());
		} catch (NoSuchMethodException | InvocationTargetException
				| IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}
		
		select = new Select();
		
		try {
			List<FieldSelectItem>  items = JsfUtil.callBeanGetter(select, "getSelectItems");
			System.out.println("Size: " + items.size());
		} catch (NoSuchMethodException | InvocationTargetException
				| IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test
	public void getBeanFieldTest2() {
		System.out.println("getBeanFieldTest2**************");
		Select select = new Select();
		
		try {
			String label = JsfUtil.callBeanGetter(select, "getLabel");
			System.out.println(label);
		} catch (NoSuchMethodException | InvocationTargetException
				| IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	

}
