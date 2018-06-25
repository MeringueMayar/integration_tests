package edu.iis.mto.blog.builders;

import edu.iis.mto.blog.api.request.UserRequest;

public class UserRequestBuilder {
    
    private String firstName;
    
    private String lastName;
    
    private String email;
    
    public static UserRequestBuilder userRequest() {
        return new UserRequestBuilder();
    }
    
    public UserRequestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    public UserRequestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    public UserRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public UserRequest build() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName(firstName);
        userRequest.setLastName(lastName);
        userRequest.setEmail(email);
        return userRequest;
    }
    
}
