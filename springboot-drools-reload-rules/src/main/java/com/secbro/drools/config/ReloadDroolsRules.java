package com.secbro.drools.config;

import com.secbro.drools.utils.KieUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.internal.io.ResourceFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by neo on 17/7/31.
 */

@Service
public class ReloadDroolsRules {

    private static final String RULES_PATH = "rules/";
    
    public void reload() throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        //全部文件的导入。。。
        for (Resource file : getRuleFiles()) {
            kfs.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
        }
        
        kfs.write("src/main/resources/rules/temp1.drl", loadRules());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieUtils.setKieContainer(kieServices.newKieContainer(getKieServices().getRepository().getDefaultReleaseId()));
        System.out.println("新规则重载成功");
    }
    
    private Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
    }
    
    private String loadRules() {
        // 从数据库加载的规则
        return "package plausibcheck.adress\n\n"
        		+ " import com.secbro.drools.model.Address;\n"
        		+ " import com.secbro.drools.model.fact.AddressCheckResult;\n\n"
        		+ " rule \"Postcode 6 numbers 1\"\n\n   "
        		+ " when\n "
        		+ " then\n       "
        		+ " System.out.println(\"规则2中打印日志：校验通过!\");\n"
        		+ " end";
    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

}
