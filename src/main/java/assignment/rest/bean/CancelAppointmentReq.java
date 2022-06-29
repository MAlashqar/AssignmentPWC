package assignment.rest.bean;


import javax.validation.constraints.NotNull;


import lombok.Data;
@Data
public class CancelAppointmentReq {
	@NotNull(message = "Doctor Id cannot be null")
	private String doctorId;
	@NotNull(message = "Appointment Id cannot be null")
	
	private String appointmentId;
}
