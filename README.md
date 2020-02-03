# rest_template_demo
RestTemplate 使用介绍 RestTemplate发送GET、POST请求的示例项目
## 前言

今天在学习Spring Cloud的过程中无意发现了 RestTemplate 这个Spring 提供的Http Rest风格接口之间调用的模板工具类，感觉比Apache提供的HttpClient更加轻量化，只需要在容器中初始化该对象之后就可以使用，因此决定探索一下。

## 准备工作

首先创建一个SpringBoot父工程，添加必要的依赖，创建Mysql表，加入一些测试数据：

这里我创建了一个 emp 员工表，添加了两条测试数据，SQL语句如下：

### 创建表

```sql
DROP TABLE IF EXISTS `emp`;

CREATE TABLE `emp` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `join_time` datetime DEFAULT NULL COMMENT '入职时间',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别 0 未知 1 男 2 女',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `education_background` varchar(64) DEFAULT NULL COMMENT '教育背景',
  `age` tinyint(3) unsigned DEFAULT NULL COMMENT '年龄',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `emp` */

insert  into `emp`(`id`,`name`,`join_time`,`sex`,`address`,`education_background`,`age`,`remark`) values (1,'无忧','2020-01-28 21:17:07',1,'陕西省/西安市/雁塔区/某某街道','本科',23,'创始人'),(2,'忘我','2020-01-28 21:18:12',1,'陕西省/西安市/雁塔区/某某街道','本科',22,'合伙人');

```

### 逆向生成

接着逆向生成实体类，Mapper和XML文件。这里我参考了博客：[坚持到底的博客](https://www.cnblogs.com/zhouguanglin/p/11239583.html) 博主解释的很详细，我就不重复说明了。

### Service和Controller

service和controller 书写起来也比较简单，只是一些基本的CRUD，这里我额外的添加了pageHelper 分页助手插件，为了更好的模拟线上真实环境。

至此，整个项目的目录结构如下：

![1580223958684](https://img2018.cnblogs.com/blog/1654189/202002/1654189-20200203163957013-2018843518.png)

通过浏览器访问对应的接口地址，获得的数据如下。证明项目已经跑通。准备工作完成：

![1580224000291](https://img2018.cnblogs.com/blog/1654189/202002/1654189-20200203163956413-693398953.png)

## 初步使用RestTemplate

### 创建项目

新建一个SpringBoot项目，为了方便，我这里只写Controller

### 利用SpringIOC创建Bean

在启动类中往容器中注入RestTemplate的Bean

```java
@SpringBootApplication
public class BossApplication {
    public static void main(String[] args) {
        SpringApplication.run(BossApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

### 使用其Get Api

在新项目中的Controller中注入Bean，然后调用其对应的Api即可快速的通过Http的方式调用接口了

```java
@RestController
@RequestMapping("boss")
public class BossController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("employ/{id}")
    public ResponseEntity<Emp> employById(@PathVariable Long id){
        ResponseEntity<Emp> emp = null;
        emp = restTemplate.getForEntity("http://127.0.0.1:9001/emp/" + id, Emp.class);
        return emp;
    }
}
```

测试效果如下，可以看到利用9002端口的项目已经调用到9001的项目并成功获取数据：

![1580225028150](https://img2018.cnblogs.com/blog/1654189/202002/1654189-20200203163955832-195157848.png)

## 深入使用

前面我们已经可以初步的使用RestTemplate进行服务之间通过HttpRest请求进行调用了，接下来我们更深入的探索其他的API。Http Rest请求常用的请求方法有4种，分别是 POST、DELETE、PUT、GET。分别对应增删改查4个动作。而在url中加上操作的资源名称。并不出现动词便是我理解的RestFul风格API。下面我对不同的请求方法分别详细介绍其对应的调用方法。

### GET

项目中查询的接口一般有两种：根据ID查询详情和根据参数查询列表。根据ID查询详情我已经在前面展示了

这里要说明的就是 getForEntity 和 getForObject 方法，他两的参数是相同的，但返回值类型不同 getForEntity 返回的是 ResponseEntity<T> 对象，在我看来就是将实体类包装，并且携带了响应的状态码，读者可以按需使用。相应的源码如下：

```java
@Nullable
public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
    RequestCallback requestCallback = this.acceptHeaderRequestCallback(responseType);
    HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor(responseType, this.getMessageConverters(), this.logger);
    return this.execute(url, HttpMethod.GET, requestCallback, responseExtractor, (Object[])uriVariables);
}

// 可以看到，getForEntity 只是对getForObject的结果进行强制类型转换，并没有什么特别的操作
public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
    RequestCallback requestCallback = this.acceptHeaderRequestCallback(responseType);
    ResponseExtractor<ResponseEntity<T>> responseExtractor = this.responseEntityExtractor(responseType);
    return (ResponseEntity)nonNull(this.execute(url, HttpMethod.GET, requestCallback, responseExtractor, uriVariables));
}
```

#### 带请求参数的GET

项目中一般都会有分页条件查询的接口，此时请求参数以key value 形式提交。我们只需要调用对应的重载方法即可

例如有如下接口。根据性别分页查询员工：

```java
@RequestMapping("/list/{sex}/{pageNum}/{pageSize}")
public List<Emp> getByParam(@PathVariable Integer sex,
                            @PathVariable Integer pageNum,
                            @PathVariable Integer pageSize){
    return empService.getListByPara(pageNum, pageSize, sex);
}
```

我们通过RestTemplate可以这样查询：

```java
@RequestMapping("employList/{sex}")
public List employById1(@PathVariable Integer sex,
                        @RequestParam Integer pageNum,
                        @RequestParam Integer pageSize) {
    // 直接返回对象
    List emp = null;
    Map<String, Object> paraMap = new HashMap<>();
    paraMap.put("pageNum", pageNum);
    paraMap.put("pageSize", pageSize);
    paraMap.put("sex", sex);
    // 通过map的形式，spring会将url对应的参数key替换成value
    //        emp = restTemplate.getForObject("http://127.0.0.1:9001/emp/list/{sex}/{pageNum}/{pageSize}", List.class, paraMap);
    // 通过可变参的形式，spring会按顺序替换url中的参数
    emp = restTemplate.getForObject("http://127.0.0.1:9001/emp/list/{sex}/{pageNum}/{pageSize}", List.class, pageNum, pageSize, sex);
    return emp;
}
```

以上是完全按照Rest风格的GET url，参数都是在url上以PathVariable 的形式出现的，但如果参数不在url上，就需要稍加修改了

#### 非Rest风格GET请求

我们知道，GET请求的请求参数一般都在url上拼接。url后面以 ? 开始，key=value 的形式拼接多个请求参数。多个键值对之间用 & 符号连接。我琢磨了很久也只发现了字符串拼接这种传参方式，(惭愧，回去该好好读读 http 请求相关的书籍了)如果读者发现了更好的方法请不吝赐教!实现代码如下：

```java
@RequestMapping("employList1/{sex}")
public List employList1(@PathVariable Integer sex,
                        @RequestParam Integer pageNum,
                        @RequestParam Integer pageSize,
                        HttpServletRequest request) {
    // 直接返回对象
    List emp = null;
    // 请求参数
    Map<String, Object> map= new HashMap<>();
    map.put("pageNum", pageNum);
    map.put("pageSize", pageSize);
    map.put("sex", sex);
	// 在url中拼接参数，然后由spring从map中将值替换
    emp = restTemplate.getForObject("http://127.0.0.1:9001/emp/list/{sex}" + "?pageNum={pageNum}&pageSize={pageSize}", List.class, map);
    return emp;
}
```

### POST

项目中，POST请求一般以JSON的形式发送。基本的发送方法很简单。将对象打包好扔到方法的第二个参数就可以发送了

```java
@RequestMapping("new")
public int addEmploy(@RequestBody Emp emp) {
    return restTemplate.postForObject("http://127.0.0.1:9001/emp/new", emp, Integer.class);
}
```

PS：在测试 POST 请求的过程中我发现了两处配置问题

1. 插入的数据乱码，这个需要在数据库连接url中加参数。设置编码位 utf8

```url
url: jdbc:mysql://192.168.25.128:3306/mysql?characterEncoding=UTF-8&useSSL=false&serverTimezone=CTT

```

1. 之前写的 select * 查询没有开启mysql下划线到Java驼峰的自动转换，导致入职日期，教育背景字段没有读取成功

这个在 application.yml 中可以配置：

```yml
mybatis:
  mapper-locations: classpath:cn/keats/mapper/*Mapper.xml
  configuration:
    map-underscore-to-camel-case: true

```

### PUT DELETE

put 请求类似于 post，delete 请求类似于 get。这里就不在赘述了

## 后记

RestTemplate的介绍就到这里了，以上的方法基本涵盖了项目目前所有的接口，使用RestTemplate给我的感受就是如果接口完全按照Rest风格书写，使用其方法很简单，基本上一行代码就调用成功了。如果不是完全按照Rest风格的接口就需要多花费一些时间来二次封装接口了。推荐调用Rest风格API时候使用。

## GITHUB项目地址

此博客的所有代码都已经同步上传至我的GITHUB仓库中，想要练手的朋友不妨克隆下来使用

[RestTemplate 使用介绍 RestTemplate发送GET、POST请求的示例项目](https://github.com/keatsCoder/rest_template_demo)
