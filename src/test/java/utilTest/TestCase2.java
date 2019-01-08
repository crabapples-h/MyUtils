package utilTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.MissX.utils.check.EmptyCheckUtil;
import com.MissX.utils.check.EmptyCheckUtil.CheckException;


public class TestCase2 {
	Map<String,Object> map = new LinkedHashMap<String,Object>();
	User user = new User();
	@Before
	public void brfore() {
		
//		List<String> list = new ArrayList<String>();
//		list.add("1");
		
//		Map<String,String> map1 = new HashMap<String,String>();
//		
		map.put("user_id", "这个是用户ID");
		map.put("status", "undefined");
//		map.put("list",list);
//		map.put("map", map1);
		
		List<String> interest = new ArrayList<String>();
		interest.add("12");
		user.setId("undefined");
		user.setName("uNdefined");
		user.setSex("sex_123456789");
		user.setInterest(interest);
	}
	@Test
	public void case1() throws CheckException {
			System.out.println(EmptyCheckUtil.EmptyCheckMapByParamsIsArray(map,true,true));
	}
	@Test
	public void case2() {
		String [] params = {"id"};
		try {
			System.out.println(EmptyCheckUtil.EmptyCheckModelParamIsArray(user, params,true,true));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	@Test
	public void case3() {
		String param = "uNdefined";
		try {
			System.out.println(EmptyCheckUtil.EmptyCheckString(param, true));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}