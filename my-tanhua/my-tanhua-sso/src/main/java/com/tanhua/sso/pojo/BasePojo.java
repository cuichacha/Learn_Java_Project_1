package com.tanhua.sso.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public abstract class BasePojo {

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date created;

    @TableField(fill = FieldFill.UPDATE)
    private Date updated;
}
