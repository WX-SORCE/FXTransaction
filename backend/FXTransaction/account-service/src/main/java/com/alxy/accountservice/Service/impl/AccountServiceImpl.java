package com.alxy.accountservice.Service.impl;

import com.alxy.accountservice.DTO.Result;
import com.alxy.accountservice.Entity.Account;
import com.alxy.accountservice.Reposity.AccountRepository;
import com.alxy.accountservice.Service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public Result<Account> getAccountById(String accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        return accountOptional.map(Result::success).orElseGet(() -> Result.error(404, "未找到该账户信息"));
    }

    @Override
    public Result<?> getAccountByUserId(String userId) {
        Optional<Account> accountOptional = accountRepository.findByUserId(userId);
        return accountOptional.map(Result::success).orElseGet(() -> Result.error(404, "未找到该用户对应的账户信息"));
    }

    // 充值方法
    @Override
    public Result<Account> recharge(String baseCurrency, String userId, BigDecimal amount) {
        Account account = accountRepository.findAccountByUserIdAndBaseCurrency(userId, baseCurrency)
                .orElseThrow(() -> new RuntimeException("账户不存在"));
        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(new Date());
        accountRepository.save(account);
        return Result.success(account);
    }

    // 消费方法
    @Override
    public Result<Account> consume(String userId, BigDecimal amount) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("账户不存在"));

        if (account.getBalance().compareTo(amount) < 0) {
            return Result.error("余额不足");
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(new Date());
        accountRepository.save(account);

        return Result.success(account);
    }

    @Override
    public Account getAccountByUserIdAndBaseCurrency(String userId, String baseCurrency) {
        return accountRepository.findAccountByUserIdAndBaseCurrency(userId, baseCurrency)
                .orElseThrow(() -> new RuntimeException("账户不存在"));
    }

    @Override
    public Boolean updateAccount(Account baseAccount) {
        try {
            accountRepository.save(baseAccount);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}