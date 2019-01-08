package utilTest;

import java.util.List;

import com.MissX.utils.annotations.MissX;

public class User {
	@MissX
	private String id;
	@MissX
	private String name;
	@MissX
	private String sex;
	@MissX
	private List<String> interest;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public List<String> getInterest() {
		return interest;
	}
	public void setInterest(List<String> interest) {
		this.interest = interest;
	}
}
