package cn.xyz.chaos.validator.utils.beans;

import java.util.List;
import java.util.Map;

public class TestBean1 {
	public TestBean2 getBean2() {
		return bean2;
	}

	public void setBean2(TestBean2 bean2) {
		this.bean2 = bean2;
	}

	public List<TestBean2> getList3() {
		return list3;
	}

	public void setList3(List<TestBean2> list3) {
		this.list3 = list3;
	}

	public Map<String, TestBean2> getMap4() {
		return map4;
	}

	public void setMap4(Map<String, TestBean2> map4) {
		this.map4 = map4;
	}

	private TestBean2				bean2;
	private List<TestBean2>			list3;
	private Map<String, TestBean2>	map4;
}
