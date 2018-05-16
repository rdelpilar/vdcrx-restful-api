package com.vdcrx.rest.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 * Application context provider
 *
 * Application context initialization
 *
 * @author Ranel del Pilar
 */

public class ApplicationContextProvider implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
