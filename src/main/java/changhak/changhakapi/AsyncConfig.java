package changhak.changhakapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // 스레드 풀 크기 조정
        executor.setMaxPoolSize(20); // 최대 스레드 수 조정
        executor.setQueueCapacity(1000); // 큐 용량 조정
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
