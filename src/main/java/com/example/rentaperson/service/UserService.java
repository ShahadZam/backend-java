package com.example.rentaperson.service;
import com.example.rentaperson.dto.PersonAndSkill;
import com.example.rentaperson.dto.UserBody;
import com.example.rentaperson.model.Skill;
import com.example.rentaperson.model.User;
import com.example.rentaperson.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SkillService skillService;

    public UserService(UserRepository userRepository,@Lazy SkillService skillService) {
        this.userRepository = userRepository;
        this.skillService = skillService;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public List<PersonAndSkill> getAllPersons() {
        String skills;
        List<PersonAndSkill> personAndSkills=new ArrayList<>();
        List<User> users=userRepository.findAll();
        List<UserBody> userBodies=formatList(users);
        for (int i = 0; i < userBodies.size(); i++) {
            personAndSkills.add(new PersonAndSkill(userBodies.get(i),skillService.getPersonSkills(userBodies.get(i).getUsername())));
        }
        return personAndSkills;

    }

    public List<PersonAndSkill> getPersonsBySkill(List<User>users) {
        List<PersonAndSkill> personAndSkills=new ArrayList<>();
        List<UserBody> userBodies=formatList(users);
        for (int i = 0; i < userBodies.size(); i++) {
            personAndSkills.add(new PersonAndSkill(userBodies.get(i),skillService.getPersonSkills(userBodies.get(i).getUsername())));
        }
        return personAndSkills;
    }

    public void register(User user) {
        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public void updateUser(User user,Integer id){
        User newUser = userRepository.getById(id);
                newUser.setUsername(user.getUsername());
                newUser.setEmail(user.getEmail());
               String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
                newUser.setPassword(hashedPassword);
                newUser.setCity(user.getCity());
                newUser.setPricePerHour(user.getPricePerHour());
                newUser.setDescription(user.getDescription());

                userRepository.save(newUser);

    }

    public void deleteUser(User user) {
        User myUser=userRepository.getById(user.getId());
        userRepository.delete(myUser);
    }

    public List<UserBody> findByCity(String city) {
        List<User>users=userRepository.findUsersByCity(city);
        return formatList(users);

    }

    public List<UserBody> getUserBySkill(String skill) {
        User user;
        List<UserBody> personList = new ArrayList<>();
        List<Skill> skills = skillService.getBySkills(skill);
        for (Skill value : skills) {
            user = userRepository.findById(value.getPersonId()).get();
            personList.add(format(user));
        }
        return personList;

    }

    public UserBody format(User user) {
        return new UserBody( user.getUsername(), user.getEmail(),
                user.getCity(),user.getPricePerHour(), user.getDescription());
    }

    public List<UserBody> formatList(List<User> user) {
        List<UserBody> userBody =new ArrayList<>();
        for (User value : user) {
            if ((value.getRole()).equals("PERSON")) {
                userBody.add(new UserBody(value.getUsername(), value.getEmail(),
                        value.getCity(),value.getPricePerHour(), value.getDescription()));
            }
        }
        return userBody;
    }

}
