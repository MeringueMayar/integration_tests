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
import static edu.iis.mto.blog.domain.model.AccountStatus.*;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;
import static java.util.Optional.of;


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
    
    private Long likingUserId;
    private User user2, user1;
    private Long postId;
    private BlogPost blogPost;

    @Before
    public void setup(){

        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);


        blogPost = new BlogPost();
        blogPost.setUser(user1);
        blogPost.setId(3L);

        Mockito.when(userRepository
                .findOne(user2.getId()))
                .thenReturn(user2);
        Mockito.when(blogPostRepository
                .findOne(blogPost.getId()))
                .thenReturn(blogPost);
        Mockito.when(likePostRepository
                .findByUserAndPost(user2, blogPost))
                .thenReturn(of(new LikePost()));
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
    public void userWithConfirmedAccountStatusCanLikePosts(){
        user2.setAccountStatus(CONFIRMED);
        blogService.addLikeToPost(user2.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void userWithNewAccountStatusCannotLikePosts(){
        user2.setAccountStatus(NEW);
        blogService.addLikeToPost(user2.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void userWithRemovedAccountStatusCannotLikePosts(){
        user2.setAccountStatus(REMOVED);
        blogService.addLikeToPost(user2.getId(), blogPost.getId());
    }

}