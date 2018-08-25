package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
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

    Long likingUserId;
    Long postToBeLikedId;
    User likingUser;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @Before
    public void setUp() {
        likingUserId = 1000L;
        postToBeLikedId = 500L;

        likingUser = new User();
        likingUser.setId(likingUserId);
        likingUser.setAccountStatus(AccountStatus.NEW);

        User postAuthor = new User();
        postAuthor.setId(100900L);

        BlogPost postToBeLiked = new BlogPost();
        postToBeLiked.setId(postToBeLikedId);
        postToBeLiked.setUser(postAuthor);

        Mockito.when(userRepository.findOne(likingUserId)).thenReturn(likingUser);
        Mockito.when(blogPostRepository.findOne(postToBeLikedId)).thenReturn(postToBeLiked);
        Mockito.when(likePostRepository.findByUserAndPost(likingUser, postToBeLiked)).thenReturn(Optional.empty());
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
    public void confirmedUserAccountShouldBeAbleToAddLikeToPost() {
        likingUser.setAccountStatus(AccountStatus.CONFIRMED);

        Assert.assertThat(blogService.addLikeToPost(likingUserId, postToBeLikedId), Matchers.is(true));
    }

    @Test (expected = DomainError.class)
    public void newUserAccountShouldNotBeAbleToAddLikeToPost_shouldThrowDomainError() {
        likingUser.setAccountStatus(AccountStatus.NEW);

        blogService.addLikeToPost(likingUserId, postToBeLikedId);
    }

}
