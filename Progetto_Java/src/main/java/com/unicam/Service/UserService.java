package com.unicam.Service;

import com.unicam.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repoUser;

    public void login(){}

    public void singIn(){}

    public void logout(){}

    public void deleteAccount(){}
}
