package com.hospitalsearch.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospitalsearch.dao.UserDAO;
import com.hospitalsearch.dto.UserAdminDTO;
import com.hospitalsearch.dto.UserRegisterDTO;
import com.hospitalsearch.entity.PatientCard;
import com.hospitalsearch.entity.Role;
import com.hospitalsearch.entity.User;
import com.hospitalsearch.entity.UserDetail;
import com.hospitalsearch.service.PatientCardService;
import com.hospitalsearch.service.RoleService;
import com.hospitalsearch.service.UserService;
import com.hospitalsearch.util.Gender;
import com.hospitalsearch.util.UserDetailRegisterDto;
import com.hospitalsearch.util.UserDto;


@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO dao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PatientCardService patientCardService;

    @Override
    public void save(User newUser) {
        try {
            logger.info("save user: " + newUser);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            PatientCard patientCard = patientCardService.add(new PatientCard());
            UserDetail userDetail = new UserDetail();
            userDetail.setPatientCard(patientCard);
            userDetail.setFirstName("Anonymous");
            userDetail.setLastName("Anonymous");
            userDetail.setGender(Gender.MAN);
            userDetail.setAddress("");
            //todo refactor
            newUser.setUserDetails(userDetail);
            dao.save(newUser);
        } catch (Exception e) {
            logger.error("Error saving user: " + newUser, e);
        }
    }

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        try {
            logger.info("register user: " + userRegisterDTO);
            user.setEmail(userRegisterDTO.getEmail().toLowerCase());
            user.setPassword(userRegisterDTO.getPassword());
            user.setUserRoles(new HashSet<>(Collections.singletonList(roleService.getByType("PATIENT"))));
            save(user);
        } catch (Exception e) {
            logger.error("Error register user: " + userRegisterDTO, e);
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        User user = dao.getById(id);
        try {
            logger.info("Delete user " + user);
            for (Role role : user.getUserRoles()) {
                if (role.getType().equals("ADMIN")) return;
            }
            dao.delete(user);
        } catch (Exception e) {
            logger.error("Error delete user: " + user, e);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            logger.info("Update merge " + user);
            dao.updateUser(user);
        } catch (Exception e) {
            logger.error("Error merge user: " + user, e);
        }
    }

    @Override
    public void update(User user) {
        try {
            logger.info("Update user " + user);
            dao.update(user);
        } catch (Exception e) {
            logger.error("Error update user: " + user, e);
        }
    }

    @Override
    public User getById(Long id) {
        User user = new User();
        try {
            user = dao.getById(id);
            logger.info("Get user: " + user);
        } catch (Exception e) {
            logger.error("User with id " + id + " doesn't exist!", e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            users = dao.getAll();
            logger.info("Get all users!");
        } catch (Exception e) {
            logger.error("Error getting all users", e);
        }
        return users;
    }

    @Override
    public User getByEmail(String email) {
        User user = new User();
        try {
            user = dao.getByEmail(email);
            logger.info("Get user with email " + email);
        } catch (Exception e) {
            logger.error("Error getting user with email " + email, e);
        }
        return user;
    }

    @Override
    public User changeStatus(Long id) {
        User user = null;
        try {
            logger.info("Change status to user with id " + id);
            user = dao.changeStatus(id);
        } catch (Exception e) {
            logger.error("Error changing status user with id " + id, e);
        }
        return user;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        User user = getByEmail(email);
        try {
            logger.info("Change password to user with email " + email);
            user.setPassword(this.passwordEncoder.encode(newPassword));
            dao.update(user);
        } catch (Exception e) {
            logger.error("Error changing password user with email " + email, e);
            return false;
        }
        return true;
    }

   /* //change password
    @Override
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        User user = getUserByEmail(email);

        if (!this.passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(this.passwordEncoder.encode(newPassword));
        userDao.update(user);

        return true;
    }*/

    @Override
    public List<User> getUsers(UserAdminDTO userAdminDTO) {
        List<User> users = new ArrayList<>();
        try {
            users = dao.getUsers(userAdminDTO);
            logger.info("Get all users!");
        } catch (Exception e) {
            logger.error("Error getting all users", e);
        }
        return users;
    }

    @Override
    public List<User> searchUser(UserAdminDTO userAdminDTO) {
        List<User> users = new ArrayList<>();
        try {
            users = dao.searchUser(userAdminDTO);
            logger.info("Search users!");
        } catch (Exception e) {
            logger.error("Error searching users", e);
        }
        return users;
    }

    @Override
    public void registerUpdate(UserDto dto, String email) {
    }

    @Override
    public UserDto getDtoByEmail(String email) {
        return null;
    }

    @Override
    public void registerUpdate(UserDetailRegisterDto dto, String email) {
    }

    //Illia
    @Override
    public List<User> getByRole(String role, int pageNumber, int pageSize, String sortBy, Boolean order) {
        List<User> users = new ArrayList<>();
        try {
            users = dao.getByRole(role, pageNumber, pageSize, sortBy, order);
            logger.info("Get users by role");
        } catch (Exception e) {
            logger.error("Getting users by role " + e);
        }
        return users;
    }

    @Override
    public List<User> getByRole(String role) {
        return dao.getByRole(role);
    }

    @Override
    public Long countOfUsersByRole(String role) {
        return dao.countOfUsersByRole(role);
    }

    @Override
    public Long countOfUsersByRole(String role, String search) {
        return dao.countOfUsersByRole(role, search);
    }

    @Override
    public List<User> searchByRole(String role, String search, int pageNumber, int pageSize, String sortBy, Boolean order) {
        List<User> users = new ArrayList<>();
        try {
            users = dao.searchByRole(role, search, pageNumber, pageSize, sortBy, order);
            logger.info("Search users by role");
        } catch (Exception e) {
            logger.error("Searching users by role " + e);
        }
        return users;
    }

    @Override
    public Integer pageCount(Long countOfItems, int itemsPerPage) {
        return (int) Math.ceil((double) countOfItems / itemsPerPage);
    }
    //Illia
}
