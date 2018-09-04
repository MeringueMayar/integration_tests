package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

import static org.hamcrest.Matchers.is;

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
        user.setLastName("Testowy");
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
    public void shouldFindUserGivenFullFirstName() {
        repository.save(user);

        String searchString = "jan";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(true));
    }

    @Test
    public void shouldFindUserGivenPartialFirstName() {
        repository.save(user);

        String searchString = "ja";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(true));
    }

    @Test
    public void shouldFindUserGivenFullEmail() {
        repository.save(user);

        String searchString = "john@domain.com";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(true));
    }

    @Test
    public void shouldFindUserGivenPartialEmail() {
        repository.save(user);

        String searchString = "domain";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(true));
    }

    @Test
    public void shouldFindUserGivenFullLastName() {
        repository.save(user);

        String searchString = "testowy";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(true));
    }

    @Test
    public void shouldFindUserGivenPartialLastName() {
        repository.save(user);

        String searchString = "test";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(true));
    }

    @Test
    public void shouldNotFindUserGivenInvalidData() {
        repository.save(user);

        String searchString = "aezakmi";
        List<User> foundUsersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(searchString, searchString, searchString);

        Assert.assertThat(foundUsersList.contains(user), is(false));
    }
}
