package cn.xyz.chaos.validator.validators;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;

public class AtMostValidatorTest {

	@Test
	public void test_atmost() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REF)).thenReturn("str1,str2,str3");
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_MAX)).thenReturn("2");
		Mockito.when(validContext.getValid()).thenReturn(valid);
		AtMostValidator atMostValidator = new AtMostValidator();

		Mockito.when(validContext.getTarget()).thenReturn(new AtMost("s1", "s2", "s3"));
		Assert.assertFalse(atMostValidator.isValid(null, validContext));

		Mockito.when(validContext.getTarget()).thenReturn(new AtMost("s1", "s2", null));
		Assert.assertTrue(atMostValidator.isValid(null, validContext));

		Mockito.when(validContext.getTarget()).thenReturn(new AtMost("s1", null, null));
		Assert.assertTrue(atMostValidator.isValid(null, validContext));

	}

	class AtMost {
		public String getStr1() {
			return str1;
		}

		public String getStr2() {
			return str2;
		}

		public String getStr3() {
			return str3;
		}

		public AtMost(String str1, String str2, String str3) {
			super();
			this.str1 = str1;
			this.str2 = str2;
			this.str3 = str3;
		}

		private String	str1;
		private String	str2;
		private String	str3;
	}
}
