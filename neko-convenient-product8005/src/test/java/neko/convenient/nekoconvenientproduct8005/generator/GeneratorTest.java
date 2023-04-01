package neko.convenient.nekoconvenientproduct8005.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;

public class GeneratorTest {
    @Test
    public void generator(){
        String classOutputPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java";
        String xmlOutputPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" +
                File.separator + "mybatis" + File.separator + "mapper";

        FastAutoGenerator.create("jdbc:mysql://192.168.30.131:3308/neko_convenient_product?" +
                "characterEncoding=utf8&useSSL=false&rewriteBatchedStatements=true&autoReConnect=true",
                "root",
                "123456")
                .globalConfig(builder -> {
                    builder.author("NEKO") // 设置作者
                            .disableOpenDir()
                            .outputDir(classOutputPath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("neko.convenient.nekoconvenientproduct8005") // 设置父包名
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputPath)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    //开启大写命名
                    builder.enableCapitalMode();
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            .enableLombok()
                            .enableChainModel();
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder()
                            //开启生成@RestController 控制器
                            .enableRestStyle()
                            .formatFileName("%sController");
                })
                .strategyConfig(builder -> {
                    builder.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                .strategyConfig(builder -> {
                    builder.mapperBuilder()
                            .mapperAnnotation(Mapper.class)
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper.xml");
                })
                .execute();
    }
}
