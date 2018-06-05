package edu.iis.mto.blog.domain.repository;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.model.UserBuilder;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    
    @Autowired
    LikePostRepository likePostRepository;
    @Autowired 
    UserRepository userRepository;
    @Autowired
    BlogPostRepository blogPostRepository;
    
    
    User user;
    User user2;
    BlogPost blogPost;
    LikePost likePost;
    @Before
    public void setUp() {
        user = new UserBuilder().withFirstName("Tomek").build();
        user2= new UserBuilder().withFirstName("Jan").withEmail("jan@test.pl").build();
        userRepository.save(user);
        userRepository.save(user2);
        
        
        blogPost=new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Test entry");
        blogPostRepository.save(blogPost);
        //
        likePost=new LikePost();
    }
    
    @Test
    public void shouldFindNoLikePostIfRepositoryIsEmptyTest() {
        List<LikePost> likePostList=likePostRepository.findAll();
         Assert.assertThat(likePostList, Matchers.hasSize(0));
    }
    
    @Test
    public void shouldReturnListWithSizeOneIfThereIsOnLike() {
        likePost.setPost(blogPost);
        likePost.setUser(user2);
        likePostRepository.save(likePost);
        List<LikePost> likePostList=likePostRepository.findAll();
        Assert.assertThat(likePostList, Matchers.hasSize(1));
    }
    
    @Test
    public void findByUserAndPostTest() {
        likePost.setPost(blogPost);
        likePost.setUser(user2);
        likePostRepository.save(likePost);
        Optional<LikePost> likePostList=likePostRepository.findByUserAndPost(user2, blogPost);
        Assert.assertThat(likePostList.get().getUser(), Matchers.is(user2));
    }

}
