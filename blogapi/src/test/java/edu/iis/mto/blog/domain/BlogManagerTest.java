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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;
    @MockBean
    private BlogPostRepository blogPostRepository;
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
    public void likePostByUserThrowDomainError() {
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        blogService.createUser(new UserRequest("Ala", "Alalowska", "alalowska@poczta.onet.pl"));
        verify(userRepository, times(1)).save(userParam.capture());
        blogService.createUser(new UserRequest("Barbara", "Barbarowska", "barbarowska@wp.pl"));
        verify(userRepository, times(2)).save(userParam.capture());
        List<User> users = userParam.getAllValues();

        when(userRepository.findOne(anyLong())).thenReturn(users.get(0));
        blogService.createPost(users.get(0).getId(), new PostRequest());
        ArgumentCaptor<BlogPost> postParam = ArgumentCaptor.forClass(BlogPost.class);
        verify(blogPostRepository).save(postParam.capture());
        BlogPost blogPost = postParam.getValue();
        blogPost.setId(1L);
        blogPost.setUser(users.get(0));
        verify(blogPostRepository).save(postParam.capture());
        blogPost = postParam.getValue();

        BlogService blogService = Mockito.spy(BlogService.class);
        when(blogService.addLikeToPost(users.get(1).getId(), blogPost.getId())).thenThrow(new DomainError("User do not confirmed email yet."));
        Assert.assertThat(users.get(1).getAccountStatus(), not(is(equalTo(AccountStatus.CONFIRMED))));

        blogService.addLikeToPost(users.get(1).getId(), blogPost.getId());
    }
}
