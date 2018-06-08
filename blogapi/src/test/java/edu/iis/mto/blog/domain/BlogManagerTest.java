package edu.iis.mto.blog.domain;

import java.util.ArrayList;
import java.util.Optional;

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
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

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

    @Test
    public void confirmedUserShouldBeAbleToLikePost() {
        User blogPostAuthor = setUpBlogPostAuthor();
        User blogUser = setUpBlogUser(AccountStatus.CONFIRMED);
        BlogPost post = setUpBlogPost(blogPostAuthor);
        Optional<LikePost> likes = Optional.empty();

        Mockito.when(userRepository.findOne(blogPostAuthor.getId())).thenReturn(blogPostAuthor);
        Mockito.when(userRepository.findOne(blogUser.getId())).thenReturn(blogUser);
        Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
        Mockito.when(likePostRepository.findByUserAndPost(blogUser, post)).thenReturn(likes);

        boolean success = blogService.addLikeToPost(blogUser.getId(), post.getId());
        Assert.assertThat(success, Matchers.equalTo(true));
    }

    @Test(expected = DomainError.class)
    public void newUserShouldNotBeAbleToLikePost() {
        User blogPostAuthor = setUpBlogPostAuthor();
        User blogUser = setUpBlogUser(AccountStatus.NEW);
        BlogPost post = setUpBlogPost(blogPostAuthor);
        Optional<LikePost> likes = Optional.empty();

        Mockito.when(userRepository.findOne(blogPostAuthor.getId())).thenReturn(blogPostAuthor);
        Mockito.when(userRepository.findOne(blogUser.getId())).thenReturn(blogUser);
        Mockito.when(blogPostRepository.findOne(post.getId())).thenReturn(post);
        Mockito.when(likePostRepository.findByUserAndPost(blogUser, post)).thenReturn(likes);

        boolean success = blogService.addLikeToPost(blogUser.getId(), post.getId());
        Assert.assertThat(success, Matchers.equalTo(false));
    }

    private User setUpBlogPostAuthor() {
        User blogPostAuthor = new User();
        blogPostAuthor.setAccountStatus(AccountStatus.CONFIRMED);
        blogPostAuthor.setFirstName("Jan");
        blogPostAuthor.setLastName("Nowak");
        blogPostAuthor.setEmail("john@domain.com");
        blogPostAuthor.setId(1L);
        return blogPostAuthor;
    }

    private User setUpBlogUser(AccountStatus accountStatus) {
        User blogUser = new User();
        blogUser.setAccountStatus(accountStatus);
        blogUser.setFirstName("Tomasz");
        blogUser.setLastName("Kowalski");
        blogUser.setEmail("tom@domain.com");
        blogUser.setId(2L);
        return blogUser;
    }

    private BlogPost setUpBlogPost(User blogPostAuthor) {
        BlogPost blogPost = new BlogPost();
        blogPost.setEntry("Nowy wpis");
        blogPost.setUser(blogPostAuthor);
        blogPost.setLikes(new ArrayList<LikePost>());
        blogPost.setId(3L);
        return blogPost;
    }

}
