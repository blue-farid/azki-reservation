package com.azki.reservation.domain.reservation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Table(name = "avaliable_slot")
@Entity
@Accessors(chain = true)
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Date startTime;
    @Column
    private Date endTime;
    @Column(name = "is_reserved")
    private boolean reserved;
}
