package com.zdx.model.vo;

import com.zdx.entity.zdx.Navigation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "导航")
public class NavigationVo {
    @Schema(description = "名称")
    private String name;

    @Schema(description = "导航数据")
    private List<Navigation> list;
}
