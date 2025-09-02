package com.saltaTech.common.domain.persistence;

import com.saltaTech.common.domain.util.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "people")
@EntityListeners(AuditingEntityListener.class)
public class Person extends Auditable {
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;
	private String email;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(unique = true)
	private String dni;
	@Column(unique = true)
	private String cuil;
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Gender gender;
	@Column(name = "birth_date")
	private LocalDate birthDate;

	public String getFullName() {
		return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
	}
}
