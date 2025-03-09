package com.atsoft.jira.plugin.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atsoft.jira.plugin.api.MyPluginComponent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({ MyPluginComponent.class })
@Named("myPluginComponent")
@Slf4j
public class MyPluginComponentImpl implements MyPluginComponent {
    @ComponentImport
    private final ApplicationProperties applicationProperties;
    private final Gson gson = new Gson();

    @Inject
    public MyPluginComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "myComponent:" + applicationProperties.getDisplayName();
        }

        JsonObject jsonObject = gson.fromJson("{\"name\":\"woo.yong\"}", JsonObject.class);
        log.info("jsonObject: {}", jsonObject);


        return "myComponent";
    }
}