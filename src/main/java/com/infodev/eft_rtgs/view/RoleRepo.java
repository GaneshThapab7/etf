package com.infodev.eft_rtgs.view;

import com.infodev.eft_rtgs.Model.userManagement.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findRoleByRoleName(String roleName);
}
