package com.blog.service.impls;

import com.blog.Tag;
import com.blog.dao.TagDao;
import com.blog.exception.InternalServerException;
import com.blog.service.TagService;
import com.blog.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    /**
     * This field used for logging events
     */
    private static final Logger LOGGER = LogManager.getLogger();

    private TagDao tagDao;
    private Validator validator;

    @Value("${tagService.notUpdated}")
    private String updateError;

    @Value("${tagService.notDeleted}")
    private String deleteError;

    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator validator) {
        this.validator = validator;
        this.tagDao = tagDao;
    }

    public List<Tag> getAllTags() {
        LOGGER.debug("Gets all tags.");
        return tagDao.getAllTags();
    }

    public Tag getTagById(Long id) {
        LOGGER.debug("Gets tag by id = [{}].", id);
        validator.validateTagId(id);
        return tagDao.getTagById(id);
    }

    public List<Tag> getAllTagsByPostId(Long id) {
        LOGGER.debug("Gets list of tags by post id = [{}].", id);
        validator.validatePostId(id);
        return tagDao.getAllTagsByPostId(id);
    }

    public Long addTag(Tag tag) {
        LOGGER.debug("Adds new tag = [{}].", tag);
        validator.checkTag(tag);
        return tagDao.addTag(tag);
    }

    public void updateTag(Tag tag) {
        LOGGER.debug("Updates tag = [{}].", tag);
        validator.validateTagId(tag.getId());
        if (!tagDao.updateTag(tag))
            throw new InternalServerException(updateError);

    }

    public void deleteTag(Long id) {
        LOGGER.debug("Deletes tag by id = [{}].", id);
        validator.validateTagId(id);
        if (!tagDao.deleteTag(id))
            throw new InternalServerException(deleteError);
    }


}
