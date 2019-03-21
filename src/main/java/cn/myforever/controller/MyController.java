package cn.myforever.controller;

import java.util.HashMap;
import java.util.Map;

import cn.myforever.annotation.Controller;
import cn.myforever.annotation.RequestMapper;
import cn.myforever.annotation.ResponseBody;
import cn.myforever.utils.Const;

@Controller
@RequestMapper("/springmvc")
public class MyController {
	//http://localhost:8080/springmvc-test-0.0.1-SNAPSHOT/springmvc/index
	@RequestMapper("/index")
	public String index() {
		return "index";
	}
	
	//http://localhost:8080/springmvc-test-0.0.1-SNAPSHOT/springmvc/index
	@RequestMapper("/info")
	@ResponseBody(type = Const.JSON)//返回json格式
	public Map<String,Object> info() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", "林文华");
		return map;
	}
}