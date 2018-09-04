package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

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
        user = createUserMock("Jan","john@domain.com");
        post = createBlogPostMockByUser(user);
    }
    private User createUserMock(String firstName, String Email){
        User user = new User();
        user.setFirstName(firstName);
        user.setEmail(Email);
        user.setAccountStatus(AccountStatus.NEW);
        return user;

    }
    private BlogPost createBlogPostMockByUser(User user) {
        BlogPost post = new BlogPost();
        post.setEntry("test");
        post.setUser(user);
        post.setLikes(new ArrayList<>());
        return post;
    }
    @Test
    public void likePostRepositoryTest() {
        userRepository.save(user);
        blogPostRepository.save(post);

        LikePost likePost = createLikePost(post,user);
        likePostRepository.save(likePost);

        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0).getUser(), Matchers.equalTo(likePost.getUser()));
        Assert.assertThat(likePosts.get(0).getPost(), Matchers.equalTo(likePost.getPost()));
        Assert.assertThat(likePosts.get(0), Matchers.equalTo(likePost));
    }
    private LikePost createLikePost(BlogPost post, User user) {
        LikePost like = new LikePost();
        like.setPost(post);
        like.setUser(user);
        return like;
    }
    @Test
    public void editPostTest() {
        userRepository.save(user);
        blogPostRepository.save(post);
        LikePost likePost = createLikePost(post, user);
        likePostRepository.save(likePost);
        post.getLikes().add(likePost);
        blogPostRepository.save(post);

        LikePost post = likePostRepository.findAll().get(0);

        User otherUser = createUserMock("Imie", "imie@poczta.onet.pl");
        BlogPost otherPost = createBlogPostMockByUser(otherUser);
        userRepository.save(otherUser);
        blogPostRepository.save(otherPost);
        post.setPost(otherPost);
        likePostRepository.save(post);

        post = likePostRepository.findAll().get(0);
        assertThat(post.getPost(), Matchers.equalTo(otherPost));
        assertThat(post.getPost(), Matchers.not(equalTo(post)));
    }
    @Test
    public void findByUserAndPostWhenEmptyRepository() {
        userRepository.save(user);
        blogPostRepository.save(post);

        Optional<LikePost> likePostRepositoryByUserAndPost = likePostRepository.findByUserAndPost(user, post);

        assertThat(likePostRepositoryByUserAndPost.orElse(null), Matchers.equalTo(null));
    }
    @Test
    public void findByUserAndPostWhenIsElementInRepository() {
        userRepository.save(user);
        blogPostRepository.save(post);

        LikePost likePost = createLikePost(post, user);
        likePostRepository.save(likePost);
        post.getLikes().add(likePost);
        blogPostRepository.save(post);

        User otherUser = createUserMock("Imie", "imie@poczta.onet.pl");
        userRepository.save(otherUser);
        BlogPost otherPost = createBlogPostMockByUser(otherUser);
        blogPostRepository.save(otherPost);
        LikePost otherLikePost = createLikePost(otherPost, otherUser);
        likePostRepository.save(otherLikePost);
        otherPost.getLikes().add(otherLikePost);
        blogPostRepository.save(otherPost);

        Optional<LikePost> likePostRepositoryByUserAndPost = likePostRepository.findByUserAndPost(user, post);
        assertThat(likePostRepositoryByUserAndPost.get(), Matchers.equalTo(likePost));
    }
    @Test
    public void findByUserAndPostWhenIsNotElementInRepository() {
        userRepository.save(user);
        blogPostRepository.save(post);

        User otherUser = createUserMock("Imie", "imie@poczta.onet.pl");
        userRepository.save(otherUser);
        BlogPost otherPost = createBlogPostMockByUser(otherUser);
        blogPostRepository.save(otherPost);
        LikePost otherLikePost = createLikePost(otherPost, otherUser);
        likePostRepository.save(otherLikePost);
        otherPost.getLikes().add(otherLikePost);
        blogPostRepository.save(otherPost);

        Optional<LikePost> likePostRepositoryByUserAndPost = likePostRepository.findByUserAndPost(user, post);
        assertThat(likePostRepositoryByUserAndPost.orElse(null), Matchers.equalTo(null));
    }
}
