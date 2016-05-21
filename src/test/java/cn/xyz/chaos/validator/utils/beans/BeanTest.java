package cn.xyz.chaos.validator.utils.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeanTest {
	public static void main(String[] args) {
		TestBean1 b1 = new TestBean1();
		TestBean2 bean2 = new TestBean2();
		bean2.setName("b2");
		b1.setBean2(bean2);

		ArrayList<TestBean2> l2 = new ArrayList<TestBean2>();
		for (int i = 0; i < 10; i++) {
			TestBean2 b2 = new TestBean2();
			b2.setName("List:TestBean2:" + i);
			l2.add(b2);
		}
		b1.setList3(l2);
		Map<String, TestBean2> map4 = new HashMap<String, TestBean2>();
		for (int i = 10; i < 20; i++) {
			TestBean2 b2 = new TestBean2();
			b2.setName("Map:TestBean2:" + i);
			map4.put(b2.getName(), b2);
		}
		b1.setMap4(map4);

		BeanUtils utils = new BeanUtils();
		Object obj;
		try {
			obj = utils.getNestedProperty(b1, "list3[1].name");
			System.out.println(obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

	}
}
