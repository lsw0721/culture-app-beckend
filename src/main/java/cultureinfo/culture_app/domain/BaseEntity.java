package cultureinfo.culture_app.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//공통 항목
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
     private String createBy; // 누가 생성했는지
     private LocalDateTime createDate; // 언제 만들었는지
     private String lastModifiedBy; // 최종적으로 누가 수정했는지
     private LocalDateTime lastModifiedDate; // 최종적으로 언제 수정했는지
}
