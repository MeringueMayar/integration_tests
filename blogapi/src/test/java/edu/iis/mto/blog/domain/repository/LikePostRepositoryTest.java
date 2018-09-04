package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    private LikePost singleLike;
    private List<LikePost> likesPost;
    private User persistedUser;
    private BlogPost persistedPost;

    @Before
    public void setUp() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Kowalsky");
        user.setEmail("john@yahoo.com");
        user.setAccountStatus(AccountStatus.NEW);

        likesPost = new ArrayList<LikePost>();

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("SOME_CONTENT_123_!@#");
        blogPost.setLikes(likesPost);

        singleLike = new LikePost();

        persistedUser = entityManager.persist(user);
        persistedPost = entityManager.persist(blogPost);

        singleLike.setPost(persistedPost);
        singleLike.setUser(persistedUser);
    }

    @Test
    public void shouldNotFindLikes_givernLikePostRepository_isEmpty(){
        List<LikePost> foundLikes = likePostRepository.findAll();
        assertThat(foundLikes.size(), Matchers.equalTo(0));
    }

    @Test
    public void shouldFindLike_givenLikePostRepository_withThisLike(){
        LikePost persistedLike = entityManager.persist(singleLike);
        List<LikePost> foundLikes = likePostRepository.findAll();

        assertThat(foundLikes.get(0).getId(), Matchers.equalTo(persistedLike.getId()));
    }

    @Test
    public void shouldAddLike_toPost(){
        LikePost persistedLike = likePostRepository.save(singleLike);

        Optional<LikePost> foundLikes = likePostRepository.findByUserAndPost(persistedUser, persistedPost);
        assertThat(foundLikes.get(), Matchers.equalTo(persistedLike));
    }

    @Test
    public void shouldNotFind_notAddedLike(){
        LikePost persistedLike = likePostRepository.save(singleLike);

        LikePost someOtherLike = new LikePost();
        Optional<LikePost> foundLikes = likePostRepository.findByUserAndPost(persistedUser, persistedPost);
        assertThat(foundLikes.get(), Matchers.not(Matchers.equalTo(someOtherLike)));
    }

    @Test
    public void searchingWithIncorrectUserData_shouldNotFindLike(){
        LikePost persistedLike = likePostRepository.save(singleLike);

        User falseUser = new User();
        falseUser.setFirstName("James");
        falseUser.setLastName("Malinovsky");
        falseUser.setEmail("123@interia.pl");
        falseUser.setAccountStatus(AccountStatus.NEW);

        entityManager.persist(falseUser);

        Optional<LikePost> foundLikes = likePostRepository.findByUserAndPost(falseUser, persistedPost);
        assertThat(foundLikes.isPresent(),Matchers.equalTo(false));
    }

    @Test
    public void searchingWithIncorrectPostData_shouldNotFindLike(){
        LikePost persistedLike = likePostRepository.save(singleLike);

        BlogPost falseBlogPost = new BlogPost();
        falseBlogPost.setUser(persistedUser);
        falseBlogPost.setEntry("SOME_CONTENT_123_!@#");
        falseBlogPost.setLikes(likesPost);

        entityManager.persist(falseBlogPost);

        Optional<LikePost> foundLikes = likePostRepository.findByUserAndPost(persistedUser, falseBlogPost);
        assertThat(foundLikes.isPresent(),Matchers.equalTo(false));
    }

    @Test
    public void searchingWithIncorrectUserAndPostData_shouldNotFindLike(){
        LikePost persistedLike = likePostRepository.save(singleLike);

        BlogPost falseBlogPost = new BlogPost();
        falseBlogPost.setUser(persistedUser);
        falseBlogPost.setEntry("SOME_CONTENT_123_!@#");
        falseBlogPost.setLikes(likesPost);

        entityManager.persist(falseBlogPost);

        User falseUser = new User();
        falseUser.setFirstName("James");
        falseUser.setLastName("Malinovsky");
        falseUser.setEmail("123@interia.pl");
        falseUser.setAccountStatus(AccountStatus.NEW);

        entityManager.persist(falseUser);

        Optional<LikePost> foundLikes = likePostRepository.findByUserAndPost(persistedUser, falseBlogPost);
        assertThat(foundLikes.isPresent(),Matchers.equalTo(false));
    }
}
