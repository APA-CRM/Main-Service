package com.crm.main.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        // TODO: Remove this index. It's unnecessary with unique index
        indexes = @Index(columnList = "file_id,organization_Id", name = "file_id_organization_id_index"),
        uniqueConstraints = @UniqueConstraint(columnNames = "file_id,organization_Id", name = "file_id_organization_id_uq")
)
public class OrganizationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID fileId;

    @ManyToOne(optional = false)
    private Organization organization;

    @Column(nullable = false)
    private Boolean isRoot = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
