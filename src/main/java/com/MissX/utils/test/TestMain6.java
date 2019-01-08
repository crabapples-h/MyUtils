package com.MissX.utils.test;

import java.io.IOException;

public class TestMain6 {
	public static void main(String [] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
//		String str = "{\"code\":200,\"info\":{\"token\":\"bec6f0235839797f8100614630759eb5\",\"accid\":\"a1v2c3d4\",\"name\":\"\"}}";
//		System.out.println(str);
//		Gson gson = new Gson();
//		Map<String,Object> map = gson.fromJson(str, Map.class);
//		Object code = map.get("code");
//		if(code instanceof Double) {
//			if(((Double) code).intValue()==200) {
//				Map<String,String> info =(Map<String, String>) map.get("info");
//				Set<String> keySet = info.keySet();
//				for (String string : keySet) {
//					System.out.println(string+":"+info.get(string));
//				}
//			}
//		}
////		if(null!=code&&code==200.0) {
////			System.err.println(map.get("info"));
////		}
		boolean i = true;
		boolean j = true;
		System.out.println(!i&!j);
		System.out.println(i);
		System.out.println(j);
	}
}