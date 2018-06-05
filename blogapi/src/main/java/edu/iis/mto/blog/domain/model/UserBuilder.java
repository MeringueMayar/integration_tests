package edu.iis.mto.blog.domain.model;


public class UserBuilder {
    
    private User user;
    private String firstName="test";
    private String lastName="test";
    private String email="test@test.com";
    private AccountStatus status = AccountStatus.NEW;
    
    public User build() {
        user=new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(status);
        return user;
    };
    
    public UserBuilder user() {
        return this;
    }
    public UserBuilder withFirstName(String firstName) {
        this.firstName=firstName;
        return this;
    }
    public UserBuilder withLastName(String lastName) {
        this.lastName=lastName;
        return this;
    }
    public UserBuilder withEmail(String email) {
        this.email=email;
        return this;
    }
    public UserBuilder withAccountStatus(AccountStatus status) {
        this.status=status;
        return this;
    }
    
    
}
