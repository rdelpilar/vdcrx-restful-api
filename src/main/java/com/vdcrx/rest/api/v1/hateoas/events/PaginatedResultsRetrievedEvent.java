package com.vdcrx.rest.api.v1.hateoas.events;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class PaginatedResultsRetrievedEvent<T> extends ApplicationEvent {
    private final UriComponentsBuilder uriBuilder;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final int page;
    private final int totalPages;
    private final int pageSize;

    public PaginatedResultsRetrievedEvent(final Class<T> clazz,
                                          final UriComponentsBuilder uriBuilderToSet,
                                          final HttpServletRequest requestToSet,
                                          final HttpServletResponse responseToSet,
                                          final int pageToSet,
                                          final int totalPagesToSet,
                                          final int pageSizeToSet) {
        super(clazz);
        uriBuilder = uriBuilderToSet;
        request = requestToSet;
        response = responseToSet;
        page = pageToSet;
        totalPages = totalPagesToSet;
        pageSize = pageSizeToSet;
    }

    public final UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

    public final HttpServletRequest getRequest() { return request; }

    public final HttpServletResponse getResponse() {
        return response;
    }

    public final int getPage() {
        return page;
    }

    public final int getTotalPages() {
        return totalPages;
    }

    public final int getPageSize() {
        return pageSize;
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    @SuppressWarnings("unchecked")
    public final Class<T> getClazz() {
        return (Class<T>) getSource();
    }
}
