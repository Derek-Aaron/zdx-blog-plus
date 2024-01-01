package com.zdx.model.vo;

import com.zdx.entity.zdx.Navigation;
import lombok.Data;

import java.util.List;

@Data
public class NavigationVo {
    private String name;
    private List<Navigation> list;
}
