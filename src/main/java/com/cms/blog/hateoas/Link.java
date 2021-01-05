package com.cms.blog.hateoas;

import javax.ws.rs.core.UriInfo;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class Link {

    private Href first  = new Href();
    private Href prev = new Href();
    private Href last = new Href();
    private Href self = new Href();


    public static <T> Link of(UriInfo uriInfo,PanacheQuery<T> pquery,Class<T> type){
        Link link = new Link();
        link.getFirst().setHref(uriInfo.getAbsolutePath().toString()+"?page=0&size="+pquery.page().size);
        link.getPrev().setHref(uriInfo.getAbsolutePath().toString()+"?page=0&size="+pquery.page().size);
        link.getLast().setHref(uriInfo.getAbsolutePath().toString()+"?page=0&size="+pquery.page().size);
        link.getSelf().setHref(uriInfo.getAbsolutePath().toString());
        return link;
    }

    public Href getFirst() {
        return first;
    }

    public void setFirst(Href first) {
        this.first = first;
    }

    public Href getPrev() {
        return prev;
    }

    public void setPrev(Href prev) {
        this.prev = prev;
    }

    public Href getLast() {
        return last;
    }

    public void setLast(Href last) {
        this.last = last;
    }

    public Href getSelf() {
        return self;
    }

    public void setSelf(Href self) {
        this.self = self;
    }
    
}



