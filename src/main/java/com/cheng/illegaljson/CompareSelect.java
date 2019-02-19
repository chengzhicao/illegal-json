package com.cheng.illegaljson;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果json串中有多个具有相同结构的字段，且只有一个字段是有值的，那说明可以用同一个字段来统一接收这个数据内容
 * 这个情况不常见，仅仅是为我本人参与的项目专门写的.
 * <pre>   {@code
 *  “newsInfo”:{
 *      "id":34212343,
 *      "code":2342345,
 *      "icon":"http://vn.jpg"
 *  }
 *  “specialInfo”:{
 *      "id":123123123,
 *      "code":455677,
 *      "icon":"https://aa.jpg"
 *  }
 * }</pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CompareSelect {
    String[] value() default {};
}
