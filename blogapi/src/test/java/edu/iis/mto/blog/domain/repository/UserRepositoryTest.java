package edu.iis.mto.blog.domain.repository;

import java.util.List;

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
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
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
    public void searchingForUserWithCorrectFirstNameShouldReturnUser() {
        User persistedUser = repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                user.getFirstName(), "wrongString", "wrongString");
        Assert.assertThat(users.get(0).getFirstName(), Matchers.equalTo(persistedUser.getFirstName()));
    }

    @Test
    public void searchingForUserWithIncorrectDataShouldReturnNoUsers() {
        repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "wrongString", "wrongString", "wrongString");
        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void searchingForUserWithCorrectLastNameShouldReturnUser() {
        User persistedUser = repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "wrongString", persistedUser.getLastName(), "wrongString");
        Assert.assertThat(users.get(0).getLastName(), Matchers.equalTo(persistedUser.getLastName()));
    }

    @Test
    public void searchingForUserWithCorrectEmailShouldReturnUser() {
        User persistedUser = repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "wrongString", "wrongString", persistedUser.getEmail());
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void searchingForUserWithCorrectFirstNameAndLastNameShouldReturnUser() {
        User persistedUser = repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                persistedUser.getFirstName(), persistedUser.getLastName(), "wrongString");
        Assert.assertThat(users.get(0).getFirstName(), Matchers.equalTo(persistedUser.getFirstName()));
        Assert.assertThat(users.get(0).getLastName(), Matchers.equalTo(persistedUser.getLastName()));
    }

    @Test
    public void searchingForUserWithCorrectFirstNameAndEmailShouldReturnUser() {
        User persistedUser = repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                persistedUser.getFirstName(), "wrongString", persistedUser.getEmail());
        Assert.assertThat(users.get(0).getFirstName(), Matchers.equalTo(persistedUser.getFirstName()));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void searchingForUserWithCorrectLastNameAndEmailShouldReturnUser() {
        User persistedUser = repository.save(user);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "wrongString", persistedUser.getLastName(), persistedUser.getEmail());
        Assert.assertThat(users.get(0).getLastName(), Matchers.equalTo(persistedUser.getLastName()));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

}
