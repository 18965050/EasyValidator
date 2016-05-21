package cn.xyz.chaos.validator.utils.beans;

public class TestBean2 {
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TestBean3 getBean3() {
		return bean3;
	}

	public void setBean3(TestBean3 bean3) {
		this.bean3 = bean3;
	}

	private String		name;
	private TestBean3	bean3;
}
