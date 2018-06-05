package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    private LikePost likePost;
    private User user;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("user@example.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        entityManager.persist(user);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Test entry");
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void shouldNotFindLikedPostsGivenThereAreNone() {
        Optional<LikePost> likedPosts = likePostRepository.findByUserAndPost(user, blogPost);

        Assert.assertThat(likedPosts, Matchers.is(Optional.empty()));
    }

    @Test
    public void shouldStoreLikedPostGivenCorrectData() {
        likePostRepository.save(likePost);
        List<LikePost> likedPostsList = likePostRepository.findAll();

        Assert.assertThat(likedPostsList.size(), Matchers.is(1));
        Assert.assertThat(likedPostsList.get(0), Matchers.is(likePost));
    }

    @Test
    public void shouldFindLikePostGivenUserAndPost() {
        likePostRepository.save(likePost);
        Optional<LikePost> likedPosts = likePostRepository.findByUserAndPost(user, blogPost);

        Assert.assertThat(likedPosts, Matchers.is(Optional.of(likePost)));
    }

    @Test
    public void shouldNotFindLikePostGivenIncorrectUserOrPost() {
        User wrongUser = new User();
        wrongUser.setEmail("otheruser@example.com");
        wrongUser.setAccountStatus(AccountStatus.CONFIRMED);

        likePostRepository.save(likePost);
        entityManager.persist(wrongUser);

        Optional<LikePost> likedPosts = likePostRepository.findByUserAndPost(wrongUser, blogPost);

        Assert.assertThat(likedPosts, Matchers.is(Optional.empty()));
    }
}
