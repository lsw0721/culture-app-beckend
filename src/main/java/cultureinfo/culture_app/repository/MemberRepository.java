package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    //아이디 찾기
    Optional<Member> findByEmail(String email);

    // loadUserByUsername 에서 이 메서드를 쓰면, roles를 한번에 join fetch 합니다
    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByUsername(String username);

}
