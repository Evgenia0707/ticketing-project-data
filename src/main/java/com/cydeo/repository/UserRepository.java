package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
 // derived query
      List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);//true - deleted user, false- not (give all users if not deleted)
    User findByUserNameAndIsDeleted(String username, Boolean deleted);
    User findByUserName(String username);//select * from users where userName = 'username' and is_deleted = false (from User)

    @Transactional
    void deleteByUserNameAndIsDeleted(String username, Boolean deleted);

    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description, Boolean deleted);
}
