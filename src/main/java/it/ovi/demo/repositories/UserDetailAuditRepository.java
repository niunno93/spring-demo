package it.ovi.demo.repositories;

import it.ovi.demo.documents.UserDetailAuditDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailAuditRepository extends ElasticsearchRepository<UserDetailAuditDocument, String> {
}
