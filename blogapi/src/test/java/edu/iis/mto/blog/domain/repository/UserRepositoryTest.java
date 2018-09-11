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
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    final static String STR = "datanotpartofanyuserinfointests";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user1, user2, user3;

    @Before
    public void setUp() {
        user1 = new User();
        user1.setFirstName("Jan");
        user1.setLastName("Kowalski");
        user1.setEmail("john@domain.com");
        user1.setAccountStatus(AccountStatus.NEW);

        user2 = new User();
        user2.setFirstName("Patryk");
        user2.setLastName("Jan");
        user2.setEmail("patrick@domain.com");
        user2.setAccountStatus(AccountStatus.NEW);

        user3 = new User();
        user3.setEmail("jan@domain.com");
        user3.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        List<User> users = repository.findAll();
        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user1);
        List<User> users = repository.findAll();
        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {
        User persistedUser = repository.save(user1);
        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserByFirstName() {
        repository.save(user1);
        repository.save(user2);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", STR, STR);
        Assert.assertThat(foundUsers.contains(user1), is(true));
    }

    @Test
    public void shouldFindUserByIncompleteFirstName() {
        repository.save(user1);
        repository.save(user2);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("try", STR, STR);
        Assert.assertThat(foundUsers.contains(user2), is(true));
    }

    @Test
    public void testFindUserFunctionIsNotCaseSensetive() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jAN", "jAN", "jAN");
        Assert.assertThat(foundUsers.contains(user1), is(true));
        Assert.assertThat(foundUsers.contains(user2), is(true));
        Assert.assertThat(foundUsers.contains(user3), is(true));
    }

    @Test
    public void shouldNotFindAnyUsersWhenNoSuchEmailExistsInDB() {
        repository.save(user1);
        repository.save(user2);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(STR, STR, "noemail.com");
        Assert.assertThat(foundUsers.contains(user1), is(false));
        Assert.assertThat(foundUsers.contains(user2), is(false));
    }

    @Test
    public void shouldFindTwoUsersInOneSearchForLastNameAndFirstName() {
        repository.save(user1);
        repository.save(user2);
        List<User> foundUsers = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jan", "jan", STR);
        Assert.assertThat(foundUsers.contains(user1), is(true));
        Assert.assertThat(foundUsers.contains(user2), is(true));
    }
}
