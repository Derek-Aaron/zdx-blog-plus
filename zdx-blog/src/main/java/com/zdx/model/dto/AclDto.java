package com.zdx.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "权限")
public class AclDto {

    @NotEmpty
    @Schema(description = "主体")
    private List<String> subjects;

    @NotBlank
    @Schema(description = "类型")
    private String type;

    @NotEmpty
    @Schema(description = "资源")
    private List<String> resources;
}
