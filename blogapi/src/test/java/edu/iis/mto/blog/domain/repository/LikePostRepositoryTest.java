package edu.iis.mto.blog.domain.repository;

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
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private DataFinder dataFinder;
    @MockBean
    private BlogService blogService;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

/*
Utwórz JUnit TestCase dla klasy LikePostRepository. Opracuj testy sprawdzające poprawność tworzenia i modyfikacji encji LikePost oraz przetestuj metodę findByUserAndPost.

 */
    @Test
    public void like(){
        User persistedUser = repository.save(user);
        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
        PostRequest postRequest=new PostRequest();

        Long idPost = blogService.createPost(persistedUser.getId(),postRequest);
        Assert.assertThat(idPost, Matchers.notNullValue());
        List<BlogPost> blogPosts = blogPostRepository.findByUser(persistedUser);
        blogService.addLikeToPost(persistedUser.getId(), idPost);
        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(persistedUser,blogPosts.get(0));
        LikePost likePost = likePostOptional.get();
        LikePost findPost = dataFinder.getPost(idPost);

        Assert.assertThat(findPost, Matchers.equalTo(likePost));
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Janek","","john@domain.com");
        Assert.assertThat(users, Matchers.hasSize(0));
    }
}
