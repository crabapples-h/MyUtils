package entryTest;

import java.util.Map;

public class EntryData {
	private Data data;

	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "[data=" + data + "]";
	}






	public class Data{
		private String flag;
//		private Map<String,String> map;
		private Object result;

		public String getFlag() {
			return flag;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}
//		public Map<String, String> getMap() {
//			return map;
//		}
//		public void setMap(Map<String, String> map) {
//			this.map = map;
//		}
		public Object getResult() {
			return result;
		}
		public void setResult(Object result) {
			this.result = result;
		}
		@Override
		public String toString() {
			return "[flag=" + flag + ", result=" + result + "]";
		}
	}
}