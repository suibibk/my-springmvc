package cn.myforever.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 加入了这个注解的类就是一个控制器
 * @author forever
 *
 */
@Target({ElementType.TYPE})//只能在类上面
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
