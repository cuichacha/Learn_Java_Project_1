package com.tanhua.commons.vo.recommend;

import java.util.Collections;
import java.util.List;

public class RecommendUsers {

    private Integer counts = 0;//总记录数
    private Integer pagesize = 0;//页大小
    private Integer pages = 0;//总页数
    private Integer page = 0;//当前页码
    private List<?> items = Collections.emptyList(); //列表

    public RecommendUsers() {
    }

    public RecommendUsers(Integer counts, Integer pagesize, Integer pages, Integer page, List<?> items) {
        this.counts = counts;
        this.pagesize = pagesize;
        this.pages = pages;
        this.page = page;
        this.items = items;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "RecommendUsers{" +
                "counts=" + counts +
                ", pagesize=" + pagesize +
                ", pages=" + pages +
                ", page=" + page +
                ", items=" + items +
                '}';
    }
}
