package com.travel.utils;

import java.util.List;

/**
 * 采用mvc思想,对响应的数据进行封装
 * M : 数据封装,简单的业务处理
 * @param <T>
 */
public class PageBean<T> {
    // 当前页的数据信息
    private List<T> data;
    private int pageNumber; // 当前页页码
    private int totalPage; // 总页数
    private int totalCount; // 总条数
    private int pageSize; // 每页显示条数
    //--------------
    private Integer start;  // 从哪里开始展示
    private Integer end;    // 到哪里结束


    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public PageBean(int pageNumber,int pageSize,int totalCount) {
        this.pageNumber = pageNumber;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
    }
    // 计算当前页的起始索引
    public int getStartIndex(){
        return (pageNumber-1) * pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    // 获取总页数,计算总页数
    public int getTotalPage() {
        return (int)Math.ceil(totalCount*1.0/pageSize);
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
