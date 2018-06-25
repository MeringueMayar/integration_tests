package edu.iis.mto.blog.domain.repository;

import static edu.iis.mto.blog.builders.UserBuilder.user;

import java.util.List;

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

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private User user;
    private User user2;

    @Before
    public void setUp() {
        user = user()
                .withFirstName("Jan")
                .withEmail("john@domain.com")
                .build();
        user2 = user()
                .withFirstName("Anna")
                .withEmail("anna@domain.com")
                .build();
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = repository.save(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {
        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }
    
    @Test
    public void shouldFindOneUserIfRepositoryContainsOneMatchingUser() {
        repository.save(user);
        repository.save(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", "test", "test");

        Assert.assertThat(users.size(), Matchers.equalTo(1));
    }
    
    @Test
    public void shouldFindTwoUsersIfRepositoryContainsTwoMatchingUsers() {
        repository.save(user);
        repository.save(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Anna", "test", "john@domain.com");

        Assert.assertThat(users.size(), Matchers.equalTo(2));
    }
    
    @Test
    public void shouldFindNoUsersIfRepositoryDoesNotContainsAnyMatchingUser() {
        repository.save(user);
        repository.save(user2);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "test", "test");

        Assert.assertThat(users.size(), Matchers.equalTo(0));
    }

}
