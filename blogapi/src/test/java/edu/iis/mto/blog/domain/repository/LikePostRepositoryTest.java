package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private User user;
    private BlogPost post;

    @Before
    public void setUp() {
        createUserMock();
        createBlogPostMockByUser(user);
    }
    private void createUserMock(){
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }
    private void createBlogPostMockByUser(User user) {
        post = new BlogPost();
        post.setEntry("test");
        post.setUser(user);
    }
/*
Utwórz JUnit TestCase dla klasy LikePostRepository. Opracuj testy sprawdzające poprawność tworzenia i modyfikacji encji LikePost oraz przetestuj metodę findByUserAndPost.

 */
    @Test
    public void likePostRepositoryTest() {
        userRepository.save(user);
        blogPostRepository.save(post);

        LikePost likePost = new LikePost();
        likePost.setPost(post);
        likePost.setUser(user);
        likePostRepository.save(likePost);

        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0).getUser(), Matchers.equalTo(likePost.getUser()));
        Assert.assertThat(likePosts.get(0).getPost(), Matchers.equalTo(likePost.getPost()));
        Assert.assertThat(likePosts.get(0), Matchers.equalTo(likePost));
    }
}
