package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        Long userID = blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void creatingANewPostShouldSaveItWithNoLikes() {
        blogService.createPost(1L, new PostRequest("Test entry"));
        ArgumentCaptor<BlogPost> blogPostParam = ArgumentCaptor.forClass(BlogPost.class);
        Mockito.verify(blogPostRepository).save(blogPostParam.capture());
        BlogPost blogPost = blogPostParam.getValue();
        Assert.assertThat(blogPost.getLikes(), Matchers.nullValue());
    }

    @Test(expected = DomainError.class)
    public void addingALikeToOwnPostShouldThrowDomainError() {
        User owner = new User();
        owner.setId(1L);
        Mockito.when(userRepository.findOne(1L)).thenReturn(owner);

        User liker = new User();
        liker.setId(2L);
        Mockito.when(userRepository.findOne(2L)).thenReturn(liker);

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(owner);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(blogPost);

        blogService.addLikeToPost(owner.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void addingALikeByNewAccountShouldThrowDomainError() {
        User owner = new UserBuilder().withID(1L).build();
        Mockito.when(userRepository.findOne(1L)).thenReturn(owner);

        User liker = new UserBuilder().withID(2L).build();
        Mockito.when(userRepository.findOne(2L)).thenReturn(liker);

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(owner);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(blogPost);

        blogService.addLikeToPost(liker.getId(), blogPost.getId());
    }
}
