package it.ovi.demo.controllers.v1.dto;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class UserDetailDto extends CreateUserDetailDto {
    @NotNull
    private Long id;
    private OffsetDateTime creationDate;
    private OffsetDateTime modificationDate;

    public UserDetailDto(Long id, String name, String email, OffsetDateTime creationDate,
                         OffsetDateTime modificationDate) {
        super(name, email);
        this.id = id;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public UserDetailDto(String name, String email) {
        super(name, email);
    }

    protected UserDetailDto() {
        // json
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OffsetDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(OffsetDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
}
