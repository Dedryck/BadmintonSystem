package com.coding24.badmintonsystem_1.entity;

import com.coding24.badmintonsystem_1.validation.NotPast;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Booking {
    private Integer bookingID;//唯一标识符

    @NotNull(message = "用户ID不能为空")
    private Integer userID;//唯一标识符

    @NotNull(message = "场地ID不能为空")
    private Integer courtID;//唯一标识符

    @NotNull(message = "开始时间不能为空")
    @NotPast(message = "开始时间不能为过去时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @NotPast(message = "结束时间不能为过去时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date endTime;

    @NotNull(message = "预订日期不能为空")
    private Date bookingDate;

    @NotNull(message = "状态不能为空")
    private String status;
}
