package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

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
    @Test
    public void likePostByUserAllConfirmed() {
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        User user1 = User("Ala", "Alalowska", "alalowska@poczta.onet.pl");
        user1.setId(1000L);
        userRepository.save(user1);
        User user2 = User("Barbara", "Barbarowska", "barbarowska@wp.pl");
        user2.setId(2000L);
        userRepository.save(user2);
        verify(userRepository, times(2)).save(userParam.capture());
        BlogPost blogPost = createPost(user1);
        blogPost.setId(3000L);
        blogPostRepository.save(blogPost);
        ArgumentCaptor<BlogPost> postParam = ArgumentCaptor.forClass(BlogPost.class);
        verify(blogPostRepository).save(postParam.capture());
        List<User> users = userParam.getAllValues();
        BlogService spyBlogService = Mockito.spy(BlogService.class);
        when(spyBlogService.addLikeToPost(users.get(1).getId(), blogPost.getId())).thenReturn(true);

        Assert.assertThat(users.get(1).getAccountStatus(), is(equalTo(AccountStatus.CONFIRMED)));
        Assert.assertThat(spyBlogService.addLikeToPost(users.get(1).getId(), blogPost.getId()), is(true));
    }
    private User User(String name, String lastName, String mail) {
        User user = new User();
        user.setEmail(mail);
        user.setFirstName(name);
        user.setLastName(lastName);
        user.setAccountStatus(AccountStatus.CONFIRMED);
        return user;
    }
    private BlogPost createPost(User user) {
        BlogPost post = new BlogPost();
        post.setEntry("Post 123");
        post.setUser(user);
        post.setLikes(new ArrayList<>());
        return post;
    }
}
