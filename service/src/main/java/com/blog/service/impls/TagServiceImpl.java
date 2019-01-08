package com.blog.service.impls;

import com.blog.Tag;
import com.blog.dao.TagDao;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private TagDao tagDao;

    @Value("${tagService.incorrectId}")
    private String incorrectTagId;

    @Value("${tagService.notExist}")
    private String notExistTag;

    @Value("${tagService.exist}")
    private String existTag;

    @Value("${postService.incorrectId}")
    private String incorrectPostId;

    @Value("${postService.notExist}")
    private String notExistPost;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public List<Tag> getAllTags() {
        return tagDao.getAllTags();
    }

    public Tag getTagById(Long id) {
        validateTagId(id);
        return tagDao.getTagById(id);
    }

    public List<Tag> getAllTagsByPostId(Long id) {
        return tagDao.getAllTagsByPostId(id);
    }

    public Long addTag(Tag tag) {
        checkTag(tag);
        return tagDao.addTag(tag);
    }

    public void updateTag(Tag tag) {
        validateTagId(tag.getId());
        tagDao.updateTag(tag);
    }

    public void deleteTag(Long id) {
        validateTagId(id);
        tagDao.deleteTag(id);
    }

    public void validateTagId(Long id) {
        if (id == null || id < 0L)
            throw new ValidationException(incorrectTagId);
        if (!tagDao.checkTagById(id))
            throw new NotFoundException(notExistTag);
    }

    private void checkTag(Tag tag) {
        if (tagDao.checkTagByTitle(tag.getTitle()))
            throw new ValidationException(existTag);
    }
}
