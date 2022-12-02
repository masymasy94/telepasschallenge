package com.masy.telepasschallenge.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue
    @Column(length = 16)
    UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    Customer customer;


    public enum Status {
        ACTIVE, INACTIVE, LOST
    }

}
