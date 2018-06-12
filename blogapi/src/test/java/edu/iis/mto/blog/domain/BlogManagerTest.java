package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.internal.verification.VerificationModeFactory.times;

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
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test (expected = DomainError.class)
    public void addingALikeByNewAccountShouldThrowDomainError() {
        User owner = createUser("Jan", "john@domain.com");
        owner.setAccountStatus(AccountStatus.NEW);
        owner.setId(1L);
        userRepository.save(owner);
        Mockito.when(userRepository.findOne(1L)).thenReturn(owner);
        User liker = createUser("Jon","jon@example.com");
        liker.setId(2L);
        liker.setAccountStatus(AccountStatus.NEW);
        userRepository.save(liker);
        Mockito.when(userRepository.findOne(2L)).thenReturn(liker);
        BlogPost post = createPost(owner);
        post.setId(1L);
        blogPostRepository.save(post);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(post);
        blogService.addLikeToPost(liker.getId(), post.getId());
    }
    private BlogPost createPost(User user) {
        BlogPost post = new BlogPost();
        post.setEntry("test");
        post.setUser(user);
        post.setLikes(new ArrayList<>());
        return post;
    }

    private User createUser(String name, String mail) {
        User user = new User();
        user.setEmail(mail);
        user.setFirstName(name);
        user.setAccountStatus(AccountStatus.NEW);
        return user;
    }
}
