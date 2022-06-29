package assignment.rest.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import assignment.rest.bean.AllDoctorReq;
import assignment.rest.bean.CancelAppointmentReq;
import assignment.rest.bean.DoctorAvailableReq;
import assignment.rest.bean.ScheduleAppointmentReq;
import assignment.rest.entity.Appointment;
import assignment.rest.entity.Doctor;
import assignment.rest.entity.Patient;
import assignment.rest.repo.AppointmentRepo;
import assignment.rest.repo.DoctorRepo;
import assignment.rest.repo.PatientRepo;

@RestController
@RequestMapping("/appointment")

public class AppointmentController {

	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private AppointmentRepo appRepo;
	@Autowired
	private PatientRepo patientRepo;

	private static Logger log = LoggerFactory.getLogger(AppointmentController.class);
	/**
	 * NOTE :should be add a Token in header,accordingly you can take one token from login API , Token validation 2 hour
	 */
//Link :http://localhost:8080/assignment/appointment/doctorAvailableList
	@PostMapping("/doctorAvailableList")
	public Map<String, Object> doctorFindAll(@Valid @RequestBody DoctorAvailableReq availableReq) {
		log.info("Start Method :doctorFindAll");
		Doctor doctor = new Doctor();
		doctor.setId(UUID.fromString(availableReq.getDoctorId()));
		List<Appointment> allApp = appRepo.findAllByDoctorAndDate(doctor, availableReq.getDate());
		Map<String, Object> result = new HashMap<>();
		if (allApp.isEmpty()) {
			result.put("errorCode", 401);
			result.put("errorMessage", "NO SCHEDUAL APPOINTMENT FOR TODAY ");
			log.info("End Method :doctorFindAll");

			return result;
		}
		result.put("date", availableReq.getDate());
		List<Map<String, String>> getSpData = new ArrayList<>();
		allApp.forEach(appointment -> {
			Map<String, String> data = new HashMap<>();
			data.put("patientName", appointment.getPatient().getFullName());
			data.put("appointmentId", appointment.getId().toString());
			getSpData.add(data);
		});
		result.put("patientList", getSpData);
		log.info("End Method :doctorFindAll");
		return result;
	}
	//Link :http://localhost:8080/assignment/appointment/cancelAppointment

	@DeleteMapping("/cancelAppointment")
	public Map<String, Object> cancelAppointment(@Valid @RequestBody CancelAppointmentReq cancleeReq) {
		log.info("Start Method :cancelAppointment");

		Map<String, Object> result = new HashMap<>();
		Doctor doctor = new Doctor();
		doctor.setId(UUID.fromString(cancleeReq.getDoctorId()));
		Appointment app = appRepo.findByDoctorAndId(doctor, UUID.fromString(cancleeReq.getAppointmentId()));
		if (app != null) {
			appRepo.delete(app);
			result.put("deleteStatus", "True");
			return result;
		}
		result.put("deleteStatus", "False");
		log.info("End Method :cancelAppointment");

		return result;
	}
	//Link :http://localhost:8080/assignment/appointment/availableDoctor

	@PostMapping("/availableDoctor")
	public Map<String, Object> availableDoctor(@Valid @RequestBody AllDoctorReq allDoctor) {
		log.info("Start Method :availableDoctor");

		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> allAvailable = new ArrayList<>();
		List<Doctor> allDoctors = doctorRepo.findAll();

		allDoctors.forEach(doctor -> {
			Map<String, Object> doctorTime = new HashMap<>();
			doctorTime.put("name", doctor.getFullName());
			List<Integer> allTime = appRepo.findAllByDoctorAndDate(doctor, allDoctor.getDate()).stream()
					.map(app -> app.getTime().getHour()).collect(Collectors.toList());
			List<String> allTimeAvailable = new ArrayList<>();
			for (int i = 9; i < 17; i++) {
				if (allTime.contains(i)) {
					continue;
				} else {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mma");
					allTimeAvailable.add(LocalTime.of(i, 00).format(dtf).toString());
				}
			}

			doctorTime.put("AvailableHour", allTimeAvailable);
			allAvailable.add(doctorTime);
		});
		if (allAvailable.isEmpty()) {
			result.put("errorCode", 401);
			result.put("errorMessage", "NO AVAILABLE DOCTOR ");
			log.info("End Method :availableDoctor");

			return result;
		}
		result.put("Doctors", allAvailable);
		log.info("End Method :availableDoctor");

		return result;
	}
	//Link :http://localhost:8080/assignment/appointment/availableDoctor

	@PostMapping("/scheduleAppointment")
	public Map<String, Object> scheduleAppointment(@Valid @RequestBody ScheduleAppointmentReq newApp) {
		log.info("Start Method :scheduleAppointment");

		Map<String, Object> result = new HashMap<>();

		if (newApp.getTime().getMinute() > 0) {
			result.put("errorCode", 401);
			result.put("errorMessage", "Cannot specify minutes, please select hour and 00 min");
			result.put("Add Status", "Failed");
			log.info("End Method :scheduleAppointment");

			return result;
		}
		if (newApp.getTime().getHour() > 17 || newApp.getTime().getHour() < 9) {
			result.put("errorCode", 401);
			result.put("errorMessage", "Doctor Not Available On This Time");
			result.put("Add Status", "Failed");
			log.info("End Method :scheduleAppointment");

			return result;

		}
		Doctor doctor = doctorRepo.findById(UUID.fromString(newApp.getDoctorId())).orElse(null);
		Patient patient = patientRepo.findById(UUID.fromString(newApp.getPatientId())).orElse(null);
		if (doctor == null) {
			result.put("errorCode", 401);
			result.put("errorMessage", "Doctor Not Found");
			result.put("Add Status", "Failed");
			log.info("End Method :scheduleAppointment");

			return result;
		}
		if (patient == null) {
			result.put("errorCode", 401);
			result.put("errorMessage", "Patient Not Found");
			result.put("Add Status", "Failed");
			log.info("End Method :scheduleAppointment");

			return result;
		}
		List<Appointment> checkAllApp = appRepo.findAllByDoctorAndDateAndTime(doctor, newApp.getDate(),
				newApp.getTime());
		if (checkAllApp != null && !checkAllApp.isEmpty()) {
			result.put("Add Status", "Failed");
			result.put("errorCode", 401);
			result.put("errorMessage", "Doctor Not Available On This Time");
			log.info("End Method :scheduleAppointment");

			return result;
		}
		Appointment app = new Appointment(null, doctor, patient, newApp.getDate(), newApp.getTime());
		appRepo.save(app);
		result.put("patientName", patient.getFullName());
		result.put("doctorName", doctor.getFullName());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		result.put("date", newApp.getDate().format(dtf).toString());
		dtf = DateTimeFormatter.ofPattern("HH:mma");
		result.put("time", newApp.getTime().format(dtf).toString());
		result.put("Scheduled", "True");
		log.info("End Method :scheduleAppointment");

		return result;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Map<String, String> handleAllExceptionMethod(Exception ex, WebRequest requset, HttpServletResponse res) {
		Map<String, String> errors = new HashMap<>();
		errors.put("errorCode", "500");
		errors.put("errorMessage", "SOMETHING WRONG ");
		ex.printStackTrace();
		return errors;
	}

}
