package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikePostRepository likePostRepository;
    @Autowired
    private BlogPostRepository blogPostRepository;

    private User mockUser;
    private BlogPost mockPost;
    @Before
    public void setUp()
    {
        mockUser = createUser("Jan","john@domain.com");
        mockPost = createPost(mockUser);
    }

    private BlogPost createPost(User mockUser) {
        BlogPost post = new BlogPost();
        post.setEntry("test");
        post.setUser(mockUser);
        post.setLikes(new ArrayList<>());
        blogPostRepository.save(post);
        return post;
    }

    private User createUser(String name, String mail) {
        User user = new User();
        user.setEmail(mail);
        user.setFirstName(name);
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);
        return user;
    }

    @Test
    public void findingByUserAndPostShouldReturnValidLikePost()
    {
        LikePost like = new LikePost();
        initLike(mockUser, mockPost, like);
        User otherUser = createUser("John", "ex@example.com");
        userRepository.save(otherUser);
        BlogPost otherPost = createPost(otherUser);
        blogPostRepository.save(otherPost);
        Optional<LikePost> result = likePostRepository.findByUserAndPost(mockUser, mockPost);
        assertThat(result.get(), Matchers.equalTo(like));
    }

    @Test
    public void findingOtherUserAndPostShouldntReturnAnything()
    {
        LikePost like = new LikePost();
        initLike(mockUser, mockPost, like);
        User otherUser = createUser("John", "ex@example.com");
        userRepository.save(otherUser);
        BlogPost otherPost = createPost(otherUser);
        blogPostRepository.save(otherPost);
        Optional<LikePost> result = likePostRepository.findByUserAndPost(otherUser, otherPost);
        assertThat(result.orElse(null), Matchers.equalTo(null));

    }

    @Test
    public void addingLikeShouldBePossible(){
        
    }
    private void initLike(User user, BlogPost post, LikePost like)
    {
        if (like != null)
        {
            like.setUser(user);
            like.setPost(post);
            likePostRepository.save(like);
            mockPost.getLikes().add(like);

        }
        blogPostRepository.save(mockPost);
    }
}