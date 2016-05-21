package cn.xyz.chaos.validator.validators;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;
import cn.xyz.chaos.validator.utils.Assert;
import cn.xyz.chaos.validator.utils.StringUtils;
import cn.xyz.chaos.validator.utils.beans.BeanUtils;

/**
 * 多值比较校验器，支持Number、Date、String型比较
 * 提供比较模式{flag}有：相等（EQ）、不相等（NE）、小于（LT）、小于等于（LE）、大于（GT）、大于等于（GE）
 * 参数{refs}格式如：logPassword,logPassword1
 * 
 * @author mfan
 */
public class CompareValidator extends AbstractValidator {
	public static final String			EQ		= "EQ";
	public static final String			NE		= "NE";
	public static final String			LT		= "LT";
	public static final String			LE		= "LE";
	public static final String			GT		= "GT";
	public static final String			GE		= "GE";
	public static final List<String>	FLAGS	= Arrays.asList(new String[] { EQ, NE, LT, LE, GT, GE });
	public static final List<Class>		NUMBERS	= Arrays.asList(new Class[] { byte.class, double.class, float.class,
			int.class, long.class, short.class, Byte.class, Double.class, Float.class, Integer.class, Long.class,
			Short.class, BigInteger.class, BigDecimal.class });

	/**
	 * 1、参与比较的值如果存在null值仅能参与EQ模式或NE模式比较
	 * 2、EQ模式下先进行快速失败比较（即相邻两个值比较）
	 * 3、NE模式下除进行快速识别比较，还需要比对所有值不能相等
	 * 4、数字类型统一转为BigDecimal进行比较
	 */
	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		Valid valid = validContext.getValid();
		String flag = attr(valid, XmlValidatorResolver.XML_ATT_FLAG).toUpperCase();
		Assert.isTrue(FLAGS.contains(flag), "The flag must not be %s",
				Arrays.toString(FLAGS.toArray(new String[FLAGS.size()])));
		String xmlRef = attr(valid, XmlValidatorResolver.XML_ATT_REF);
		Assert.isTrue(StringUtils.isNotBlank(xmlRef), "The ref must not be null");
		String[] refs = StringUtils.split(xmlRef, StringUtils.COMMA);
		Assert.isTrue(refs.length >= 2, "The ref must not least 2 column");
		object = BeanUtils.getProperty(validContext.getTarget(), refs[0]);
		// 用于NE情况下各值相互比较
		List list = new ArrayList();
		for (int index = 1; index < refs.length; index++) {
			Object target = BeanUtils.getProperty(validContext.getTarget(), refs[index]);
			// 数字型强制转为BigDecimal
			if ((object != null) && NUMBERS.contains(object.getClass())) {
				object = new BigDecimal(object.toString());
			}
			if ((target != null) && NUMBERS.contains(target.getClass())) {
				target = new BigDecimal(target.toString());
			}
			// NE模式下存储所有属性值以便相互比较
			if (NE.equals(flag)) {
				if (index == 1) {
					list.add(0, object);
				}
				list.add(index, target);
			}
			// 其中包含null值的
			if ((object == null) || (target == null)) {
				// 快速比较失败，仅支持EQ与NE模式，其他应均失败
				if (!isValidNull(object, target, flag)) {
					return false;
				}
				// NE模式下再次比较各值之间是否存在相同，最后两个无需比较因在之前的isValidNull中已做比较
				if (NE.equals(flag)) {
					for (int idx = 0; idx < (list.size() - 2); idx++) {
						Object o = list.get(idx);
						if (!isValidNull(o, target, flag)) {
							return false;
						} else if (!compareTwo(o, target, flag)) {
							return false;
						}
					}
				}
				return false;
			}
			// 不含null值进行比较
			if (!compareTwo(object, target, flag)) {
				return false;
			}
			// 作为下个比较参考值
			object = target;
		}
		return true;
	}

	/**
	 * 比较含null值的情况
	 * 
	 * @param object
	 * @param target
	 * @param flag
	 * @return
	 */
	protected boolean isValidNull(Object object, Object target, String flag) {
		if ((object == null) || (target == null)) {
			if (object == target) { // 即object == target == null
				if (EQ.equals(flag)) {
					return true;
				}
			} else if (NE.equals(flag)) {// 即object != target
				return true;
			}
		}
		return false;
	}

	protected boolean compareTwo(Object object, Object target, String flag) {
		if ((object == null) || (target == null)) {
			return true;
		}
		// 如类型不一致
		if (object.getClass() != target.getClass()) {
			return true;
		}
		if ((object instanceof String) || (object instanceof BigDecimal) || (object instanceof Date)) {
			int cp = ((Comparable) object).compareTo(target);
			if (EQ.equals(flag) && (cp != 0)) {
				return false;
			} else if (NE.equals(flag) && (cp == 0)) {
				return false;
			} else if (LT.equals(flag) && (cp != -1)) {
				return false;
			} else if (LE.equals(flag) && !((cp == -1) || (cp == 0))) {
				return false;
			} else if (GT.equals(flag) && (cp != 1)) {
				return false;
			} else if (GE.equals(flag) && !((cp == 1) || (cp == 0))) {
				return false;
			}
		}
		return true;
	}
}
