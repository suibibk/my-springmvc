package cn.myforever.entity;
/**
 * url映射的对象和方法
 * @author forever
 *
 */
public class ObjectMethod {
	private Object object;
	private Object method;
	private String type;//返回值类型，加上@ResponseBody的就是json，否则就是页面   0是json 1是页面
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public Object getMethod() {
		return method;
	}
	public void setMethod(Object method) {
		this.method = method;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ObjectMethod [object=" + object + ", method=" + method + ", type=" + type + "]";
	}
	
}
