package com.infodev.eft_rtgs.Model.userManagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLE")

@Getter
@Setter
public class Role {
    @Id
    private Long roleId;
    private String roleName;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users= new HashSet<>();

}
