package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "TBL_POST")
@EntityListeners(value = AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    //    @Column(length = 1000)
    @Column(
            // application.yml의 ddl-auto: 에서 적용되지 않음.
            columnDefinition = "LONGTEXT"
    )
    private String content;

    @ColumnDefault(value = "0")
    private long viewCnt;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime regDate;



}
