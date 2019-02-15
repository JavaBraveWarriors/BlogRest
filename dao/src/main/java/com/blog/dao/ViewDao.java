package com.blog.dao;

import com.blog.View;

import java.util.List;

public interface ViewDao {

    boolean addView(View view);

    boolean deleteView(Long viewId);

    List<View> getListViewsOfPost(Long initial, Long size, Long postId);

    List<View> getListViewsOfUser(Long initial, Long size, Long userId);

    boolean checkViewByPostIdAndUserId(Long postId, Long userId);
}