package com.blog.service.impls;

import com.blog.dao.TagDao;
import com.blog.dto.TagDto;
import com.blog.exception.InternalServerException;
import com.blog.model.Tag;
import com.blog.service.TagService;
import com.blog.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * The Tag service.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    /**
     * This field used for logging events.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * This field is used to communicate with the database.
     */
    private TagDao tagDao;

    /**
     * This field is used for validate data.
     */
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

    public Tag getTagById(Long tagId) {
        LOGGER.debug("Gets tag by id = [{}].", tagId);
        validator.validateTagId(tagId);
        return tagDao.getTagById(tagId);
    }

    public List<TagDto> getAllTagsByPostsId(final Set<Long> postsId) {
        LOGGER.debug("Gets list of tags by post id = [{}].", postsId);
        return tagDao.getAllTagsByPostsId(postsId);
    }

    public Long addTag(Tag tag) {
        LOGGER.debug("Adds new tag = [{}].", tag);
        validator.checkTagWithTitle(tag);
        return tagDao.addTag(tag);
    }

    public void updateTag(Tag tag) {
        LOGGER.debug("Updates tag = [{}].", tag);
        validator.validateTagId(tag.getId());
        if (!tagDao.updateTag(tag)) {
            throw new InternalServerException(updateError);
        }
    }

    public void deleteTag(Long tagId) {
        LOGGER.debug("Deletes tag by id = [{}].", tagId);
        validator.validateTagId(tagId);
        if (!tagDao.deleteTag(tagId)) {
            throw new InternalServerException(deleteError);
        }
    }
}
