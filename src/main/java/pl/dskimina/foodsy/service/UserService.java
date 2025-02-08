package pl.dskimina.foodsy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public List<UserData> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(toDataService::convert).toList();
    }

    @Transactional
    public UserData getUserById(String userId){
        User user = userRepository.findByUserId(userId);
        return toDataService.convert(user);
    }

    public User getUserInstanceById(String userId){
        return userRepository.findByUserId(userId);
    }
}
