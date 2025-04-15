package com.alxy.accountservice.Controller;

import com.alxy.accountservice.DTO.Result;
import com.alxy.accountservice.DTO.TradeVo;
import com.alxy.accountservice.Entity.Account;
import com.alxy.accountservice.Service.AccountService;
import com.alxy.accountservice.Utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    @Resource
    private AccountService accountService;
    @Resource
    private FlaskFeign flaskFeign;

    /**
     * 根据账户 ID 获取账户信息
     *
     * @param accountId 账户 ID
     * @return 包含账户信息的通用返回对象
     */
    @GetMapping("/getAccountById")
    public Result<Account> getAccountById(@RequestParam String accountId) {
        return accountService.getAccountById(accountId);
    }


    // 挂单
    @PostMapping("/")
    public Result<Account> guadan(@RequestBody Account account) {
        return accountService.getAccountById(account.getAccountId());
    }

    // 市价单
    @PostMapping("/Buy")
    public Result<?> updateBalance(@RequestBody TradeVo tradeVo) {
        // 卖出 - 从Transaction中获取
        if(tradeVo.getTargetCurrency().equals(tradeVo.getBaseCurrency())) {
            Account account = accountService.getAccountByUserIdAndBaseCurrency(tradeVo.getUserId(), tradeVo.getBaseCurrency());
            account.setBalance(account.getBalance().add(tradeVo.getBaseBalance()));
        }
        // 查找基础币种账户
        Account baseAccount = accountService.getAccountByUserIdAndBaseCurrency(tradeVo.getUserId(), tradeVo.getBaseCurrency());
        if (baseAccount == null) {
            return Result.error("基础币种账户不存在");
        }
        // 判断余额是否足够
        if (baseAccount.getBalance().compareTo(tradeVo.getBaseBalance()) < 0) {
            return Result.error("基础币种账户余额不足");
        }
        // 查找目标币种账户
        Account targetAccount = accountService.getAccountByUserIdAndBaseCurrency(tradeVo.getUserId(), tradeVo.getTargetCurrency());
        if (targetAccount == null) {
            return Result.error("目标币种账户不存在");
        }
        // 扣减余额
        baseAccount.setBalance(baseAccount.getBalance().subtract(tradeVo.getBaseBalance()));
        boolean baseUpdateSuccess = accountService.updateAccount(baseAccount);
        if (!baseUpdateSuccess) {
            return Result.error("基础币种账户余额更新失败");
        }
        // 增加目标币种账户余额
        targetAccount.setBalance(targetAccount.getBalance().add(tradeVo.getTargetBalance()));
        boolean targetUpdateSuccess = accountService.updateAccount(targetAccount);
        if (!targetUpdateSuccess) {
            return Result.error("目标币种账户余额更新失败");
        }
        // 如果都成功
        return Result.success();
    }



    // 获取交易token
    @PostMapping("getFaceToken")
    Result<?> getTokenByFace(@RequestParam("image") MultipartFile file) {
        return flaskFeign.getTokenByFace(file);
    }

    // token验证 -- 充值
    @PostMapping("/recharge")
    public Result<Account> recharge(@RequestParam String baseCurrency, @RequestParam BigDecimal amount, @RequestParam String faceToken) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = claims.get("username").toString();
        String userId = claims.get("userId").toString();
        try {
            // 调用人脸 token 验证
            boolean tokenResult = flaskFeign.validateFaceToken(username, faceToken);
            if (!tokenResult) {
                return Result.error(401, "人脸Token验证失败：");
            }
            // 验证通过，执行充值
            return accountService.recharge(baseCurrency, userId, amount);
        } catch (Exception e) {
            return Result.error(500, "充值异常：" + e.getMessage());
        }
    }


    @PostMapping("/consume")
    public Result<Account> consume(@RequestParam String userId, @RequestParam BigDecimal amount) {
        try {
            return accountService.consume(userId, amount);
        } catch (Exception e) {
            return Result.error(500, "消费异常：" + e.getMessage());
        }
    }


    /**
     * 根据用户 ID 获取账户信息
     *
     * @param userId 用户 ID
     * @return 包含账户信息的通用返回对象
     */
    @GetMapping("getAccountByUserId")
    public Result<?> getAccountByUserId(@RequestParam String userId) {
        return accountService.getAccountByUserId(userId);
    }
}