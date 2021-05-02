package it.ovi.demo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_detail")
public class UserDetailEntity {
    @Id
    @GeneratedValue
    private long id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;
    @NotNull
    @Column(nullable = false, updatable = false)
    private OffsetDateTime creationDate;
    @NotNull
    @Column(nullable = false)
    private OffsetDateTime modificationDate;

    public UserDetailEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @PrePersist
    private void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        creationDate = now;
        modificationDate = now;
    }

    @PreUpdate
    private void preUpdate() {
        modificationDate = OffsetDateTime.now();
    }

    public UserDetailEntity() {
        this(null, null);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public OffsetDateTime getModificationDate() {
        return modificationDate;
    }
}
