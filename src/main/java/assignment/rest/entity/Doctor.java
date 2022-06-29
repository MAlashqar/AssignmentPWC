package assignment.rest.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DOCTOR")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7795273480205251969L;
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "ID", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(name = "username",unique=true)
	@NotNull(message = "username cannot be null")
	private String username;
	
	@Column(name = "password")
	@NotNull(message = "password cannot be null")
	private String password;
	
	@Column(name = "full_name")
	@NotNull(message = "fullName cannot be null")
	private String fullName;
	
	@Column(name = "national_ID")
	@NotNull(message = "nationalId cannot be null")
	private String nationalId;
	
	@Column(name = "speciality")
	@NotNull(message = "speciality cannot be null")
	private String speciality;

}
