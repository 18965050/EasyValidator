package cn.xyz.chaos.validator.validators;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;

import java.util.Arrays;
import java.util.List;

import static cn.xyz.chaos.validator.validators.CompareValidator.*;

/**
 * Created by mengfanjun on 14/12/10 010.
 */
public class CompareValidatorTest {

	@Test
	public void test_compare_str() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(validContext.getValid()).thenReturn(valid);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REF)).thenReturn("str1,str2,str3");
		CompareValidator compareValidator = new CompareValidator();
		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO("123ABC", "123ABC", "123ABC"));

		assertIt(compareValidator, validContext, Arrays.asList(EQ, LE, GE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO("100", "101", "102"));
		assertIt(compareValidator, validContext, Arrays.asList(LT, LE, NE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO("102", "101", "100"));
		assertIt(compareValidator, validContext, Arrays.asList(GT, GE, NE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO("101", "101", "100"));
		assertIt(compareValidator, validContext, Arrays.asList(GE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO("100", "100", "101"));
		assertIt(compareValidator, validContext, Arrays.asList(LE));
	}

	@Test
	public void test_compare_int() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(validContext.getValid()).thenReturn(valid);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REF)).thenReturn("int1,int2,int3");
		CompareValidator compareValidator = new CompareValidator();
		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO(100, 100, 100));
		assertIt(compareValidator, validContext, Arrays.asList(EQ, LE, GE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO(100, 101, 102));
		assertIt(compareValidator, validContext, Arrays.asList(LT, LE, NE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO(102, 101, 100));
		assertIt(compareValidator, validContext, Arrays.asList(GT, GE, NE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO(101, 101, 100));
		assertIt(compareValidator, validContext, Arrays.asList(GE));

		Mockito.when(validContext.getTarget()).thenReturn(new CompareDTO(100, 100, 101));
		assertIt(compareValidator, validContext, Arrays.asList(LE));
	}

	private void assertIt(CompareValidator compareValidator, ValidContext validContext, List<String> flags) {
		for (String flag : CompareValidator.FLAGS) {
			Valid valid = validContext.getValid();
			Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_FLAG)).thenReturn(flag);
			if (flags.contains(flag)) {
				Assert.assertTrue(compareValidator.isValid(null, validContext));
			} else {
				Assert.assertFalse(compareValidator.isValid(null, validContext));
			}
		}
	}

	class CompareDTO {
		public CompareDTO(String str1, String str2, String str3) {
			this.str1 = str1;
			this.str2 = str2;
			this.str3 = str3;
		}

		public CompareDTO(int int1, int int2, int int3) {
			this.int1 = int1;
			this.int2 = int2;
			this.int3 = int3;
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

		public int getInt3() {
			return int3;
		}

		public void setInt3(int int3) {
			this.int3 = int3;
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

		public String getStr3() {
			return str3;
		}

		public void setStr3(String str3) {
			this.str3 = str3;
		}

		private int		int1;
		private int		int2;
		private int		int3;
		private String	str1;
		private String	str2;
		private String	str3;

	}

}
