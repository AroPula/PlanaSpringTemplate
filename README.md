# PlanaSpringTemplate

## 项目简介✈️

> PlanaSpringTemplate 是一个手写 Spring、Tomcat、Dubbo、mybatis 等技术的项目，旨在深入理解这些流行框架的底层实现原理，并提供一个轻量级的替代方案。
> 该项目集成了 Spring 的依赖注入（DI）和面向切面编程（AOP）、Tomcat 的 Web 服务器功能以及 Dubbo 的 RPC 服务框架，适用于学习和研究目的。

## 核心模块🚩

1. **手写 Spring 框架** ：实现了基本的依赖注入和面向切面编程功能，简化了 Spring 的复杂性，同时保留了核心功能。
2. **手写 Tomcat 服务器** ：具备处理 HTTP 请求和响应的能力，支持部署简单的 Web 应用程序。
3. **手写 Dubbo 服务框架** ：实现了服务的注册、发现和调用功能，支持 RPC 通信。
3. **手写 Mybatis 框架** ：实现了mapper.xml文件的编写、读取配置文件、连接mysql数据库兼容jdbc

## 示例代码👻

### Spring 示例

```java
import org.springframework.stereotype.Component;

// 定义一个简单的Bean UserService.java
public interface UserService {
    void sayHello();
}

//实现接口 Impl/UserServiceImpl.java
@Component
public class UserServiceImpl implements UserService{
    public void sayHello(){
        System.out.println("Plana Spring Template!");
    }
}

//设置扫描路径 AppConfig.java
@ComponentScan("com.spring.service")
public class AppConfig {

}

//测试类 PlanaSpringExample.java
public class PlanaSpringExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.sayHello();
    }
}
```

```java
@Component("userService") //给bean命名
@Scope("prototype")  //声明是否为单例，默认为singleton单例
public class UserServiceImpl implements UserService {

    @Autowired  //自动装配
    private OrderService orderService;

    private String BeanName;
    
    @Override
    public void test() {
        System.out.println(orderService.order());
    }

}
```

### Mybatis示例
> 1.书写mybatis-config配置文件
```xml
<configuration>
    <!-- dataSource为数据库连接源 -->
    <dataSource>
        <property name="driveClass" value="com.mysql.cj.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/richu1?useSSL=false&amp;serverTimezone=UTC"/>
        <property name="username" value="root"/>
        <property name="password" value="password"/>
    </dataSource>

    <!-- mapper为Mapper-xml文件的位置 -->
    <mapper resource="mapper/UserMapper.xml"/>
</configuration>
```

> 2.书写mapper文件
> 这里给出一个userMapper的实例java接口文件
```java
public interface UserMapper {
    List<User> list();

    User findUserById(Long id);

    Integer addUser(User user);

    Integer updateUser(User user);

    Integer deleteUser(Long id);
}
```
> 这里给出一个userMapper的实例xml文件
``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<mapper namespace="com.richuff.mapper.UserMapper">
    <select id="list" resultType="com.richuff.entity.User">
        select * from user
    </select>

    <select id="findUserById" parameterType="java.lang.Long" resultType="com.richuff.entity.User">
        select * from user where id = #{id}
    </select>

    <insert id="addUser" parameterType="com.richuff.entity.User">
        insert into user(id,name,age) values(#{id},#{name},#{age})
    </insert>

    <update id="updateUser" parameterType="com.richuff.entity.User">
        update user set name=#{name},age=#{age} where id = #{id}
    </update>

    <delete id="deleteUser"  parameterType="java.lang.Long">
        delete from user where id=#{id}
    </delete>
</mapper>
```

> 3.在Java代码中初始化mybatis并调用Mapper
```java
public class MybatisTest {
    UserMapper userMapper;

    @Before
    public void init()  {
        String resourceName = "mybatis-config.xml";
        InputStream resource = Resource.getResourceAsStream(resourceName);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource);
        SqlSessionDefault sqlSession = (SqlSessionDefault) sqlSessionFactory.openSession();
        userMapper = (UserMapper) sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void testGet(){
        Long id = 3L;
        User user = userMapper.findUserById(id);
        System.out.println("user = " + user);
    }

    @Test
    public void testDelete(){
        Long id = 3L;
        Integer count = userMapper.deleteUser(id);
        System.out.println("count = " + count);
    }

    @Test
    public void testUpdate(){
        Integer integer = Integer.valueOf("100");
        User user = User.builder().id(666L).name("cnm").age(integer).build();
        Integer count = userMapper.updateUser(user);
        System.out.println("count = " + count);
    }

    @Test
    public void testInsert(){
        Integer integer = Integer.valueOf("100");
        User user = User.builder().id(3L).name("cnm").age(integer).build();
        Integer count = userMapper.addUser(user);
        System.out.println("count = " + count);
    }
}
```

## 贡献指南🤝
🤝 如何贡献
> 我们欢迎并感谢任何形式的贡献！无论是修复 typo、提交 issue、改进文档，还是提交新功能，你的帮助都会让项目变得更好。

✅ 开始之前
1. 请先阅读 行为准则（如适用）。
2. 确保你熟悉项目的 README 和目录结构。
3. 如果你要报告 bug 或提议新功能，请先搜索 Issues 是否已存在类似内容。

🐛 报告问题（Issue）
> 请使用我们的 Issue 模板 提交：

1.清晰的标题

2.复现步骤

3.期望行为 vs 实际行为

4.系统环境（如操作系统、浏览器、版本号等）

🔧 提交代码（Pull Request）
1. Fork 仓库
> 点击右上角的「Fork」按钮，将项目复制到你的账户下。
2. 克隆到本地
```bash
git clone https://github.com/你的用户名/你的项目.git
cd 你的项目
```
3. 创建新分支

```bash
git checkout -b fix/你的修复描述
git checkout -b feature/你的新功能名称
```
4. 提交更改
```bash
# 写清楚 commit message，建议使用 Conventional Commits 规范：
fix: 修复了导航栏在移动端显示异常的问题
feat: 新增用户头像上传功能
docs: 更新安装指南中的 Node 版本要求
```
5. 推送到你的 Fork
```bash
git push origin fix/你的修复描述
```
6. 提交 Pull Request
>    标题简洁明了，说明「做了什么」,正文可引用关联的 Issue，例如： Closes #123

🙋‍♂️ 有疑问？

> 欢迎开 Discussion 或在 Issue 中提问，我们会尽快回复！

## 许可证🚀

本项目遵循 [MIT License](LICENSE) 许可证，您可以自由使用、修改和分发该项目的代码。

## 特别感谢❤️
<a href="https://github.com/AroPula/PlanaSpringTemplate/graphs/contributors" target="_blank">
  <img src="https://contrib.rocks/image?repo=AroPula/PlanaSpringTemplate" />
</a>