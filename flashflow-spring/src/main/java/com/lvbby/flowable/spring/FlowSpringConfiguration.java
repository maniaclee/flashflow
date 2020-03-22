
package com.lvbby.flowable.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * flow在spring中的生效配置
 * @author dushang.lp
 * @version $Id: FlowSpringConfiguration.java, v 0.1 2020年03月07日 19:58 dushang.lp Exp $
 */
@Configuration
public class FlowSpringConfiguration {
    @Bean
    public FlowSpring flow() {
        return new FlowSpring();
    }
    @Bean
    public FlowAop flowAop() {
        return new FlowAop();
    }

}