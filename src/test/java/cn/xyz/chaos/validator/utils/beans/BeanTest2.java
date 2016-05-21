package cn.xyz.chaos.validator.utils.beans;

import cn.xyz.chaos.validator.utils.beans.resolver.DefaultResolver;

/**
 * Created by mengfanjun on 14/11/11 011.
 */
public class BeanTest2 {
	public static void main(String[] args) {
		DefaultResolver resolver = new DefaultResolver();
		String string = "name[1]";
		string = "name(1)";
		System.out.println(resolver.getProperty(string));
		System.out.println(resolver.getIndex(string));
		System.out.println(resolver.getKey(string));
	}
}
