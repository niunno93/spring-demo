package it.ovi.demo.models;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.OffsetDateTime;

public class UserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private final OffsetDateTime creationDate;
    private final OffsetDateTime modificationDate;

    public UserDetail(Long id, String name, String email, OffsetDateTime creationDate,
                      OffsetDateTime modificationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public UserDetail(String name, String email) {
        this(null, name, email, null, null);
    }

    public Long getId() {
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
