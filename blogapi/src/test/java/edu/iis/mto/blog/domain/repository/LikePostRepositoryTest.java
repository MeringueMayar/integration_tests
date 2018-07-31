package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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


}
