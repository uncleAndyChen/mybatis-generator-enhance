# MyBatis Generator（MBG），写扩展类，以适应 MySQL 大小写敏感配置的各种情况、适应分表时动态替换表名
## 本扩展的作用
给表名添加MySQL“边界”，用 \`（左上角数字键1左边、Tab键上边、Esc键下边的键）引起来。目的是分表时进行表名替换，把每张表的表名当作一个整体，避免替换 `sys_user` 时 把 `sys_user_role` 中的 `sys_user` 也替换掉。

注：项目根目录下有更详细的介绍，点击以下链接查看：
- gitee:  https://gitee.com/uncleAndyChen/mybatis-generator-enhance
- github: https://github.com/uncleAndyChen/mybatis-generator-enhance

## 自己扩展的好处
1. 比起直接修改MBG源代码，这种方式对MBG无代码侵入，方便将来升级MBG。
1. 符合面向对象设计的【开闭原则】，即通过增加代码来为软件添加新功能，而不是直接修改原有代码。这一点，MBG做得非常好，除了可以非常方便的扩展之外，还可以写各种插件以实现自己的业务需要。

## 使用自己的扩展类
在配置文件 `generatorConfig.xml` 的 `context` 节点，配置 `targetRuntime` 值，指向自己的扩展类，需要带包名，如本例：
```xml
<context id="Mysql" targetRuntime="mybatis.generator.enhance.IntrospectedTableEnhanceImpl" defaultModelType="flat">
```

扩展类的代码很简单，只有十几行
```java
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

public class IntrospectedTableEnhanceImpl extends IntrospectedTableMyBatis3Impl {
    @Override
    public String getFullyQualifiedTableNameAtRuntime() {
        return getTableNameFromConfigFile();
    }

    @Override
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        return getTableNameFromConfigFile();
    }

    private String getTableNameFromConfigFile() {
        String tableName = this.getTableConfiguration().getTableName();
        return "`" + tableName + "`";
    }
}
```

## 两种运行方式
### 以程序方式运行
仿照官方的`org.mybatis.generator.api.ShellRunner`，写一段代码，以程序的方式运行。本工程的代码如下：
```java
public class GeneratorEnhanceRun {
   public static void main(String[] args) {
      try {
         List<String> warnings = new ArrayList<>();
         // 初始化配置解析器
         ConfigurationParser configurationParser = new ConfigurationParser(warnings);
         // 获取配置文件信息
         InputStream inputStream = GeneratorEnhanceRun.class.getResourceAsStream("/generatorConfig.xml");
         // 调用配置解析器创建配置对象
         Configuration config = configurationParser.parseConfiguration(inputStream);
         // 调用配置解析器创建配置对象
         DefaultShellCallback callback = new DefaultShellCallback(true);
         // 创建一个MyBatisGenerator对象。MyBatisGenerator类是真正用来执行生成动作的类
         MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
         ProgressCallback progressCallback = new VerboseProgressCallback();
         // 执行生成操作
         myBatisGenerator.generate(progressCallback);

         // 输出警告信息
         if (!warnings.isEmpty()) {
            System.out.println("--------------- warnings start ---------------");

            for (String warning : warnings) {
               System.out.println(warning);
            }

            System.out.println("--------------- warnings end  ---------------");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
```

本工程，直接运行`GeneratorEnhanceRun`下的`main`方法，在运行之前需要先配置好 generatorConfig.xml，并放到 resources 目录下面

### cmd窗口运行jar文件
- 下载 MBG 的jar包，[官方 github releases 页](https://github.com/mybatis/generator/releases)， [1.4.2 的 release 版本](https://github.com/mybatis/generator/releases/download/mybatis-generator-1.4.2/mybatis-generator-core-1.4.2-bundle.zip)，解压，找到`mybatis-generator-1.4.2.jar`，备用。
- 执行项目根目录下的 `package.bat`，生成的 jar 文件：`mybatis-generator-enhance\target\mybatis-generator-enhance-0.0.1.jar`，会用到
```shell
java -Dfile.encoding=UTF-8 -cp mybatis-generator-core-1.4.2.jar;mybatis-generator-enhance\target\mybatis-generator-enhance-0.0.1.jar;mysql-connector-j-8.0.33.jar org.mybatis.generator.api.ShellRunner -configfile mybatis-generator-enhance\src\main\resources\generatorConfig.xml -overwrite -verbose

# 其中，-verbose 是打印执行过程
```
> 这里通过 -cp 指定需要用到的所有jar包，用分号隔开，这样在运行的时候才能找到相应的类。

#### 把相关依赖打到一起，用一个 jar 文件执行即可
在项目 mybatis.generator.enhance 的 pom.xml 添加打包插件，如下：
```xml
<plugin>
   <!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-assembly-plugin -->
   <groupId>org.apache.maven.plugins</groupId>
   <artifactId>maven-assembly-plugin</artifactId>
   <version>3.7.1</version>
   <configuration>
      <!-- 通过此标签可覆盖默认命名规则，直接指定输出文件名 -->
      <finalName>mybatis-generator-enhance-with-dependencies</finalName>
      <!-- 设置为 false 可消除文件名中的分类器部分（如-jar-with-dependencies） -->
      <appendAssemblyId>false</appendAssemblyId>
      <!-- 此配置可指定打包文件的输出路径，下面的配置，会将 jar 输出到项目根目录 -->
      <outputDirectory>${project.build.directory}/../../</outputDirectory>
      <archive>
         <manifest>
            <mainClass>mybatis.generator.enhance.IntrospectedTableEnhanceImpl</mainClass>
         </manifest>
      </archive>
      <descriptorRefs>
         <descriptorRef>jar-with-dependencies</descriptorRef>
      </descriptorRefs>
   </configuration>
   <executions>
      <execution>
         <phase>package</phase>
         <goals>
            <goal>single</goal>
         </goals>
      </execution>
   </executions>
</plugin>
```
通过包含相关依赖的 jar 包执行
```shell
java -Dfile.encoding=UTF-8 -cp mybatis-generator-enhance-with-dependencies.jar org.mybatis.generator.api.ShellRunner -configfile mybatis-generator-enhance\src\main\resources\generatorConfig.xml -overwrite -verbose

```

## 原理
简单的说，就是自己的实现类`IntrospectedTableEnhanceImpl`继承自MBG的一个具体实现类，重写获取表名的方法。

IntrospectedTable是MBG提供的一个比较基础的扩展类，相当于可以重新定义一个runtime。如果要通过继承IntrospectedTable完成扩展，需要自己来实现生成XML和Java代码的所有代码，也可以直接继承IntrospectedTableMyBatis3Impl，重写自己需要的业务逻辑，本模块就是直接继承自该类。

要扩展自己的业务逻辑，建议先仔细阅读IntrospectedTableMyBatis3Impl和IntrospectedTableMyBatis3SimpleImpl源码，这两个类用得多一些。

在MBG中，提供了几种默认的IntrospectedTable的实现，其实在context上设置的runtime对应的就是不同的IntrospectedTable的实现，下面是几种runtime和对应的实现类：
- MyBatis3 (default)：org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl
- MyBatis3Simple：org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl
- Ibatis2Java2：org.mybatis.generator.codegen.ibatis2.IntrospectedTableIbatis2Java2Impl
- Ibatis2Java5：org.mybatis.generator.codegen.ibatis2.IntrospectedTableIbatis2Java5Impl

## MySQL 数据库驱动版本与数据库版本问题
用高版本的数据库驱动`mysql-connector-java 8.0.13`连接低版本数据库`MySQL 5.7.23`，会有以下问题：
1. 报错
    ```
    Cannot obtain primary key information from the database, generated objects may be incomplete
    ...
    ```
1. 生成的 mapper 缺少以下接口：
    ```
    deleteByPrimaryKey
    selectByPrimaryKey
    updateByPrimaryKeySelective
    updateByPrimaryKey
    ```

### 解决
数据库驱动与数据库版本匹配即可，作者在以下两个版本（5.x与8.x）测试通过：
1. 数据库驱动`mysql-connector-java 8.0.13`连接数据库`MySQL 8.0.11`，对应`driverClassName: com.mysql.cj.jdbc.Driver`
1. 数据库驱动`mysql-connector-java 5.1.29`连接数据库`MySQL 5.7.23`，对应`driverClassName: com.mysql.jdbc.Driver`

本工程的数据库驱动版本，使用的是当前（25-09-22）最新的`mysql-connector-java 8.0.33`，仅支持连接高版本MySQL 8.x。

如果要在`MySQL 5.7.x`下运行，只需要修改以下两个地方（注意是5.7.x，其它5.x版本没测试）：
1. 修改pom.xml中`mysql-connector.version`，改为低版本`5.1.39`。
1. 本项目的执行，依赖 resources 目录下的`generatorConfig.xml`文件，将其中`driverClass`，由`com.mysql.cj.jdbc.Driver`改为`com.mysql.jdbc.Driver`。
