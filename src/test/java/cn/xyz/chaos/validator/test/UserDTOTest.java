package cn.xyz.chaos.validator.test;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.xyz.chaos.validator.EasyFieldError;
import cn.xyz.chaos.validator.EasyValidatorUtilities;
import cn.xyz.chaos.validator.test.entity.UserDTO;

public class UserDTOTest {

	EasyValidatorUtilities	utils	= new EasyValidatorUtilities();

	@Test
	public void test_login() {
		UserDTO login = new UserDTO("mfan", "mima", null, null, 0, null, null, null);
		List<EasyFieldError> errors = utils.validator(login, new String[] { "login" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 0);

		login = new UserDTO("mfan", null, null, null, 0, null, null, null);
		errors = utils.validator(login, new String[] { "login" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 1);

		login = new UserDTO("mfan", "123456789", null, null, 0, null, null, null);
		errors = utils.validator(login, new String[] { "login" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 1);
	}

	@Test
	public void test_regist() {
		UserDTO reg = new UserDTO("lname", "pwd123", "pwd123", "姓名", 0, new Date(), "18912345678", "02512345678");
		List<EasyFieldError> errors = utils.validator(reg, new String[] { "regist", "regist1", "regist2" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 0);

		reg.setLogUsername("帐号");
		reg.setLogPassword2("pwd124");
		reg.setMobile(null);
		errors = utils.validator(reg, new String[] { "regist", "regist1", "regist2" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == (2 * 3));

		reg.setPhone(null);
		errors = utils.validator(reg, new String[] { "regist", "regist1", "regist2" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == (3 * 3));
	}

	@Test
	public void test_registScript() {
		UserDTO reg = new UserDTO("lname", "pwd123", "pwd123", "姓名", 0, new Date(), "18912345678", "02512345678");
		List<EasyFieldError> errors = utils.validator(reg, new String[] { "registScript" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 0);

		reg.setMobile(null);
		errors = utils.validator(reg, new String[] { "registScript" });
		Assert.assertTrue(errors.size() == 0);

		reg.setPhone(null);
		errors = utils.validator(reg, new String[] { "registScript" });
		Assert.assertTrue(errors.size() == 1);

		reg.setGender(1);
		errors = utils.validator(reg, new String[] { "registScript" });
		Assert.assertTrue(errors.size() == 0);
	}

	@Test
	public void test_registSimple() {
		UserDTO reg = new UserDTO("lname", "pwd123", "pwd123", null, 0, null, null, null);
		List<EasyFieldError> errors = utils.validator(reg, new String[] { "registSimple" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 0);

		reg.setLogUsername("我是帐号");
		errors = utils.validator(reg, new String[] { "registSimple" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 0);

		reg.setLogUsername("我是帐号据说要很长");
		errors = utils.validator(reg, new String[] { "registSimple" });
		debugPrint(errors);
		Assert.assertTrue(errors.size() == 1);
	}

	private void debugPrint(List<EasyFieldError> errors) {
		System.out.println();
		System.out.println("----------------------------");
		for (EasyFieldError error : errors) {
			System.out.println(error.getField() + " - " + error.getMsg());
		}
		System.out.println("----------------------------");
	}
}
