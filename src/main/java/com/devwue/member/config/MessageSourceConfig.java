package com.devwue.member.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MessageSourceConfig implements WebMvcConfigurer {
    @Bean
    public MessageSource messageSource(@Value("${spring.messages.exception}") String basename,
                                       @Value("${spring.messages.encoding}") String encoding,
                                       @Value("${spring.messages.fallback-to-system-locale}") String fallbackSystemLocale) {
        YamlMessageSource messageSource = new YamlMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setFallbackToSystemLocale(fallbackSystemLocale.equals("true"));
        return messageSource;
    }

    public MessageSource validationMessageSource(String basename, String encoding, String fallbackSystemLocale) {
        YamlMessageSource messageSource = new YamlMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setFallbackToSystemLocale(fallbackSystemLocale.equals("true"));
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator(@Value("${spring.messages.basename}") String basename,
                                                  @Value("${spring.messages.encoding}") String encoding,
                                                  @Value("${spring.messages.fallback-to-system-locale}") String fallbackSystemLocale) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(validationMessageSource(basename, encoding, fallbackSystemLocale));
        return factoryBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    private LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }

}
