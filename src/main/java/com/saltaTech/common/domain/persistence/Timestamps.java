package com.saltaTech.common.domain.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Timestamps {
	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	@Column(updatable = false)
	private LocalDateTime deletedDate;
	@Builder.Default
	@Column(nullable = false)
	private boolean enabled = true;
}
