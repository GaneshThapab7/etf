package com.infodev.eft_rtgs.Model.userManagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "user", catalog = "")

@Data
public class User {
    @Id
//@SequenceGenerator(name = "USER_REGISTRATION" , schema = "EFT", sequenceName ="E_USERSEQUENCR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    @Basic
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName;

    @Basic
    @Column(name = "USER_ENAME")
    private String userEname;
    @Basic
    @Column(name = "USER_NNAME")
    private String userNname;
    @Basic
    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;
    @Basic
    @Column(name = "RESOURCE_FLAG")
    private char resource_flag;
    @Basic
    @Column(name = "USER_LOCK_FLAG")
    private char userLockFlag;
    @Basic
    @Column(name = "CREATED_BY")
    private String CreatedBy;
    @Basic
    @Column(name = "DATE_TIME")
   // @Temporal()
    private Date dateTime;

    @Column(name = "LAST_PASS_CHANGE")
    private Date lastPassChange;
    @Column(name = "LAST_LOGIN_DATE")
   // @Temporal()
    private Date lastLoginDate;
    @Column(name = "MAC_REQUIRED")
    private String macRequired;
    @Column(name = "PO_USER")
    private char poUser;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "SUB_DISTRICT_CODE")
    private String subDistrictCode;

  @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  @JoinTable(
          name = "users_roles",
          joinColumns = @JoinColumn(
                  name = "user_id", referencedColumnName = "userId"),
          inverseJoinColumns = @JoinColumn(
                  name = "role_id", referencedColumnName = "roleId"))
  List<Role> roles = new ArrayList<>();

  private boolean askForPasswordChange;
  @Column(name = "TRANSACTION_PIN", nullable = true,length = 8)
  private String transactionPin;

}
