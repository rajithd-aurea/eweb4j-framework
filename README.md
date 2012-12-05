= 为什么要用 EWeb4J ？ =
----
    EWeb4J 是一个基于 Servlet/Jdbc 构建的轻量级 Java Web 开发框架。它可以代替 SSH  来开发一个完整的 Web 应用程序。
    它专注于 少侵入、少配置、松耦合、RESTful架构风格的 Web 应用程序开发。
    EWeb4J 的目标是让 Java Web 开发更加简单。
  
  *  独具特色的 RESTful 路由
  *  由客户端决定 HTTP 响应内容的格式
  *  智能的HTTP 参数绑定
  *  灵活可控的HTTP 参数验证
  *  超级方便的文件上传下载
  *  超级方便的视图数据传递
  *  充血模型
  *  声明式事务、事务模板、事务嵌套
  *  丰富的DAO封装类
  *  多数据源、表关联
  *  简单的 IOC 容器
  *  MVC、ORM、IOC 可控开关
  *  键值对配置文件支持
  *  国际化支持

  让我们看看它是如何做到的。
 
== 1. 独具特色的 RESTful 路由 ==
----
    讨厌 _Struts_ 的 Action 配置？ 讨厌 _SpringMVC_ 的 @RequestMapping 注解？ 
    讨厌 _JAX-RS_ 的 @Path @GET @POST @DELETE 注解？
  
    如果你的回答都是 yes 的话，恭喜你， EWeb4J 是你的菜。不妨看看下面这段代码：
    public class HelloAction{
        public String doHelloWorld(){
            return "uri: /hello-world , http: GET|POST|PUT|DELETE";
        }
    }

    从上面这个简单的例子可以直接看出，类名以 Action 结尾，方法名字以 do 开头，
    do 后面的 HelloWorld 就会映射为 /hello-world 。

    我们先不着急仔细研究它，继续看下面的例子。
    
    public class PetsControl{
    	public String doAtGet(){
        	return "uri: /pets , http: GET";
    	}
    	public String doBindIdAtGet(long id){
        	return "uri: /pets/{id} , http: GET";
    	}
    	public String doBindIdJoinEditAtGet(long id){
        	return "uri: /pets/{id}/edit , http: GET";
    	}
    	public String doBindIdAtDelete(long id){
        	return "uri: /pets/{id} , http: DELETE";
    	}
    	public String doBindIdAtPut(long id){
        	return "uri: /pets/{id} , http: PUT";
    	}
    	public String doAtPost(){
        	return "uri: /pets , http: POST";
    	}
    	public String doNewAtGet(){
        	return "uri: /pets/new , http: GET";
    	}
    }
    
    可以明显的看到这个类和上个类有些不一样，它是以 Control 结尾的，上一个是 Action 结尾的。那么这二者有什么区别呢？
    通过对比，可以发现，以 Control 结尾的类，它所有的方法名字映射到 uri 的时候，都会在前面多出一个前缀，
    比如上面的 PetsControl 类的所有方法 uri 前面都多出了 /pets/ 前缀。
    但是 HelloAction 这个类却没有多出 /hello/ 这样的前缀。这就是以 Action 结尾和 Control 结尾的不同之处。

    另外，可以看到方法是通过 At 来配置 HTTP method 的。
    
    总结一下，EWeb4J 框架的 HTTP 路由配置其实就是一套简单的 "命名规范"，也就是 "约定"。
    public String doUri1BindParam1AndParam2JoinUri2AtGetOrPostOrPutOrDelete(String param1, String param2){
    	return "uri: /uri1/{param1}/{param2}/uri2, http: GET|POST|PUT|DELETE";
    }

    如果上述方法所在的类命名以 Control 或 Controller 结尾, 那需要在上述 uri 前面增加该名字去除 Control 
    或 Controller 后的前缀。例如：PetsControl -> /pets/ 。
    
    控制器类的命名约定以 Action 或 Control 或 Controller 结尾。

== 2. 由客户端决定 HTTP 响应内容的格式 ==
----

== 3. 智能的 HTTP 参数绑定 ==
----
    你可以将 HTTP 参数绑定到 Java 类成员变量上，也可以绑定到 Java 方法参数上。不管你的参数是基本数据类型还是 POJO 。

    如果你希望将参数绑定到类成员变量上，那么为该变量写上一个 setter 方法即可。
    public class HelloAction{
    	private String name;
    	public String doHello(){
        	return "hello, " + name;
    	}
    	public void setName(String name){
        	this.name = name;
    	}
    }
    
    还可以将类成员变量封装到一个 POJO 上。
    public class Pet{
    	private String name;
    	// setter and getter
    }
    
    public class HelloAction{
    	private Pet pet;
    	public String doHello(){
        	return "hello, " + pet.getName();
    	}
    	public void setPet(Pet pet){
        	this.pet = pet;
    	}
    }
    
    这时候传递过来的参数应该是  pet.name ，如果有更多层次的嵌套，照写即可，例如 pet.master.name 。

    如果你希望将参数绑定到方法参数上，此时代码将是：
    public class HelloAction{
    	public String doHello(@QueryParam("name")String name){
        	return "hello, " + name;
    	}
    }
    
    public class Pet{
    	private String name;
    	// setter and getter
    }
    
    public class HelloAction{
    	public String doHello(@QueryParam("pet")Pet pet){
        	return "hello, " + pet.getName();
    	}
    }
    
    如果不希望绑定到任何“东西”上，那么也可以这么做：
    public class HelloAction{
    	public String doHello(QueryParams params){
        	return "hello, " + params.toStr("name");//or params.toStrs("name")[0]
    	}
    }

== 4. 灵活可控的 HTTP 参数验证 ==
----
    EWeb4J 框架对 HTTP 参数的验证也是很有特点的。
    #  在任何 POJO 的成员变量中定义验证规则，便于复用。
    #  在任何 Action 方法上给定需验证的参数集合，触发验证行为。
    #  在 Action 方法体内进行验证结果的处理，你有最大的控制权。
    不妨看看代码：
    public class HelloAction{
    	@Required(mess="名字必填")
    	@Length(min=2, max=6, mess="名字{min}到{max}位")
    	private String name;
    	@Validate({"name"})
    	public String doHelloBindName(Validation val){
        	if (val.hasErr())
            	return JsonConverter.convert(val.getAllErr());
        
        	return "Hello, " + this.name;
    	}    
    	//setter and getter
    }
    
    val.getAllErr() 其实是一个 Map<String, List<String>>
    大概结构是：{参数名：[信息1, 信息2, 信息3...]}
    例如：{name:[名字必填，名字2到6位]}

== 5. 超级方便的视图数据传递 ==
----
    在不使用继承的前提下，EWeb4J 框架也能让视图数据传递变得非常简单。你只需要在 Action 方法参数里多添加一个 Map 即可。

    不妨看看代码：
    public class HelloAction{
    	public String doHello(Map model){
        	model.put("name", "李小龙");    
        	return "fmt:index.html";
    	}
    }
    
    然后在 index.html 模板文件中取出即可 
    <h1>${name !}, Welcome to EWeb4J framework.</h1>

    如果你喜欢 Struts2 的 ModelDriven 的话，嘿嘿，EWeb4J 一样也有，不过，比起 Struts2 需要继承，
    EWeb4J 应该会更加优雅。你所需要做的就只是为你的类成员变量提供一个 getter 方法即可。

    还是上面那个例子，如果是 ModelDriven 的话，代码将会是：
    public class HelloAction{
    	private String name;
    	public String doHello(){
        	this.name = "李小龙";
        	return "fmt:index.html";
    	} 
    	public String getName(){
        	return this.name;
    	}
    }

    不管你的视图模板是 JSP、Freemarker 还是 Velocity， 你的这些代码都不需要修改。 
    看到了吗？它是如此的简单！再也不需要 Servlet Request 了，Action 方法的测试也变得很简单了。最重要的是，
    没有继承！没有实现任何接口，如此松耦合，还不合您胃口么？: )

== 6. 超级方便的文件上传下载 ==
----
    文件上传下载？这要涉及到文件IO流吧。或者，用第三方组件？例如Apache 的common-upload, 不不不，咱都不用，
    咱直接在控制器里声明一个 File 对象就行了。什么？这么简单？先看看代码吧~
    public class UploadControl{
    	private File file;
    	public String doAtPost(){
        	FileUtil.copy(file, new File("c://"+file.getName()));
    	}
    	public void setFile(File file){
        	this.file = file;
    	}
    }
    
    upload.html
    <form action="upload" method="POST" enctype="multipart/form-data">
    	<input type="file" name="file" />
    	<input type="submit" value="上传" />
    </form>
    PS：注意表单文件input的name要和控制器声明的File对象名一致（实质上是和setter方法名相关）。另外注意 enctype

    就这么简单，在一个Action方法执行前，框架会自动接收所有的文件上传请求，并将其作为一个临时文件保存到临时目录里，
    然后Action方法执行之后，框架会自动清空这些临时文件。因此，我们只要在Action方法体中，
    将临时文件拷贝到另外一个目录即可。下面有一个更加全面的例子：
    //文件上传
    public class UploadControl {
    	final static String path = ConfigConstant.ROOT_PATH + "/WEB-INF/uploads/";
    	private File tmpFile1;
    	private UploadFile tmpFile2;
    	private File[] tmpFile3;
    	private UploadFile[] tmpFile4;
    	
    	public String doAtGet() {
    		return "fmt:Upload/upload.html";
    	}
    	
    	public String doAtPost() {
    		//为了查看临时文件，当前线程睡眠10秒钟
    		//Thread.sleep(10*1000);
    		
    		FileUtil.copy(tmpFile1, new File(path + tmpFile1.getName()));
    		FileUtil.copy(tmpFile2.getTmpFile(), new File(path + tmpFile2.getFileName()));
    		
    		for (File f : tmpFile3)
    			FileUtil.copy(f, new File(path + f.getName()));
    		for (UploadFile f : tmpFile4)
    			FileUtil.copy(f.getTmpFile(), new File(path + f.getFileName()));
    			
    		return StringUtil.getNowTime()+"上传成功!" ;
    	}

        //别忘记setter方法哦
    }
    
    upload.html
    <h1>EWeb4J Framework File Upload Demo</h1>
    <form action="${BaseURL}upload" method="post" enctype="multipart/form-data">
    	<label>tmpFile1:</label><input type="file" name="tmpFile1" /><br />
    	<label>tmpFile2:</label><input type="file" name="tmpFile2" /><br />
    	<label>tmpFile3:</label><input type="file" name="tmpFile3" /><br />
    	<label>tmpFile3:</label><input type="file" name="tmpFile3" /><br />
    	<label>tmpFile4:</label><input type="file" name="tmpFile4" /><br />
    	<label>tmpFile4:</label><input type="file" name="tmpFile4" /><br />
    	
    	<input type="submit" value="上传" />
    </form>

    文件上传这么简单，我想聪明的你应该猜到，文件下载大概怎么做了吧！没错，仅需要在Action方法里返回一个File对象即可！
    如果有多个文件，那么请返回文件数组吧！框架会自动将其打包成zip。
    public class DownloadControl {
    	final static String path = ConfigConstant.ROOT_PATH + "/WEB-INF/uploads/";
    	final File file = new File(path+"just4download.jpg"); 
    	
    	public File doAtGet(){
    		return file;
    	}
    	
    	public File[] doArrayAtGet(){
    		return new File[]{file};
    	}
    }
    PS:框架采用标准的HTTP文件下载的方式进行响应，客户端可以自己修改文件下载保存名字。
    response.setContentType("application/zip");  
    response.addHeader("Content-Disposition", "attachment; filename=xxx");

== 7. 充血模型 ==
----
    尽管实现充血模型需要继承这种强耦合方式，但是这对于敏捷的web开发来说是好处很多的。它已经被 Play、ROR 这些框架证明了。
    EWeb4J 一直在坚持无继承、无接口实现的松耦合开发。但是还有另外一个原则：用户掌控一切。
    因此，EWeb4J 在这方面做了一个折中，既提供无继承、无接口实现的松耦合方案，也提供继承来实现充血模型的方案。
    最终选择权在用户手里。
    尽管如此，在使用 EWeb4J 框架做 web 开发时，我个人还是比较喜欢：
  *  实体模型类使用继承获得一些基本的持久化功能
  *  其他地方尽量使用松耦合，无继承、无实现接口，多使用组合。

    因为，充血模型能让 DAO 层免去。这对于开发来说是一件好事，因为它使得开发更加简单了。我们都喜欢简单的东西，不是吗？
    说了这么多，不妨来看看 EWeb4J 的充血模型代码：
    @Entity
    public class Pet extends Model{
    	private String name;
    	private int age;
    	@ManyToOne
    	private Master master;
    	
    	public Pet(){}
    	public Pet(String name, int age){
        	this.name = name;
        	this.age = age;
    	}
    	//setter and getter
    	
    	public static void main(String[] args){
        	String err = EWeb4JConfig.start();
        	if (err != null)
            	return ;
        
        	Pet pet = new Pet("小黑", 3);
        	/* 插入记录，调用该方法后，pet 对象的 ID 值已经被注入了 */

        	pet.create();//或者是 pet.save();
        	pet.create("name", "age");//也可以指定插入哪些字段

        	/* 更新记录，将 ID=3 的记录的名字由 小黑 改成 小白 */

        	pet.setName("小白");
        
        	pet.save();//相同的 save 方法，有 ID 值即是更新，否则是插入
        	pet.save("name", "age");//也可以指定更新哪些字段
        
        	/* 删除记录,根据当前 pet 对象的 ID 值查找删除 */
        	pet.delete();

        	/* 级联查询 */
        	pet.cascade().merge("master");//根据当前pet的ID获取master数据
        	/* 级联插入 */
        	pet.cascade().persist("master");//PS: XXToOne的关系不需要级联插入
        	final long newId = 5L;
        	/* 级联更新,对主键的更新级联到所有外键引用上去 */
        	new Master().cascade().refresh(newId, "pets");
	
        	/* 级联删除 */
        	user.cascade().remove("master");
        
        	/* 查找记录 */
       		List<Pet> list = pet.findAll();
        	list = pet.find().fetch(1, 5);//分页
        	/* 
         	 * api: find(quey, params)
         	 * query: byF1AndF2LikeAndF3NotLikeOrF4IsNullOrF5IsNotNull 
           	 * -> f1 = ? and f2 like ? and f3 not like ? or f4 is null or f5 is not null  
         	 */
        	//所有的字段都要填写类属性名而不是表字段名,orm会自动处理
        	Pet xh = pet.find("byNameAndAge", "小黑", 3).first();//条件查询
        	xh = pet.find("name = ? and age = ?", "小黑", 3).first();//同上

        	xh = pet.find("byNameIsNullAndAge", 3).first();//
        	xh = pet.find("name is null and age = ?", 3).first();//同上

        	xh = pet.find("byNameIsNotNullOrAgeLike", "%3").first();//
        	xh = pet.find("name is not null or age like ?", "%3").first();//同上

        	xh = pet.find("byNameNotLikeAndAge", "小%", 3).first();//
        	xh = pet.find("name not like ? and age = ?", "小%", 3).first();//同上
        
        	sh = pet.findById(4);

        	list = pet.find("name like ?", "小%").fetch(1, 6);//条件查询+分页

        	pet.delete("byName", "小黑");//条件删除
        	pet.deleteAll();

        	long count = pet.count();
        	count = pet.count("byAge", 3);    
    	}
    }

== 8. 事务支持 ==
----
    使用 EWeb4J 你可以很轻松的做到事务控制。你大概可以通过以下两种简单的方式来做：
    
    * 使用注解，在 Action 方法上声明事务控制
    public class HelloAction{
    	@Transactional
    	public String doSave(){
        	return "只需要添加一个注解@Transactional";
    	}
    }
    
    * 使用事务模板，即内部匿名类来包含需要事务控制的代码
    API：Transaction.execute(int level, Trans atom, Object... args);
    
    其中 level是事务隔离级别，java.sql.Connection类的一些常量
    Connection.TRANSACTION_READ_UNCOMMITTED 
    Connection.TRANSACTION_READ_COMMITTED
    Connection.TRANSACTION_REPEATABLE_READ
    Connection.TRANSACTION_SERIALIZABLE
    Demo：
    Transaction.execute(Connection.TRANSACTION_READ_COMMITTED, new Trans(){
    	public void run(Object... args) throws Exception{
        	//do some dao operation...
    	}
    });

== 9. 丰富的DAO封装类 ==
----
    统一从DAOFactory进行调用。
    //这个是最强大的DAO, 链式编程。
    DAOFactory.getDAO();
    
    //级联操作就需要用到这个DAO
    DAOFactory.getCascadeDAO();
    
    //以下这些相对上述DAO更加底层。
    DAOFactory.getInsertDAO();
    DAOFactory.getUpdateDAO();
    DAOFactory.getDeleteDAO();
    DAOFactory.getSelectDAO();
    DAOFactory.getDivPageDAO();
    DAOFactory.getSearchDAO();
    
== 10. 多数据源、表关联 ==
----
== 11. 简单的 IOC 容器 ==
----
== 12. 键值对配置文件和国际化 ==
----
    某个message.properties文件：
    welcome=欢迎使用EWeb4J框架！
    version=1.9-SNAPSHOT
    author=赖伟威
    
    然后在start.xml中配置：
    <properties>
    	<file id="Message" path="message.properties" />
    </properties>
    
    最后在代码里这样就能获取到这个文件的Map映射
    Map message = Props.getMap("Message");
    message.get("welcome");
    message.get("version");
    message.get("author");
    
    此外，还提供国际化支持，
    首先在start.xml添加：
    <locales>
    	<locale language="en" country="US" />
    	<locale language="zh" country="CN" />
    </locales>
    
    将 message.properties 重命名为 message_zh_CN.properties 作为简体中文资源
    然后拷贝一份，重命名为 message_en_US.properties 作为美式英语资源，接着把这个文件内容修改为：
    welcome=Welcome to EWeb4J Framework！
    version=1.9-SNAPSHOT
    author=Lai Weiwei
    
    在代码里通过这个来控制调用哪个国际化资源文件：
    Map message = Props.getMap("Message");//注意这里还是不变的，ID依然写这个
    Lang.set(Locale.US);//美国英文
    message.get("welcome");
    Lang.set(Locale.SIMPLIFIED_CHINESE);//简体中文
    message.get("welcome");
    
    如果你开发的是web应用程序，框架会自动根据HTTP请求判断当前的国家信息设置国际化参数。