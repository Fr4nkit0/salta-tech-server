package com.saltaTech.organization.domain.persistence;

import com.saltaTech.common.domain.persistence.Timestamps;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "organizations")
public class Organization extends Timestamps {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@Column(nullable = false,length = 50)
	private String name ;
	@Column(nullable = false, length = 50 , unique = true)
	private String slug;
}
