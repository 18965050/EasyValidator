package cn.xyz.chaos.validator.validators;

import static cn.xyz.chaos.validator.validators.CompareValidator.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;

/**
 * Created by mengfanjun on 14/12/10 010.
 */
public class EqualValidatorTest {

	@Test
	public void test_equal_str() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(validContext.getValid()).thenReturn(valid);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REF)).thenReturn("str1,str2");
		EqualValidator equalValidator = new EqualValidator();
		Mockito.when(validContext.getTarget()).thenReturn(new EqualDTO("123ABC", "123ABC"));
		Assert.assertTrue(equalValidator.isValid(null, validContext));

		Mockito.when(validContext.getTarget()).thenReturn(new EqualDTO("abc", "Abc"));
		Assert.assertFalse(equalValidator.isValid(null, validContext));
	}

	@Test
	public void test_compare_int() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(validContext.getValid()).thenReturn(valid);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REF)).thenReturn("int1,int2");
		EqualValidator equalValidator = new EqualValidator();
		Mockito.when(validContext.getTarget()).thenReturn(new EqualDTO(100, 100));
		Assert.assertTrue(equalValidator.isValid(null, validContext));

		Mockito.when(validContext.getTarget()).thenReturn(new EqualDTO(100, 101));
		Assert.assertFalse(equalValidator.isValid(null, validContext));
	}

	class EqualDTO {
		public EqualDTO(String str1, String str2) {
			this.str1 = str1;
			this.str2 = str2;
		}

		public EqualDTO(int int1, int int2) {
			this.int1 = int1;
			this.int2 = int2;
		}

		public int getInt1() {
			return int1;
		}

		public void setInt1(int int1) {
			this.int1 = int1;
		}

		public int getInt2() {
			return int2;
		}

		public void setInt2(int int2) {
			this.int2 = int2;
		}

		public String getStr1() {
			return str1;
		}

		public void setStr1(String str1) {
			this.str1 = str1;
		}

		public String getStr2() {
			return str2;
		}

		public void setStr2(String str2) {
			this.str2 = str2;
		}

		private int		int1;
		private int		int2;
		private String	str1;
		private String	str2;

	}

}
