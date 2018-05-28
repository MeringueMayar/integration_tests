package edu.iis.mto.blog.domain;

import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

	@MockBean
	UserRepository userRepository;

	@MockBean
	BlogPostRepository postRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	@Test
	public void creatingNewUserShouldSetAccountStatusToNEW() {

		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
		Mockito.verify(userRepository).save(userParam.capture());
		User user = userParam.getValue();
		Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));

	}

	@Test(expected = DomainError.class)
	public void addLikeByOwnerPostShouldThrowDomainError() {

		// create owner of post
		Long ownerId = new Long(1);
		User owner = new User();
		owner.setId(ownerId);

		// create liker of post
		Long likerId = new Long(2);
		User liker = new User();
		liker.setId(likerId);
		liker.setAccountStatus(AccountStatus.NEW);

		// create post
		BlogPost blogPost = new BlogPost();
		blogPost.setId(ownerId);
		blogPost.setUser(owner);

		when(userRepository.findOne(likerId)).thenReturn(liker);
		when(postRepository.findOne(ownerId)).thenReturn(blogPost);

		blogService.addLikeToPost(likerId, ownerId);

	}
}
