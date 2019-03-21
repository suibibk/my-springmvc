package cn.myforever.dispatcher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cn.myforever.annotation.Controller;
import cn.myforever.annotation.RequestMapper;
import cn.myforever.annotation.ResponseBody;
import cn.myforever.entity.ObjectMethod;
import cn.myforever.utils.ClassUtil;
import cn.myforever.utils.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


public class MyDispatcher extends HttpServlet {
	//放置所有的controller,key是类名称小写 value是类对象 用线程安全的map
	private ConcurrentHashMap<String, Object> controllers = new ConcurrentHashMap<String, Object>();
	//url和对象映射
	private ConcurrentHashMap<String, Object> objectMappings = new ConcurrentHashMap<String, Object>();
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("初始化完成：controllers="+controllers.toString()+";objectMappings="+objectMappings.toString());
		//获取url
		String url =request.getRequestURI();
		System.out.println("用户的url:"+url);
		//从map中获取路径
		ObjectMethod om =(ObjectMethod) objectMappings.get(url);
		if(om==null) {
			response.getWriter().write("404");
			return;
		}
		//执行方法，获取返回值
		Method method = (Method) om.getMethod();
		try {
			//获取类型
			String type = (String) om.getType();
			Object object = method.invoke(om.getObject());
			System.out.println("object:"+object);
			if(type.equals(Const.JSON)) {
				response.getWriter().write(new Gson().toJson(object));
			}else {
				//默认跳转倒到页面
				request.getRequestDispatcher("/WEB-INF/jsp/"+object+".jsp").forward(request, response);
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//初始化的时候会调用
	public void init() throws ServletException {
		System.out.println("初始化开始");
		//初始化bean，扫包
		List<Class<?>> clazzs = ClassUtil.getClasses("cn.myforever.controller");
		System.out.println("clazzs is "+clazzs);
		if(clazzs==null){
			System.out.println("clazzs is null");
			return;
		}
		//初始化含有Controller注解的类，放入集合对象中
		try {
			initControllerType(clazzs);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//初始化映射器
		initHandlerMapping();
		System.out.println("初始化完成：controllers="+controllers.toString()+";objectMappings="+objectMappings.toString());
	}

	/**
	 * 初始化处理器映射器，这里就是找出链接对应的方法即可
	 */
	private void initHandlerMapping() {
		//遍历有controller的map，查看类上面是否有map
		for(Entry<String,Object> entry:controllers.entrySet()) {
			//获取对象
			Object object = entry.getValue();
			Class<?> clazz = object.getClass();
			//查看该对象上是否有RequestMapping注解
			String typeUrl = "/my-springmvc-0.0.1-SNAPSHOT";
			//这个方法只有1.8才有，所以这里要用jdk1.8，也就是tomcat1.8
			RequestMapper requestMapper = clazz.getDeclaredAnnotation(RequestMapper.class);
			if(requestMapper!=null) {
				typeUrl +=requestMapper.value();
			}
			//处理这个object
			dealObjectMethod(object,clazz,typeUrl);
		}
		
	}
	/**
	 * 处理请求映射
	 * @param clazz
	 * @param typeUrl
	 */
	private void dealObjectMethod(Object object,Class<?> clazz,String typeUrl){
		//循环处理该类的方法，判断是否有注解
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			RequestMapper requestMapper2 = method.getDeclaredAnnotation(RequestMapper.class);
			if(requestMapper2!=null) {
				//这里有值，就表明这个方法是一个请求映射
				String url = typeUrl+requestMapper2.value();
				//判断有没有返回值
				String type = Const.PAGE;//默认返回页面
				ResponseBody responseBody  =method.getDeclaredAnnotation(ResponseBody.class);
				if(responseBody!=null) {
					type= responseBody.type();
				}
				//这里就可以封装map了
				ObjectMethod om = new ObjectMethod();
				om.setObject(object);
				om.setMethod(method);
				om.setType(type);
				objectMappings.put(url, om);
			}
		}
		
	}
	/**
	 * 初始化含有集合操作的类
	 * @param clazzs
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void initControllerType(List<Class<?>> clazzs) throws InstantiationException, IllegalAccessException {
		//遍历所有的类，获取类上的Controller注解
		System.out.println("1");
		for (Class<?> clazz : clazzs) {
			System.out.println("2");
			Controller controller = clazz.getDeclaredAnnotation(Controller.class);
			System.out.println("3");
			if(controller!=null){
				System.out.println("4");
				//获取类名称首字母小写当做key
				String  key = ClassUtil.toLowerCaseFirstOne(clazz.getSimpleName());
				Object object = clazz.newInstance();
				controllers.put(key, object);
				System.out.println("5:"+controllers.toString());
			}
		}
	}
	/*public static void main(String[] args) throws ServletException {
		MyDispatcher myDispatcher = new MyDispatcher();
		myDispatcher.init();
	}*/
}
