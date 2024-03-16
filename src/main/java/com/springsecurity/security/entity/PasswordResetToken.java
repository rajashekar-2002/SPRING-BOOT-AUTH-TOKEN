package com.springsecurity.security.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private static final int EXPIRATION_TIME=10;

    

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",
                nullable = false,
                foreignKey = @ForeignKey(name="FK_USER_PASSWORDrESET_TOKEN"))
    private UserEntity user;



    public PasswordResetToken(UserEntity user,String token){
        this.token=token;
        this.user=user;
        this.expirationTime=calculateExpriationDate(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token){
        this.token=token;
        this.expirationTime=calculateExpriationDate(EXPIRATION_TIME);


    }

    private Date calculateExpriationDate(int expirationTime) {
        // add expirationdate to current date +10
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());

    }
}
