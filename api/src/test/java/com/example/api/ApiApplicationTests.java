package com.example.api;

import com.example.api.repository.CouponRepository;
import com.example.api.service.ApplyService;
import org.apache.catalina.Executor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApiApplicationTests {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository countRepository;

    @Test
    public void 한번만응모()
    {
        applyService.apply(1L);

        long count = countRepository.count();

        assertThat(count).isEqualTo(1);

    }

    @Test
    public void 여러명응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++)
        {
            long userId = i;
            executorService.submit(() ->
            {
                try {
                    applyService.apply(userId);
                }
                finally {
                    latch.countDown();
                }

            });
        }

        latch.await();
        Thread.sleep(10000);
        long count = countRepository.count();

        assertThat(count).isEqualTo(100);
    }

    @Test
    public void 한명당_한개쿠폰발급() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++)
        {
            long userId = i;
            executorService.submit(() ->
            {
                try {
                    applyService.apply(1L);
                }
                finally {
                    latch.countDown();
                }

            });
        }

        latch.await();
        Thread.sleep(10000);
        long count = countRepository.count();

        assertThat(count).isEqualTo(1);
    }

}
