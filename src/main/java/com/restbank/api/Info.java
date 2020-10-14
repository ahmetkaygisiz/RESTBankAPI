package com.restbank.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class Info {
    private long totalCount;
    private int page;
    private int size;
    private String next;
    private String prev;

    public Info(Page page, String ENDPOINT) {
        this.size = page.getSize();
        this.totalCount = page.getTotalElements();
        this.page = page.getTotalPages();
        this.next = page.hasNext() ? ENDPOINT + "?page=" + page.nextOrLastPageable().getPageNumber() + "&size=" + page.getSize()  : null;
        this.prev = page.hasPrevious() ? ENDPOINT + "?page=" + page.previousOrFirstPageable().getPageNumber() + "&size=" + page.getSize() : null;
    }
}
