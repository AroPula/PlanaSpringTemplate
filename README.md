# PlanaSpringTemplate 项目 README

## 项目简介

> PlanaSpringTemplate 是一个手写 Spring、Tomcat 和 Dubbo 等技术的项目，旨在深入理解这些流行框架的底层实现原理，并提供一个轻量级的替代方案。
> 该项目集成了 Spring 的依赖注入（DI）和面向切面编程（AOP）、Tomcat 的 Web 服务器功能以及 Dubbo 的 RPC 服务框架，适用于学习和研究目的。

## 核心模块

1. **手写 Spring 框架** ：实现了基本的依赖注入和面向切面编程功能，简化了 Spring 的复杂性，同时保留了核心功能。
2. **手写 Tomcat 服务器** ：具备处理 HTTP 请求和响应的能力，支持部署简单的 Web 应用程序。
3. **手写 Dubbo 服务框架** ：实现了服务的注册、发现和调用功能，支持 RPC 通信。

## 快速入门

1. **克隆项目**
使用 Git 命令克隆项目到本地：
    `git clone https://github.com/richuff/PlanaSpringTemplat.git`

## 示例代码

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
### Tomcat 示例

```java
// 定义一个简单的 Servlet
public class HelloServlet implements Servlet {
    @Override
    public void init(ServletConfig config) {}

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) {
        try {
            response.getWriter().write("Hello, Plana Tomcat Template!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {}
}

public class TomcatExample {
    public static void main(String[] args) {
        try {
            // 创建 Tomcat 实例并设置端口
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);

            // 添加 Servlet 映射
            Context context = tomcat.addContext("", new File(".").getAbsolutePath());
            Tomcat.addServlet(context, "helloServlet", new HelloServlet());
            context.addServletMappingDecoded("/", "helloServlet");

            // 启动 Tomcat 服务器
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Dubbo 示例

```java
// 定义一个服务接口
public interface HelloService {
    String sayHello(String name);
}

// 实现服务接口
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}

// 服务提供方
public class ProviderExample {
    public static void main(String[] args) {
        // 创建服务 registry
        Registry registry = new SimpleRegistry();

        // 创建服务导出器并导出服务
        Exporter<HelloService> exporter = new DubboExporter<>(
            new HelloServiceImpl(),
            new ServiceConfig<HelloService>()
                .setInterface(HelloService.class)
                .setRef(new HelloServiceImpl())
                .setRegistry(registry)
                .setApplication(new ApplicationConfig("plana-dubbo-template-provider"))
                .setProtocol(new ProtocolConfig("dubbo", 20880)
                    .setThreads(200))
                .setRegistry(new RegistryConfig("multicast://224.5.6.7:1234")));
        exporter.export();

        // 启动服务提供方
        System.out.println("Plana Dubbo Template Provider started!");
    }
}

// 服务消费方
public class ConsumerExample {
    public static void main(String[] args) {
        // 创建服务 registry
        Registry registry = new SimpleRegistry();

        // 引用远程服务
        ReferenceConfig<HelloService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("plana-dubbo-template-consumer"));
        reference.setRegistry(new RegistryConfig("multicast://224.5.6.7:1234"));
        reference.setInterface(HelloService.class);
        reference.setProtocol("dubbo");
        reference.setVersion("1.0.0");
        reference.setTimeout(3000);

        // 获取服务代理并调用方法
        HelloService helloService = reference.get();
        String result = helloService.sayHello("Plana");
        System.out.println(result);
    }
}
```

## 技术栈

* 编程语言：Java
* 构建工具：Maven
* 依赖管理：Maven 依赖管理

## 贡献指南

欢迎对本项目进行贡献！如果您发现任何问题或有改进建议，请按照以下步骤进行：

1. **Fork 项目** ：在 GitHub 上 fork 本项目到您的个人仓库。

2. **提交更改** ：进行代码更改并提交，确保提交信息清晰描述更改内容。
    * `git commit -m "Add your commit message"`

3. **推送分支** ：将您的分支推送到您的 fork 仓库。
    * `git push origin main`

4. **提交 Pull Request** ：在 GitHub 上向原项目仓库提交 Pull Request，详细描述您的更改。

## 许可证

本项目遵循 [MIT License](LICENSE) 许可证，您可以自由使用、修改和分发该项目的代码。