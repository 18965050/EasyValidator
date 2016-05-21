package cn.xyz.chaos.validator.test;

import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import cn.xyz.chaos.validator.EasyFieldError;
import cn.xyz.chaos.validator.EasyValidatorUtilities;
import cn.xyz.chaos.validator.test.entity.SampleDTO;

public class SampleDTOTest {

	@Test
	public void test_int_min_max() {
		EasyValidatorUtilities utils = new EasyValidatorUtilities();
		SampleDTO dto = new SampleDTO(2, 2, 1);
		// ///////////////////////////////////////
		List<EasyFieldError> errors = utils.validator(dto, new String[] { "test_int_min_max" });
		Assert.isTrue(!errors.isEmpty(), "min-max");
	}

	private static void show(String msg, List<EasyFieldError> errors) {
		System.out.println(msg);
		for (EasyFieldError error : errors) {
			System.out.println("\t" + error.getField() + " - " + String.format(error.getMsg(), error.getArgs()));
		}
		System.out.println("-----------------------------------------------");
	}
}
