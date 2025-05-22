package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentSessionRepository extends JpaRepository<ContentSession, Long> {
}
