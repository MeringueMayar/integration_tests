package edu.iis.mto.blog.domain;

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
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository postRepository;
    
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
    
    @Test(expected = DomainError.class)
    public void addingLikeToPostByNotConfirmedUserShouldThrowDomainError() {
        Long postAndOwnerId = new Long(1);
        Long likingUserId = new Long(2);
        User likingUser = new User();
        likingUser.setId(likingUserId);
        likingUser.setAccountStatus(AccountStatus.NEW);
        User postOwner = new User();
        postOwner.setId(postAndOwnerId);
        BlogPost post = new BlogPost();
        post.setId(postAndOwnerId);
        post.setUser(postOwner);
        Mockito.when(userRepository.findOne(likingUserId)).thenReturn(likingUser);      
        Mockito.when(postRepository.findOne(postAndOwnerId)).thenReturn(post);
        blogService.addLikeToPost(likingUserId, postAndOwnerId);
    }

}
