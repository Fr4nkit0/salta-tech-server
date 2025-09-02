package com.saltaTech.user.domain.persistence;
import com.saltaTech.common.domain.persistence.Timestamps;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends Timestamps implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@Column(length = 50, nullable = false)
	private String firstName;
	@Column(length = 50, nullable = false)
	private String lastName;
	@Column(nullable = false , unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(nullable = false)
	private boolean isSuperUser = false;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (!isSuperUser) return List.of();
		return List.of(new SimpleGrantedAuthority("ROLE_SUPERUSER"));
	}

	@Override
	public String getUsername() {
		return email;
	}
}
