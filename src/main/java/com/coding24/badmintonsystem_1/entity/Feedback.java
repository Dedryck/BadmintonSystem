package com.coding24.badmintonsystem_1.entity;

import com.coding24.badmintonsystem_1.validation.NotPast;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class Feedback {
    private Integer feedbackID;

    @NotNull(message = "用户ID不能为空")
    private Integer userID;

    @NotBlank(message = "评论不能为空")
    @Size(max = 500, message = "评论长度不能超过500字符")
    private String comments;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能低于1")
    @Max(value = 5, message = "评分不能高于5")
    private Integer rating;

    @NotNull(message = "反馈日期不能为空")
    @NotPast(message = "反馈日期不能为过去时间")
    private Date feedbackDate;

    @NotBlank(message = "用户名不能为空")
    private String username;
}
