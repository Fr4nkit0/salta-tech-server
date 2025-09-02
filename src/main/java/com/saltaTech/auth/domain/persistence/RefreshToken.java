package com.saltaTech.auth.domain.persistence;
import com.saltaTech.user.domain.persistence.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id ;
	@Column(length = 2048 , nullable = false , updatable = false)
	private String token;
	@Column(nullable = false)
	private Date expiration;
	@Column(nullable = false)
	private boolean isValid;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
