package com.example.client_zeebe.common.ruleService;

import com.example.client_zeebe.common.drools.entity.BusinessRule;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DroolsService {

    private final Logger log = LoggerFactory.getLogger(DroolsService.class);
    private final RuleService ruleService;
    private final String GROUP_ID = "com.example.demo";
    private final String ARTIFACT_ID = "dynamic-rules";
    private final AtomicLong VERSION_COUNTER = new AtomicLong(1); // For dynamic versioning

    public DroolsService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    public KieContainer createTempContainer(List<BusinessRule> rules, String entityName) {

        String KMODULE_XML = String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <kmodule xmlns="http://www.drools.org/xsd/kmodule">
                    <kbase name="validate" packages="rules" default="true">
                        <ksession name="%s" default="true" type="stateless"/>
                    </kbase>
                </kmodule>
                """, entityName);
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/META-INF/kmodule.xml",
                ks.getResources().newReaderResource(new java.io.StringReader(KMODULE_XML)));
        String newVersion = VERSION_COUNTER.getAndIncrement() + ".0-SNAPSHOT";
        ReleaseId releaseId = ks.newReleaseId(GROUP_ID, ARTIFACT_ID, newVersion);
        kfs.generateAndWritePomXML(releaseId); // Generate a pom.xml for the in-memory kjar
        for (BusinessRule r : rules) {
            log.info("Rule '{}'", r.getRuleContent());
            String sb = "src/main/resources/rules/" + r.getRuleName() + ".drl";
            kfs.write(sb, ks.getResources().newReaderResource(new StringReader(r.getRuleContent()))
                    .setResourceType(ResourceType.DRL));
        }
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Error compiling dynamic rules: " + kieBuilder.getResults().toString());
        }
        KieModule km = kieBuilder.getKieModule();
        ks.getRepository().addKieModule(km);
        return ks.newKieContainer(releaseId);
    }

}
