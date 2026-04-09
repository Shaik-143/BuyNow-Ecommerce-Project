package com.zosh.model;

import com.zosh.domain.AccountStatus;
import com.zosh.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String sellerName;

    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress=new Address();

    private String GSTIN;

    private USER_ROLE role;

    private  boolean isEmailVerified=false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;

//	public String getSellerName() {
//		return sellerName;
//	}
//
//	public void setSellerName(String sellerName) {
//		this.sellerName = sellerName;
//	}
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getGSTIN() {
//		return GSTIN;
//	}
//
//	public void setGSTIN(String gSTIN) {
//		GSTIN = gSTIN;
//	}
//
//	public USER_ROLE getRole() {
//		return role;
//	}
//
//	public void setRole(USER_ROLE role) {
//		this.role = role;
//	}
//
//	public boolean isEmailVerified() {
//		return isEmailVerified;
//	}
//
//	public void setEmailVerified(boolean isEmailVerified) {
//		this.isEmailVerified = isEmailVerified;
//	}
//
//	public AccountStatus getAccountStatus() {
//		return accountStatus;
//	}
//
//	public void setAccountStatus(AccountStatus accountStatus) {
//		this.accountStatus = accountStatus;
//	}
}
