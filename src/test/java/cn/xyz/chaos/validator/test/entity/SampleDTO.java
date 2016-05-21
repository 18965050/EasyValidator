package cn.xyz.chaos.validator.test.entity;

import java.util.Date;

public class SampleDTO {

	private int	int1;
	private int	int2;
	private int	int3;

	public SampleDTO(int int1, int int2, int int3) {
		this.int1 = int1;
		this.int2 = int2;
		this.int3 = int3;
	}

	public SampleDTO(String str1, String str2, String str3) {
		this.str1 = str1;
		this.str2 = str2;
		this.str3 = str3;
	}

	private String	str1;
	private String	str2;
	private String	str3;

	public SampleDTO(Date date1, Date date2, Date date3) {
		this.date1 = date1;
		this.date2 = date2;
		this.date3 = date3;
	}

	private Date	date1;
	private Date	date2;
	private Date	date3;
}
