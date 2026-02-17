package com.jpmc.midascore.entity;


import jakarta.persistence.*;
import com.jpmc.midascore.entity.UserRecord;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord recipient;

    private Float amount;

    public TransactionRecord(){}

    public TransactionRecord(UserRecord sender, UserRecord recipient, Float amount){
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public Long getId(){
        return id;
    }

    public UserRecord getSender(){
        return sender;
    }

    public UserRecord getRecipient(){
        return recipient;
    }
    public Float getAmount(){
        return amount;
    }


}
