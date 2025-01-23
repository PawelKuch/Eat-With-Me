package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import pl.dskimina.foodsy.entity.User;
import pl.dskimina.foodsy.entity.data.UserData;
import pl.dskimina.foodsy.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ToDataService toDataService;

    public UserService(UserRepository userRepository, ToDataService toDataService) {
        this.userRepository = userRepository;
        this.toDataService = toDataService;
    }

    public List<UserData> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(toDataService::convert).toList();
    }
}
