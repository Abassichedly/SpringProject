package tn.esprit.spring.ARMP.service.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.enums.UserRole;
import tn.esprit.spring.ARMP.repository.UserRepository;
import tn.esprit.spring.ARMP.service.interfaces.IUserService;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        user.setIsActive(true);
        user.setDateAdhesion(LocalDate.now());
        if (user.getRole() == null) {
            user.setRole(UserRole.MEMBRE_SIMPLE);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User retrieveUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void removeUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByFirstNameContaining(String firstName) {
        return userRepository.findByFirstNameContaining(firstName);
    }

    @Override
    public List<User> findByLastNameContaining(String lastName) {
        return userRepository.findByLastNameContaining(lastName);
    }

    @Override
    public List<User> findByEmailContaining(String email) {
        return userRepository.findByEmailContaining(email);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findByIsActive(Boolean isActive) {
        return userRepository.findByIsActive(isActive);
    }

    @Override
    public List<User> findByClubId(Long clubId) {
        return userRepository.findByClubId(clubId);
    }

    @Override
    public List<User> searchUsers(String firstName, String lastName, UserRole role) {
        return userRepository.searchUsers(firstName, lastName, role);
    }

    @Override
    public List<User> searchAll(String firstName, String lastName, String email, UserRole role,
                                Boolean isActive, Long clubId, LocalDate dateStart, LocalDate dateEnd) {
        return userRepository.searchAll(firstName, lastName, email, role, isActive, clubId, dateStart, dateEnd);
    }

    @Override
    public List<User> findAllOrderByFirstNameAsc() {
        return userRepository.findAllByOrderByFirstNameAsc();
    }

    @Override
    public List<User> findAllOrderByDateAdhesionDesc() {
        return userRepository.findAllByOrderByDateAdhesionDesc();
    }

    @Override
    public List<User> findByClubIdAndIsActiveTrue(Long clubId) {
        return userRepository.findByClubIdAndIsActiveTrue(clubId);
    }

    @Override
    public List<User> findByClubIdAndRole(Long clubId, UserRole role) {
        return userRepository.findByClubIdAndRole(clubId, role);
    }

    @Override
    public List<User> findUsersWithAtLeastNParticipations(int minParticipations) {
        return userRepository.findUsersWithAtLeastNParticipations(minParticipations);
    }
}