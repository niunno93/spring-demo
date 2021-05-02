package it.ovi.demo.documents;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Document(indexName = "user_detail_audit")
public class UserDetailAuditDocument {

    @Id
    @GeneratedValue
    private String id;
    @Field
    private Long userDetailId;
    @Field
    private String name;
    @Field
    private String email;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private OffsetDateTime modificationDate;

    public UserDetailAuditDocument(Long userDetailId, String name, String email, OffsetDateTime modificationDate) {
        this.userDetailId = userDetailId;
        this.name = name;
        this.email = email;
        this.modificationDate = modificationDate;
    }

    public String getId() {
        return id;
    }

    public Long getUserDetailId() {
        return userDetailId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public OffsetDateTime getModificationDate() {
        return modificationDate;
    }
}
