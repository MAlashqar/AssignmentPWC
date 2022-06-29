package assignment.rest.bean;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DoctorAvailableReq {
	@NotNull(message = "Doctor Id cannot be null")
	private String doctorId;
	@NotNull(message = "Date cannot be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/YYYY")

	private LocalDate date;

//	@NotNull(message = "Id cannot be null")
//	@Positive(message = "Id must be greather than 0")
//	Integer id;
//	@NotEmpty(message = "Name cannot be empty")
//	String name;
//
//	Integer salary;
//	@Email(regexp = ".+@.+\\..+", message = "Please add valid email")
//	String email;
}
