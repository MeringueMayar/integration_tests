package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import java.util.Optional;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikePostRepository likePostRepository;

    private User user1;
    private BlogPost blogPost1, blogPost2;
    private LikePost likePost;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setFirstName("Jan");
        user1.setLastName("Kowalski");
        user1.setEmail("john@domain.com");
        user1.setAccountStatus(AccountStatus.CONFIRMED);
        entityManager.persist(user1);

        blogPost1 = new BlogPost();
        blogPost1.setUser(user1);
        blogPost1.setEntry("entry");
        entityManager.persist(blogPost1);
        
        blogPost2 = new BlogPost();
        blogPost2.setUser(user1);
        blogPost2.setEntry("entry");
        entityManager.persist(blogPost2);

        likePost = new LikePost();
        likePost.setUser(user1);
        likePost.setPost(blogPost1);
    }

    @Test
    public void shouldNotFindAnyLikedPostsWhenThereAreNone() {
        List<LikePost> likedPosts = likePostRepository.findAll();
        Assert.assertThat(likedPosts, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOnePostIfRepositoryContainsOnePostEntity() {
        likePostRepository.save(likePost);
        List<LikePost> likedPosts = likePostRepository.findAll();
        Assert.assertThat(likedPosts, Matchers.hasSize(1));
    }
    
    @Test
    public void shouldFindLikedPostByUserAndPost() {
        likePostRepository.save(likePost);
        Optional<LikePost> likedPost = likePostRepository.findByUserAndPost(user1, blogPost1);
        Assert.assertThat(likedPost.isPresent(), is(true));
    }

    
    @Test
    public void shouldNotFindLikedPostWithInvalidSearchParameters() {
        likePostRepository.save(likePost);
        Optional<LikePost> likedPost = likePostRepository.findByUserAndPost(user1, blogPost2);
        Assert.assertThat(likedPost.isPresent(), is(false));
    }
    

}
