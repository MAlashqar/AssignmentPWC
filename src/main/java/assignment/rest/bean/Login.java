package assignment.rest.bean;

import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
public class Login {

	@NotNull(message = "username cannot be null")
	private String username;
	@NotNull(message = "password cannot be null")
	private String password;
	
	@NotNull(message = "userType cannot be null please select (Doctor,Patient)")
private String userType;
}
