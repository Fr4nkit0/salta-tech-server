package com.saltaTech.common.domain.persistence;
import com.saltaTech.user.domain.persistence.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
// @EntityListeners(AuditingEntityListener.class) implementar en las clases hija del Auditable
public abstract class Auditable extends Timestamps {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "created_by", nullable = false, updatable = false)
	private User createdBy;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_modified_by", insertable = false)
	private User lastModifiedBy;
}
