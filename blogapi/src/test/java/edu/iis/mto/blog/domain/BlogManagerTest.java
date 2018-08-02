package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private User blogPostUser;
    private BlogPost blogPost;
    private User blogPostAuthor;

    @Before
    public void setUp(){
        blogPostAuthor = new User();
        blogPostAuthor.setFirstName("John");
        blogPostAuthor.setLastName("Lemon");
        blogPostAuthor.setEmail("123@onet.pl");
        blogPostAuthor.setAccountStatus(AccountStatus.CONFIRMED);
        blogPostAuthor.setId(1L);

        blogPostUser = new User();
        blogPostUser.setFirstName("Mike");
        blogPostUser.setLastName("Placek");
        blogPostUser.setEmail("321333@onet.pl");
        blogPostUser.setId(2L);

        blogPost = new BlogPost();
        blogPost.setEntry("CCONTENT_CONTENT");
        blogPost.setUser(blogPostAuthor);
        blogPost.setId(3L);
        blogPost.setLikes(new ArrayList<LikePost>());
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void userWithConfirmedStatus_shouldAddLikePost(){
        blogPostUser.setAccountStatus(AccountStatus.CONFIRMED);

        Optional<LikePost> likes = Optional.empty();

        Mockito.when(userRepository.findOne(blogPostAuthor.getId())).thenReturn(blogPostAuthor);
        Mockito.when(userRepository.findOne(blogPostUser.getId())).thenReturn(blogPostUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likePostRepository.findByUserAndPost(blogPostUser, blogPost)).thenReturn(likes);

        blogService.addLikeToPost(blogPostUser.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void userWithNewStatus_shouldNotAddLikePost(){
        blogPostUser.setAccountStatus(AccountStatus.NEW);

        Optional<LikePost> likes = Optional.empty();

        Mockito.when(userRepository.findOne(blogPostAuthor.getId())).thenReturn(blogPostAuthor);
        Mockito.when(userRepository.findOne(blogPostUser.getId())).thenReturn(blogPostUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likePostRepository.findByUserAndPost(blogPostUser, blogPost)).thenReturn(likes);

        blogService.addLikeToPost(blogPostUser.getId(), blogPost.getId());
    }
}
