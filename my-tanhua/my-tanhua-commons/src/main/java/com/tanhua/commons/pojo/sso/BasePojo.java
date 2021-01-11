package com.tanhua.commons.pojo.sso;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

public abstract class BasePojo implements Serializable {

    private static final long serialVersionUID = -4296017160071130964L;

    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

    public BasePojo() {
    }

    public BasePojo(Date created, Date updated) {
        this.created = created;
        this.updated = updated;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "BasePojo{" +
                "created=" + created +
                ", updated=" + updated +
                '}';
    }
}
