package com.MissX.utils.check;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import com.MissX.utils.annotations.MissX;


/**
 * 	实体类空验证工具
 *	time 2018年9月27日 下午4:31:27
 */
public class EmptyCheckUtil {

public static final String [] CHECK_PARAMS = {" ","","[]","{}","undefined"};	//需要进行验证的值(不区分大小写)
	
	/**
	 * Map执行空验证的方法
	 * @param key 当前验证参数名
	 * @param entry 当前验证参数值
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws CheckException 抛出空验证失败异常
	 */
	private static boolean MapCheckMethod(String key,Object entry,boolean exception,boolean print) throws CheckException {
		boolean bool = false;
		if(null != entry) {  //首先判断需要验证的value是否为null
			for (int i = 0; i < CHECK_PARAMS.length; i++) {
				if(print) {	//判断是否需要打印验证过程
					System.out.println(key + " : " + entry.toString().toLowerCase()+" ---> "+CHECK_PARAMS[i]);
				}
				if((CHECK_PARAMS[i].equals(entry.toString().toLowerCase()))) {	//判断value中是否有CHECK_PARAMS中对应的值
					bool = true;
					if(exception) { //空验证失败时判断是否需要抛出异常
						throw new CheckException(key + "的结果为 :'" + entry.toString()+"'");
					}
					break;
				}
			}
		}else {
			bool = false;
			if(exception) {
				throw new CheckException(key + "的结果为 : null");
			}
		}
		return bool;
	}
	
	/**
	 * 验证传入Map指定key中是否有值为空()
	 * @param map @param map 需要验证的Map
	 * @param params params 需要验证的Key
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws CheckException 空验证失败时抛出此异常
	 */
	public static boolean EmptyCheckMapByParamsIsArray(Map<String,Object> map,String [] params,boolean exception,boolean print) throws CheckException{
		try {
			Set<String> keySet = map.keySet();	//获取map的KeySet
			for (String key : keySet) {		//遍历map的key
				for (String param : params) {	//遍历需要验证的key名字
					if(key.equals(param)) {		//判断map中的key是否有需要验证的值
						Object entry = map.get(key); //获取对应key的value值
						if(MapCheckMethod(key,entry,exception,print)) { //进入MapCheckMethod方法判断返回值是否为空
							return false;
						}
					}
				}
			}
			return true;
		}catch(CheckException e) {
			throw e;
		}
	}
	
	/**
	 * 验证传入Map全部key中是否有值为空
	 * @param map 需要验证的Map
	 * @param params 需要验证的Key
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws CheckException 空验证失败时抛出此异常
	 */
	public static boolean EmptyCheckMapByParamsIsArray(Map<String,Object> map,boolean exception,boolean print) throws CheckException{
		try {
			Set<String> keySet = map.keySet();	//获取map的KeySet
			for (String key : keySet) {		//遍历map的key
				Object entry = map.get(key);	//获取对应key的value值
				if(MapCheckMethod(key,entry,exception,print)) {	//进入MapCheckMethod方法判断返回值是否为空
					return false;
				}
			}
			return true;
		}catch(CheckException e) {
			throw e;
		}
	}
	
	/**
	 * 实体类执行空验证的方法
	 * @param name 当前验证参数名
	 * @param value 当前验证参数值
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws CheckException 抛出空验证失败异常
	 */
	private static boolean ModelCheckMethod(String name,Object value,boolean exception,boolean print) throws CheckException {
		boolean bool = false;
		if(null != value) {	//首先判断需要验证的value是否为null
			for (int i = 0; i < CHECK_PARAMS.length; i++) {
				if(print) {	//判断是否需要打印验证过程
					System.out.println(name + " : " + value.toString().toLowerCase()+" ---> "+CHECK_PARAMS[i]);
				}
				if((CHECK_PARAMS[i].equals(value.toString().toLowerCase()))) {	//判断value中是否有CHECK_PARAMS中对应的值
					bool = true;
					if(exception) {	//空验证失败时判断是否需要抛出异常
						throw new CheckException(name + "的结果为 :'" + value.toString()+"'");
					}
					break;
				}
			}
		}else {
			bool = false;
			if(exception) {
				throw new CheckException(name + "的结果为 : null");
			}
		}
		return bool;
	}
	
	/**
	 * 空验证工具,在需要验证的属性上添加注解@MissX(泛型方法)(只能传入实体类)
	 * 注:若未添加注解则始终验证通过
	 * @param<T> 泛型方法
	 * @param model 需要验证的实体类
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws Exception 可选择验证失败时抛出异常
	 */
	public static <T> boolean EmptyCheckModelByMissXAnnotation(T model,boolean exception,boolean print) throws Exception{
		try {
			Field[] fields = model.getClass().getDeclaredFields(); //获取所有自定义属性
			for (Field field : fields) {
				String name = field.getName();	//获取每个属性的名字,用于下面验证时拼接
				MissX xia = field.getDeclaredAnnotation(MissX.class);
				if(null!=xia) {	//判断属性上是否拥有注解MissX
					Method[] methods = model.getClass().getDeclaredMethods();	//获取所有自定义的方法
					for (Method method : methods) {	//遍历所有自定义方法
						if(method.getName().toLowerCase().equals(("get"+name).toLowerCase())) {	//判断当前方法是否为get方法
							Object result = method.invoke(model);	//执行当前get方法并获取返回值
							if(ModelCheckMethod(method.getName(), result, exception, print)) {	//进入ModelCheckMethod方法判断返回值是否为空
								return false;
							}
						}
					}
				}
			}
			return true;
		}catch(CheckException e) {
			throw e;
		}
	}
		
	/**
	 * 验证传入实体类中指定参数是否为空(泛型方法)(只能传入实体类)
	 * @param <T> 泛型方法
	 * @param model 需要验证的实体类
	 * @param params 需要验证的属性
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws Exception 可选择验证失败时抛出异常
	 */
	public static <T> boolean EmptyCheckModelParamIsArray(T model,String [] params,boolean exception,boolean print) throws Exception {
		try {
			Method[] methods = model.getClass().getDeclaredMethods();//获取传入参数的类型中包含自定义的方法
			for (String str : params) {
				String checkName = ("get" + str).toLowerCase();	//将需要验证的参数拼接为get方法
				for (Method method : methods) {
					if(method.getName().startsWith("get")) { //判断传入参数中的所有以get开头的方法
						if (checkName.equals(method.getName().toLowerCase())) { //判断当前get方法是否为需要验证的方法
							Object result = method.invoke(model);	//执行当前get方法并获取返回值
							if(ModelCheckMethod(method.getName(), result, exception, print)) {	//进入ModelCheckMethod方法判断返回值是否为空
								return false;
							}
						}
					}
				}
			}
		} catch (CheckException e) {
			throw e;
		}
		return true;
	}
	
	/**
	 * 验证传入实体类中全部参数是否为空(泛型方法)(只能传入实体类)
	 * @param <T> 泛型方法
	 * @param model 需要验证的实体类
	 * @param exception 是否需要抛出异常
	 * @param print 是否需要打印验证过程
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws Exception 可选择验证失败时抛出异常
	 */
	public static <T> boolean EmptyCheckModelParamIsArray(T model,boolean exception,boolean print) throws Exception {
		try {
			Method[] methods = model.getClass().getDeclaredMethods();//获取传入参数的类型中包含自定义的方法
				for (Method method : methods) {
					if(method.getName().startsWith("get")) { //判断传入参数中的所有以get开头的方法
						Object result = method.invoke(model);	//执行当前get方法并获取返回值
						if(ModelCheckMethod(method.getName(), result, exception, print)) {	//进入ModelCheckMethod方法判断返回值是否为空
							return false;
						}
					}
				}
		} catch (CheckException e) {
			throw e;
		}
		return true;
	}
	
	/**
	 * @param param 需要验证的字符串
	 * @param exception 是否需要抛出异常
	 * @return 返回验证结果，true为通过，false为未通过，若exception为true时会抛出相应异常
	 * @throws Exception 可选择验证失败时抛出异常
	 */
	public static boolean EmptyCheckString(String param,boolean exception) throws CheckException {
		boolean bool = true;
		try {
			if(null != param) {
				for (int i = 0; i < CHECK_PARAMS.length; i++) {
					if((CHECK_PARAMS[i].equals(param.toString().toLowerCase()))) {	//判断value中是否有CHECK_PARAMS中对应的值
						bool = false;
						if(exception) {	//空验证失败时判断是否需要抛出异常
							throw new CheckException("验证结果 :'" + param.toString()+"'");
						}
						break;
					}
				}
			}else {
				bool = false;
				if(exception) {
					throw new CheckException("验证结果 :'" + param+"'");
				}
			}
		} catch (CheckException e) {
			throw e;
		}
		return bool;
	}
	
	/**
	 * 自定义异常类型(空验证失败时抛出此异常)
	 * Time 2018年12月21日 下午3:13:37
	 * @author HeQuan
	 * Admin
	 */
	public static class CheckException extends Exception{
		private static final long serialVersionUID = 1L;
		public CheckException(String message) {
			super(message);
		}
	}
}