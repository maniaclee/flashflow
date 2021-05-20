/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.common.module;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.lvbby.flashflow.core.model.FlowContext;
import com.lvbby.flashflow.core.module.FlowModule;
import com.lvbby.flashflow.core.util.FlowCommonUtils;
import com.lvbby.flashflow.core.util.FlowUtils;
import lombok.Data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author dushang.lp
 * @version : IoReadModule.java, v 0.1 2021年05月16日 下午8:13 dushang.lp Exp $
 */
public class IoReadModule implements FlowModule {
    @Override
    public void exec(FlowContext context) throws Exception {
        IoReadModuleConfig config = FlowUtils.getNodeProps(context, IoReadModuleConfig.class);

        Charset charset = Charset.forName(config.charset);
        if (FlowCommonUtils.isNotEmpty(config.file)) {
            for (String file : config.file) {
                String[] strings = parseVar(config, file);
                String var = strings[0];
                String fileName = strings[1];
                Path path = Paths.get(fileName);
                List<String> lines = Files.readAllLines(path, charset);
                processContent(context,config,var,lines);
            }
        }
        if (FlowCommonUtils.isNotEmpty(config.classPathFile)) {
            for (String file : config.classPathFile) {
                String[] strings = parseVar(config, file);
                String var = strings[0];
                String fileName = strings[1];

                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                List<String> lines = Lists.newLinkedList();
                String line = null;
                while((line = bufferedReader.readLine())!=null) {
                    lines.add(line);
                }
                processContent(context,config,var,lines);
            }
        }
    }

    private String[]  parseVar(IoReadModuleConfig config , String file){
        return file.split("=");
    }
    private void processContent(FlowContext context ,IoReadModuleConfig config, String var,List<String> lines){
        if(config.mergeLines){
            context.put(var, Joiner.on(config.lineSeparator).join(lines));
            return;
        }
        context.put(var, lines);
    }

    @Data
    public static class IoReadModuleConfig {
        private List<String> file;
        private List<String> classPathFile;

        private String charset = "utf-8";

        private boolean mergeLines = false;
        private String lineSeparator = "\n";

    }

}