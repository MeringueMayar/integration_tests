package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

public class UserBuilder {

    private User user;

    public UserBuilder() {
        user = new User();
        user.setFirstName("T");
        user.setLastName("Est");
        user.setEmail("test@example.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    public UserBuilder withFirstName(String firstName) {
        user.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        user.setLastName(lastName);
        return this;

    }

    public UserBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withID(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder withAccountStatus(AccountStatus accountStatus) {
        user.setAccountStatus(accountStatus);
        return this;
    }

    public User build() {
        return user;
    }
}
