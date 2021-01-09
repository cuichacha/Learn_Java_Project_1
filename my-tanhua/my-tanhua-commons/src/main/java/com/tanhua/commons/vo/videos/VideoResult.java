package com.tanhua.commons.vo.videos;

import java.util.List;

public class VideoResult {
    private Integer counts;//总记录数
    private Integer pageSize;//页大小
    private Integer pages;//总页数
    private Integer page;//当前页码
    private List<?> items; //列表

    public VideoResult() {
    }

    public VideoResult(Integer counts, Integer pageSize, Integer pages, Integer page, List<?> items) {
        this.counts = counts;
        this.pageSize = pageSize;
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

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
        return "VideoResult{" +
                "counts=" + counts +
                ", pageSize=" + pageSize +
                ", pages=" + pages +
                ", page=" + page +
                ", items=" + items +
                '}';
    }
}
