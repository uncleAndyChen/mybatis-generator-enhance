package mybatis.generator.enhance;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
