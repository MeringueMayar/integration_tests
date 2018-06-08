package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    private LikePost like;
    private List<LikePost> likes;
    private User persistedUser;
    private BlogPost persistedPost;

    @Before
    public void setUp() {
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        likes = new ArrayList<LikePost>();

        BlogPost post = new BlogPost();
        post.setUser(user);
        post.setEntry("Tresc posta");
        post.setLikes(likes);
        like = new LikePost();

        persistedUser = entityManager.persist(user);
        persistedPost = entityManager.persist(post);

        like.setPost(persistedPost);
        like.setUser(persistedUser);

    }

    @Test
    public void shouldFindNoLikesIfRepositoryIsEmpty() {
        List<LikePost> likesRepository = repository.findAll();
        Assert.assertThat(likesRepository, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneLikeIfRepositoryHasOneLikePostEntity() {
        likes.add(like);
        LikePost persistedLike = entityManager.persist(like);

        List<LikePost> likesRepository = repository.findAll();
        Assert.assertThat(likesRepository, Matchers.hasSize(1));
        Assert.assertThat(likesRepository.get(0).getPost(), Matchers.equalTo(persistedLike.getPost()));
    }

    @Test
    public void shouldStoreNewLike() {
        likes.add(like);
        LikePost persistedLike = repository.save(like);

        Assert.assertThat(persistedLike.getId(), Matchers.notNullValue());
    }

    @Test
    public void likedPostShouldHaveAddedNewLike() {
        likes.add(like);
        LikePost persistedLike = repository.save(like);

        Assert.assertThat(persistedPost.getLikes().contains(persistedLike), Matchers.equalTo(true));
    }

    @Test
    public void searchingWithCorrectDataShouldReturnLike() {
        likes.add(like);
        LikePost persistedLike = repository.save(like);

        Optional<LikePost> searchedLikes = repository.findByUserAndPost(persistedUser, persistedPost);
        Assert.assertThat(searchedLikes.get(), Matchers.equalTo(persistedLike));
    }

    @Test
    public void searchingWithIncorrectDataShouldNotReturnLike() {
        likes.add(like);
        repository.save(like);
        User incorrectUser = setUpIncorecctUser();
        BlogPost incorrectBlogPost = setUpIncorrectBlogPost();

        Optional<LikePost> searchedLikes = repository.findByUserAndPost(incorrectUser, incorrectBlogPost);
        Assert.assertThat(searchedLikes.isPresent(), Matchers.equalTo(false));
    }

    @Test
    public void searchingWithIncorrectUserShouldNotReturnLike() {
        likes.add(like);
        entityManager.persist(like);
        User incorrectUser = setUpIncorecctUser();

        Optional<LikePost> searchedLikes = repository.findByUserAndPost(incorrectUser, persistedPost);
        Assert.assertThat(searchedLikes.isPresent(), Matchers.equalTo(false));
    }

    @Test
    public void searchingWithIncorrectPostShouldNotReturnLike() {
        likes.add(like);
        entityManager.persist(like);
        BlogPost incorrectBlogPost = setUpIncorrectBlogPost();

        Optional<LikePost> searchedLikes = repository.findByUserAndPost(persistedUser, incorrectBlogPost);
        Assert.assertThat(searchedLikes.isPresent(), Matchers.equalTo(false));
    }

    private User setUpIncorecctUser() {
        User incorrectUser = new User();
        incorrectUser.setEmail("now@n.pl");
        incorrectUser.setLastName("Kowalski");
        incorrectUser.setFirstName("Tomasz");
        incorrectUser.setAccountStatus(AccountStatus.NEW);
        return entityManager.persist(incorrectUser);
    }

    private BlogPost setUpIncorrectBlogPost() {
        BlogPost incorrectBlogPost = new BlogPost();
        incorrectBlogPost.setUser(persistedUser);
        incorrectBlogPost.setEntry("nowy");
        return entityManager.persist(incorrectBlogPost);
    }

}
