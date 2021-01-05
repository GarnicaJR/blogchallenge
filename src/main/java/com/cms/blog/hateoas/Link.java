package com.cms.blog.hateoas;

import javax.ws.rs.core.UriInfo;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.HashMap;
import java.util.Map;

public class Link {

    public static <T> Map<String, Href> of(UriInfo request, PanacheQuery<T> pquery, Class<T> type) {

        Map<String, Href> links = new HashMap<>();
        String fullPath = request.getAbsolutePath().toString();

        links.put("first", new Href(fullPath + "?page=0" + "&size=" + pquery.page().size));

        int indexPage = pquery.page().index;
        if (indexPage > 0) {
            links.put("prev", new Href(fullPath + "?page=" + (indexPage - 1) + "&size=" + pquery.page().size));
        }

        if (indexPage < (pquery.pageCount() - 1)) {
            links.put("next", new Href(fullPath + "?page=" + (indexPage + 1) + "&size=" + pquery.page().size));
        }

        links.put("last", new Href(fullPath + "?page=" + (pquery.pageCount() - 1) + "&size=" + pquery.page().size));

        if (request.getRequestUri().getQuery() != null) {
            links.put("self", new Href(request.getAbsolutePath().toString() + "?" + request.getRequestUri().getQuery()));
        } else {
            links.put("self", new Href(fullPath + "?page=0" + "&size=" + pquery.page().size));
        }
        return links;
    }
}



