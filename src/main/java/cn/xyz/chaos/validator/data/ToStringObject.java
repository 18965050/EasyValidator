/**
 * Author lvchenggang 
 * XYZ Reserved
 * Created on 2016年5月20日 下午5:35:18
 */
package cn.xyz.chaos.validator.data;

import cn.xyz.chaos.validator.utils.JSONUtils;

/**
 * 方便对象结构查看
 * @author lvchenggang
 *
 */
public abstract class ToStringObject {

	@Override
	public String toString() {
		return JSONUtils.toJSONString(this);
	}

}
