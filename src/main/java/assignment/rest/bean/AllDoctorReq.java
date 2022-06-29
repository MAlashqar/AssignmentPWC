package assignment.rest.bean;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class AllDoctorReq {
	@NotNull(message = "Patient Id cannot be null")
	private String patientId;
	@NotNull(message = "Date cannot be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/YYYY")

	private LocalDate date;
}
