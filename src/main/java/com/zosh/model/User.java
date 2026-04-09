package com.zosh.model;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zosh.domain.USER_ROLE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;

    private String mobile;

    private USER_ROLE role;

    @OneToMany
    private Set<Address> addresses=new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_coupons",
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private Set<Coupon> usedCoupons=new HashSet<>();

	/*
	 * public void setPassword(String encode) { // TODO Auto-generated method stub
	 * 
	 * }
	 */

	/*
	 * public USER_ROLE getRole() { return role; }
	 * 
	 * public void setRole(USER_ROLE role) { this.role = role; }
	 * 
	 * public String getMobile() { return mobile; }
	 * 
	 * public void setMobile(String mobile) { this.mobile = mobile; }
	 * 
	 * public void setFullName(String string) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * public void setEmail(String adminUsername) { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 */
}