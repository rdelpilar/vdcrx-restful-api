package com.vdcrx.rest.api.v1.hateoas.listener;

import com.vdcrx.rest.api.v1.hateoas.events.PaginatedResultsRetrievedEvent;
import com.vdcrx.rest.utils.LinkUtil;
import io.jsonwebtoken.lang.Assert;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({ "rawtypes" })
@Component
public class PaginatedResultsRetrievedDiscoverabilityListener implements ApplicationListener<PaginatedResultsRetrievedEvent> {
    private static final String PAGE = "page";

    public PaginatedResultsRetrievedDiscoverabilityListener() {
        super();
    }

    @Override
    public final void onApplicationEvent(final PaginatedResultsRetrievedEvent ev) {
        Assert.notNull(ev);
        addLinkHeaderOnPagedResourceRetrieval(
                ev.getUriBuilder(),
                ev.getRequest(),
                ev.getResponse(),
                ev.getClazz(),
                ev.getPage(),
                ev.getTotalPages(),
                ev.getPageSize());
    }

    private void addLinkHeaderOnPagedResourceRetrieval(final UriComponentsBuilder uriBuilder,
                                                       final HttpServletRequest request,
                                                       final HttpServletResponse response,
                                                       final Class clazz,
                                                       final int page,
                                                       final int totalPages,
                                                       final int pageSize) {
        plural(uriBuilder, request, clazz);

        final StringBuilder linkHeader = new StringBuilder();
        if (hasNextPage(page, totalPages)) {
            final String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
            linkHeader.append(LinkUtil.createLinkHeader(uriForNextPage, LinkUtil.REL_NEXT));
        }
        if (hasPreviousPage(page)) {
            final String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(LinkUtil.createLinkHeader(uriForPrevPage, LinkUtil.REL_PREV));
        }
        if (hasFirstPage(page)) {
            final String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(LinkUtil.createLinkHeader(uriForFirstPage, LinkUtil.REL_FIRST));
        }
        if (hasLastPage(page, totalPages)) {
            final String uriForLastPage = constructLastPageUri(uriBuilder, totalPages, pageSize);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(LinkUtil.createLinkHeader(uriForLastPage, LinkUtil.REL_LAST));
        }

        if (linkHeader.length() > 0) {
            response.addHeader(HttpHeaders.LINK, linkHeader.toString());
        }
    }

    private String constructNextPageUri(final UriComponentsBuilder uriBuilder, final int page, final int size) {
        return uriBuilder.replaceQueryParam(PAGE, page + 1).replaceQueryParam("size", size).build().encode().toUriString();
    }

    private String constructPrevPageUri(final UriComponentsBuilder uriBuilder, final int page, final int size) {
        return uriBuilder.replaceQueryParam(PAGE, page - 1).replaceQueryParam("size", size).build().encode().toUriString();
    }

    private String constructFirstPageUri(final UriComponentsBuilder uriBuilder, final int size) {
        return uriBuilder.replaceQueryParam(PAGE, 0).replaceQueryParam("size", size).build().encode().toUriString();
    }

    private String constructLastPageUri(final UriComponentsBuilder uriBuilder, final int totalPages, final int size) {
        return uriBuilder.replaceQueryParam(PAGE, totalPages).replaceQueryParam("size", size).build().encode().toUriString();
    }

    private boolean hasNextPage(final int page, final int totalPages) {
        return page < (totalPages - 1);
    }

    private boolean hasPreviousPage(final int page) {
        return page > 0;
    }

    private boolean hasFirstPage(final int page) {
        return hasPreviousPage(page);
    }

    private boolean hasLastPage(final int page, final int totalPages) {
        return (totalPages > 1) && hasNextPage(page, totalPages);
    }

    private void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (linkHeader.length() > 0) {
            linkHeader.append(", ");
        }
    }

    private void plural(final UriComponentsBuilder uriBuilder, final HttpServletRequest request, final Class clazz) {
        final String requestUrl = request.getRequestURI();
        uriBuilder.path(requestUrl);
    }
}
