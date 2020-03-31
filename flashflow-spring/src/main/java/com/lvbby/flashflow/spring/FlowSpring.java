
package com.lvbby.flashflow.spring;

import com.lvbby.flashflow.core.FlowContainer;
import com.lvbby.flashflow.core.FlowScript;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.anno.FlowAction;
import com.lvbby.flashflow.core.utils.FlowUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * flow生效
 * 1. 在bpp中进行action的注入，因为Action的初始化在spring的生命周期中难以控制
 * 2.
 * @author dushang.lp
 * @version $Id: FlowSpring.java, v 0.1 2020年03月07日 00:48 dushang.lp Exp $
 */

public class FlowSpring implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanPostProcessor {

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        /** 1.注册action */
        Map<String, IFlowAction> actions = applicationContext.getBeansOfType(IFlowAction.class);
        actions.forEach((beanName, action) -> {
            /** 注册所有的action */
            FlowContainer.registerFlowAction(action.actionId(), action);
            System.out.println("register flowAction:" + action.actionId());
        });
        /** 2.注册script */
        Map<String, FlowScript> bizList = applicationContext.getBeansOfType(FlowScript.class);
        if (bizList != null) {
            bizList.values().forEach(FlowScript::register);
        }
    }

    /***
     * 优先级：
     * 1. IFlowAction#actionId
     * 2. FlowAction
     * 3. spring bean name
     */
    private String getActionName(String actionBeanName, IFlowAction flowAction) {
        String actionName = flowAction.actionId();
        if (FlowUtils.isNotBlank(actionName)) {
            return actionName;
        }
        FlowAction annotationOnBean = applicationContext.findAnnotationOnBean(actionBeanName, FlowAction.class);
        /***
         * 先找BeFlowAction，没有的话使用beanName使用
         */
        return Optional.ofNullable(annotationOnBean).map(FlowAction::id).filter(FlowUtils::isNotBlank).orElse(actionBeanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /** 使用spring的方式来判定类型 */
        try {
            if (applicationContext.isTypeMatch(beanName, IFlowAction.class)) {
                final IFlowAction iFlowAction = (IFlowAction) bean;

                /** 代理bean的actionName方法，返回beanName */
                if (FlowUtils.isBlank(iFlowAction.actionId())) {
                    return createProxy(iFlowAction, invocation -> {
                        Method method = invocation.getMethod();
                        if (method.getName().equals("actionId")) {
                            return getActionName(beanName, iFlowAction);
                        }
                        return invocation.proceed();
                    });
                }
                return bean;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return bean;
    }

    private Object createProxy(Object iFlowAction, MethodInterceptor interceptor) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(iFlowAction);
        proxyFactoryBean.setProxyTargetClass(true);
        proxyFactoryBean.addAdvice(interceptor);
        return proxyFactoryBean.getObject();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}