package com.devwue.member.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(@Value("${spring.life-cycle.timeout-per-shutdown-phase}") String timeWait) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setThreadNamePrefix("thread-pool-");
        // for graceful shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(Integer.parseInt(timeWait.replaceAll("\\D+", "")));

        executor.initialize();
        return executor;
    }
}
