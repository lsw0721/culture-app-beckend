package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDetailRepository extends JpaRepository<ContentDetail, Long>, ContentDetailRepositoryCustom {
}
