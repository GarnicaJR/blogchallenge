package com.cms.blog.hateoas;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class Page {

    private long size;
    private long totalElements;
    private long totalPages;
    private long number;


    public static <T> Page of(PanacheQuery<T> pquery,Class<T> type){
        Page page = new Page();
        page.setSize(pquery.page().size);
        page.setTotalElements(pquery.count());
        page.setTotalPages(pquery.pageCount());
        page.setNumber(pquery.page().index);
        return page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
