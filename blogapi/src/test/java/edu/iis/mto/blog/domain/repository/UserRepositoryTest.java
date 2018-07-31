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

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    private final String DUMMY_STRING = "dummy";

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalsky");
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
    public void searchingForUser_shouldFindCorrectUser_givenFirstName(){
        User persistedUser = repository.save(user);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), DUMMY_STRING, DUMMY_STRING);

        assertThat(foundUsers.get(0), Matchers.equalTo(persistedUser));
    }

    @Test
    public void searchingForUser_shouldNotFindCorrectUser_givenIncorrectFirstName(){
        User persistedUser = repository.save(user);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Pszemek", DUMMY_STRING, DUMMY_STRING);

        assertThat(foundUsers.size(), Matchers.equalTo(0));
    }

    @Test
    public void searchingForUser_shouldFindCorrectUser_givenLastName(){
        User persistedUser = repository.save(user);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(DUMMY_STRING, user.getLastName(), DUMMY_STRING);

        assertThat(foundUsers.get(0), Matchers.equalTo(persistedUser));
    }

    @Test
    public void searchingForUser_shouldNotFindCorrectUser_givenIncorrectLastName(){
        User persistedUser = repository.save(user);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(DUMMY_STRING, "Placek", DUMMY_STRING);

        assertThat(foundUsers.size(), Matchers.equalTo(0));
    }

    @Test
    public void searchingForUser_shouldFindCorrectUser_givenEmail(){
        User persistedUser = repository.save(user);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(DUMMY_STRING, DUMMY_STRING, user.getEmail());

        assertThat(foundUsers.get(0), Matchers.equalTo(persistedUser));
    }

    @Test
    public void searchingForUser_shouldNotFindCorrectUser_givenIncorrectEmail(){
        User persistedUser = repository.save(user);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(DUMMY_STRING, DUMMY_STRING, "dr4g0n5l43r@o2.pl");

        assertThat(foundUsers.size(), Matchers.equalTo(0));
    }
}
