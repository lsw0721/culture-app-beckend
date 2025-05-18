package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Announcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Optional<Announcement> findById(Long id);

}
