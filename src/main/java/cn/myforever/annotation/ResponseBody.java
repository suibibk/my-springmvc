package cn.myforever.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 返回值的类型 0是json 1是jsp 默认jsp
 * @author forever
 *
 */
@Target({ElementType.METHOD})//方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseBody {
	String type();
}
