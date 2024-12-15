package springbootinit.manager;

import org.redisson.api.*;
import org.springframework.stereotype.Service;
import springbootinit.common.ErrorCode;
import springbootinit.exception.BusinessException;

import javax.annotation.Resource;

/**
 * 专门提供RedisLimiter 限流基础服务
 */
@Service
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;
    /**
     * 限流操作
     * @param key 区分不同的限流器, 比如不同的用户id应该分别统计
     */
    public void doRateLimit(String key){
        //创建一个名称为user_limiter的限流器, 每秒最多访问两次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        //每当一个操作来了之后，请求一个令牌
        boolean conOp = rateLimiter.tryAcquire(1);
        if(!conOp){
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }

    }
}
