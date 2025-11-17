package com.azki.reservation.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

// we could use entity graph for "join" or use many-to-one or one-to-many relations too,
// but we do not need the full data of them for now or some of the data more than ID.
// so we could just use these for now
@Data
@Table
@Entity
@Accessors(chain = true)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long customerId;
    @Column(nullable = false)
    private Long slotId;
    @Column
    private boolean active = true;
}
