package cn.myforever.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 映射路径
 * @author forever
 *
 */
@Target({ElementType.TYPE,ElementType.METHOD})//只能在类或者方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapper {
	String value();
}
