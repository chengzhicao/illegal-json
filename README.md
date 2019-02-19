# 使用Gson反序列化不规范Json数据

## 1.功能及特点
>1.摆脱后台数据格式对前端的影响，前端实体类不一定完全按照后台的格式写<br>
2.增加json反序列化的容错能力<br>
3.使用`@Select`获取json中不同层级的值<br>
4.兼容Gson的注解方法

## 2.使用方式
1. 在类上使用
```
@JsonAdapter(value = IllegalJsonDeserializer.class)
public class Student{

}
```
2. 在字段中使用
```
public class Person{

    @JsonAdapter(value = IllegalJsonDeserializer.class)
    public Student student;
    
    @Select("address.city")
    public String city;
}
```

## 3.详细介绍
> https://www.jianshu.com/p/990b66a6bc3e