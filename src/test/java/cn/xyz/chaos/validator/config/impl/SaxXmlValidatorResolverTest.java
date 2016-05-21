package cn.xyz.chaos.validator.config.impl;

import java.util.List;

import org.junit.*;

import cn.xyz.chaos.validator.action.*;
import cn.xyz.chaos.validator.action.xml.*;
import cn.xyz.chaos.validator.validators.Validator;

public class SaxXmlValidatorResolverTest {

	SaxXmlValidatorResolver	sax	= null;

	@Before
	public void init() {
		sax = new SaxXmlValidatorResolver();
	}

	@Test
	public void test_ActionResolver() {
		List<ActionResolver> resolvers = sax.resolveActionResolver();
		Assert.assertTrue(resolvers.size() == 2);
		Assert.assertTrue(resolvers.get(0) instanceof FieldActionResolver);
		Assert.assertTrue(resolvers.get(1) instanceof ScriptActionResolver);
	}

	@Test
	public void test_ActionValidator() {
		List<ActionValidator> resolvers = sax.resolveActionValidator();
		Assert.assertTrue(resolvers.size() == 2);
		Assert.assertTrue(resolvers.get(0) instanceof FieldActionValidator);
		Assert.assertTrue(resolvers.get(1) instanceof ScriptActionValidator);
	}

	@Test
	public void test_Validator() {
		List<Validator> resolvers = sax.resolveValidator();
		Assert.assertTrue(resolvers.size() >= 18);
	}
}