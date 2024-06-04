package com.coding24.badmintonsystem_1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

public class NotPastValidator implements ConstraintValidator<NotPast, Date> {

    @Override
    public void initialize(NotPast constraintAnnotation) {
        // 初始化
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // 由@NotNull注解处理空值
        }
        // 获取当前日期，不包括时间部分
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        // 验证日期必须是今天或以后
        return !date.before(today);
    }
}
