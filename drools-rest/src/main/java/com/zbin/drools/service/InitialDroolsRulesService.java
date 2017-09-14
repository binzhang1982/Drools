package com.zbin.drools.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.zbin.drools.entity.CoachInfo;
import com.zbin.drools.entity.CoachInfoExample;
import com.zbin.drools.mapper.CoachInfoMapper;
import com.zbin.drools.utils.KieUtils;

@Service
public class InitialDroolsRulesService {

    private static final String RULES_PATH = "rules/";
    
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public CoachInfoMapper coachInfoMapper;
    
    @PostConstruct  
    public void init() throws IOException {
		CoachInfoExample coachExam = new CoachInfoExample();
		List<CoachInfo> coachs = coachInfoMapper.selectByExample(coachExam);
		
		for (CoachInfo coach : coachs) {
			System.out.println("PostConstruct:" + coach.getPhonenum());
		}
		
		loadRules();
		
		System.out.println("PostConstruct KieContainer is null:" + String.valueOf(KieUtils.getKieContainer() == null));
		
    }

    public void loadRules() throws IOException {
        KieServices kieServices = getKieServices();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        //全部文件的导入。。。
        for (Resource file : getRuleFiles()) {
            kfs.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
        }
        
//        kfs.write("src/main/resources/rules/temp1.drl", loadRules());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieUtils.setKieContainer(kieServices.newKieContainer(getKieServices().getRepository().getDefaultReleaseId()));
        System.out.println("新规则加载成功");
    }
    
    private Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
    }
    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

}
