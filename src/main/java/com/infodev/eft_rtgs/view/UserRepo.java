package com.infodev.eft_rtgs.view;

import com.infodev.eft_rtgs.Model.userManagement.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findUSerByUserNameAndPassword(String username,String password);
    User findUserByUserIdAndPassword(Long userId, String password);
    @Query(value = "select e.*, c.* from user e join CGAS_C_DISTRICT c\n" +
            "    on e.DISCTICT_CD = c.DISTRICT_CD",nativeQuery = true)
    List<User> findUsersAll();
}
