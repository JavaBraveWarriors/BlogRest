package com.blog.dao;

import com.blog.model.View;

import java.util.List;

/**
 * This interface defines various operations for easy management for the View object.
 * Use this interface if you want to access the View.
 *
 * @author Aliaksandr Yeutushenka
 * @see View
 */
public interface ViewDao {

    /**
     * Add new view.
     *
     * @param view is {View} value to be added.
     * @return {boolean} value, if view was added - returns true, if not - false.
     */
    boolean addView(View view);

    /**
     * Delete view using view ID.
     *
     * @param viewId is {Long} value which identifies the view ID.
     * @return {boolean} value, if view was deleted - returns true, if not - false.
     */
    boolean deleteView(Long viewId);

    /**
     * Gets list views of post.
     *
     * @param initial is {Long} value ID of the view from which you want to get objects.
     * @param size    is {Long} value the number of required objects.
     * @param postId  is {Long} value which identifies the necessary post ID.
     * @return {List<View>} is a list of views.
     */
    List<View> getListViewsOfPost(Long initial, Long size, Long postId);

    /**
     * Gets list views of user.
     *
     * @param initial is {Long} value ID of the view from which you want to get objects.
     * @param size    is {Long} value the number of required objects.
     * @param userId  is {Long} value which identifies the necessary user ID.
     * @return the list views of user
     */
    List<View> getListViewsOfUser(Long initial, Long size, Long userId);

    /**
     * Check view by post id and user id.
     *
     * @param postId is {Long} value which identifies the necessary post ID.
     * @param userId is {Long} value which identifies the necessary user ID.
     * @return {boolean} value, if view exists - returns true, if not - false.
     */
    boolean checkViewByPostIdAndUserId(Long postId, Long userId);
}