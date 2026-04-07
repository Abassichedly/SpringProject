package tn.esprit.spring.ARMP.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.ARMP.entity.User;
import tn.esprit.spring.ARMP.enums.UserRole;
import tn.esprit.spring.ARMP.service.interfaces.IUserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

    private final IUserService userService;

    // ========== CRUD ==========
    @GetMapping("/listUser")
    public List<User> retrieveAllUsers() {
        return userService.retrieveAllUsers();
    }

    @GetMapping("/getbyid/{userId}")
    public User retrieveUser(@PathVariable String userId) {
        return userService.retrieveUser(userId);
    }

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{userId}")
    public void removeUser(@PathVariable String userId) {
        userService.removeUser(userId);
    }

    // ========== RECHERCHES SIMPLES ==========
    @GetMapping("/search/firstname/{firstName}")
    public List<User> findByFirstNameContaining(@PathVariable String firstName) {
        return userService.findByFirstNameContaining(firstName);
    }

    @GetMapping("/search/lastname/{lastName}")
    public List<User> findByLastNameContaining(@PathVariable String lastName) {
        return userService.findByLastNameContaining(lastName);
    }

    @GetMapping("/search/email/{email}")
    public List<User> findByEmailContaining(@PathVariable String email) {
        return userService.findByEmailContaining(email);
    }

    @GetMapping("/search/role/{role}")
    public List<User> findByRole(@PathVariable UserRole role) {
        return userService.findByRole(role);
    }

    @GetMapping("/search/actif/{actif}")
    public List<User> findByIsActive(@PathVariable Boolean actif) {
        return userService.findByIsActive(actif);
    }

    @GetMapping("/search/club/{clubId}")
    public List<User> findByClubId(@PathVariable Long clubId) {
        return userService.findByClubId(clubId);
    }

    // ========== RECHERCHE MULTI-CRITÈRES ==========
    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) UserRole role) {
        return userService.searchUsers(firstName, lastName, role);
    }

    @GetMapping("/search/all")
    public List<User> searchAll(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) {
        return userService.searchAll(firstName, lastName, email, role, isActive, clubId, dateStart, dateEnd);
    }

    // ========== TRI ==========
    @GetMapping("/sorted/name")
    public List<User> findAllOrderByFirstNameAsc() {
        return userService.findAllOrderByFirstNameAsc();
    }

    @GetMapping("/sorted/date")
    public List<User> findAllOrderByDateAdhesionDesc() {
        return userService.findAllOrderByDateAdhesionDesc();
    }

    // ========== SPÉCIFIQUE ==========
    @GetMapping("/search/club/{clubId}/actifs")
    public List<User> findByClubIdAndIsActiveTrue(@PathVariable Long clubId) {
        return userService.findByClubIdAndIsActiveTrue(clubId);
    }

    @GetMapping("/search/club/{clubId}/role/{role}")
    public List<User> findByClubIdAndRole(@PathVariable Long clubId, @PathVariable UserRole role) {
        return userService.findByClubIdAndRole(clubId, role);
    }

    @GetMapping("/search/withAtLeast/{minParticipations}")
    public List<User> findUsersWithAtLeastNParticipations(@PathVariable int minParticipations) {
        return userService.findUsersWithAtLeastNParticipations(minParticipations);
    }
}