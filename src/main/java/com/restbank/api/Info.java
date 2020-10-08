package com.restbank.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class Info {
    private long totalCount;
    private int pages;
    private String next;
    private String prev;


    public Info(Page page, String ENDPOINT) {
        this.totalCount = page.getTotalElements();
        this.pages = page.getTotalPages();
        this.next = page.hasNext() ? ENDPOINT + "?page=" + page.nextOrLastPageable().getPageNumber() + "&pageSize=" + page.getSize()  : null;
        this.prev = page.hasPrevious() ? ENDPOINT + "?page=" + page.previousOrFirstPageable().getPageNumber() + "&pageSize=" + page.getSize() : null;
    }
}
