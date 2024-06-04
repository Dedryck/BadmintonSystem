package com.coding24.badmintonsystem_1.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Court {
//    @NotNull(message = "场地ID不能为空")
    private Integer courtID;

    @NotBlank(message = "位置不能为空")
    @Size(max = 50, message = "位置长度不能超过50个字符")
    private String location;

    @NotBlank(message = "状态不能为空")
    @Size(max = 20, message = "状态长度不能超过20个字符")
    private String status;
}
