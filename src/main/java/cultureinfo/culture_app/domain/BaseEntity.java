package cultureinfo.culture_app.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//공통 항목
@MappedSuperclass
@Getter
public abstract class BaseEntity {
     protected String createBy; // 누가 생성했는지
     protected LocalDateTime createDate; // 언제 만들었는지
     @Setter
     private String lastModifiedBy; // 최종적으로 누가 수정했는지
     @Setter
     private LocalDateTime lastModifiedDate; // 최종적으로 언제 수정했는지
}
