package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
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
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    private User persistedUser;
    private User persistedUser2;

    private BlogPost persistedPost;
    private BlogPost persistedPost2;

    private LikePost likePost;
    private LikePost likePost2;

    @Before
    public void setUp() {
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        persistedUser = entityManager.persist(user);

        BlogPost post = new BlogPost();
        post.setEntry("Test entry");
        post.setUser(persistedUser);
        persistedPost = entityManager.persist(post);

        User user2 = new User();
        user2.setFirstName("Stefan");
        user2.setLastName("Kowalski");
        user2.setEmail("stefan@domain.com");
        user2.setAccountStatus(AccountStatus.NEW);
        persistedUser2 = entityManager.persist(user2);

        BlogPost post2 = new BlogPost();
        post2.setEntry("Test entry no. 2");
        post2.setUser(persistedUser2);
        persistedPost2 = entityManager.persist(post2);

        likePost = new LikePost();
        likePost.setUser(persistedUser);
        likePost.setPost(persistedPost);

        likePost2 = new LikePost();
        likePost2.setUser(persistedUser2);
        likePost2.setPost(persistedPost2);

    }

    @Test
    public void shouldFindNoLikePostsInEmptyRepository() {
        Assert.assertThat(repository.findAll(), Matchers.hasSize(0));
    }

    @Test
    public void shouldFindProperNumberOfLikePostsInRepository() {
        repository.save(likePost);
        repository.save(likePost2);
        Assert.assertThat(repository.findAll(), Matchers.hasSize(2));
    }

    @Test
    public void shouldFindProperLikePostInRepository() {
        repository.save(likePost);
        repository.save(likePost2);
        Assert.assertThat(repository.findByUserAndPost(persistedUser2, persistedPost2).get(), Matchers.is(likePost2));
    }

    @Test
    public void shouldNotFindLikePostInRepositoryForIncorrectData() {
        repository.save(likePost);
        repository.save(likePost2);
        Assert.assertThat(repository.findByUserAndPost(persistedUser, persistedPost2).isPresent(), Matchers.is(false));
    }

}
