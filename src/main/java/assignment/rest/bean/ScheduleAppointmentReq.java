package assignment.rest.bean;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class ScheduleAppointmentReq {
	@NotNull(message = "Doctor Id cannot be null")
	private String doctorId;
	
	@NotNull(message = "Patient Id cannot be null")
	private String patientId;
	
	@NotNull(message = "Date cannot be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/YYYY")
	private LocalDate date;
	
	@NotNull(message = "Date cannot be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mma")
	@DateTimeFormat(pattern = "HH:mma")
	private LocalTime time;
}
