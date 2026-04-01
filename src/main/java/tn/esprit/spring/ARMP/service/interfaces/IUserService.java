package tn.esprit.spring.ARMP.service.interfaces;

import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.enums.UserRole;

import java.time.LocalDate;
import java.util.List;

public interface IUserService {

        // CRUD
        List<User> retrieveAllUsers();
        User addUser(User user);
        User updateUser(User user);
        User retrieveUser(String id);
        void removeUser(String id);

        // Recherche simple
        List<User> findByFirstNameContaining(String firstName);
        List<User> findByLastNameContaining(String lastName);
        List<User> findByEmailContaining(String email);
        List<User> findByRole(UserRole role);
        List<User> findByIsActive(Boolean isActive);
        List<User> findByClubId(Long clubId);

        // Recherche multi-critères
        List<User> searchUsers(String firstName, String lastName, UserRole role);
        List<User> searchAll(String firstName, String lastName, String email, UserRole role,
                             Boolean isActive, Long clubId, LocalDate dateStart, LocalDate dateEnd);

        // Tri
        List<User> findAllOrderByFirstNameAsc();
        List<User> findAllOrderByDateAdhesionDesc();

        // Spécifique
        List<User> findByClubIdAndIsActiveTrue(Long clubId);
        List<User> findByClubIdAndRole(Long clubId, UserRole role);
        List<User> findUsersWithAtLeastNParticipations(int minParticipations);
}