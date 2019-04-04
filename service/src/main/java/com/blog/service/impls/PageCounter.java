package com.blog.service.impls;

public class PageCounter {
    public static Long getCountPages(Long size, Long countOfPosts) {
        Long countOfPages = countOfPosts / size;
        if (countOfPosts % size != 0) {
            countOfPages++;
        }
        return countOfPages;
    }
}
