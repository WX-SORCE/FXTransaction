// 自定义注解 @RoleCheck
package com.hsbc.pws.risk.filter;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleCheck {
    String value();
}