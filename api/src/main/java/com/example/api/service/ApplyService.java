package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService
{
    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository)
    {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }

    public void apply(Long userId)
    {
        /*// redis incr -> key의 value를 1씩 증가
        // redis 는 싱글쓰레드로 동작
        long count = couponRepository.count();*/

        Long count = couponCountRepository.increment();

        if (count > 100)
        {
            return;
        }

        couponRepository.save(new Coupon(userId));
    }
}
