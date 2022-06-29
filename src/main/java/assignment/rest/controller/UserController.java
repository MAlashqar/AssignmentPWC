package assignment.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import assignment.rest.bean.Login;
import assignment.rest.entity.Doctor;
import assignment.rest.entity.Patient;
import assignment.rest.repo.DoctorRepo;
import assignment.rest.repo.PatientRepo;
import assignment.rest.security.Jwt;

@RestController
@RequestMapping("/user")

public class UserController {
	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private PatientRepo patientRepo;
	@Autowired
	private Jwt jwt;
	private static Logger log = LoggerFactory.getLogger(UserController.class);
	
//Link :http://localhost:8080/assignment/user/login
	@PostMapping("/login")
	public Map<String, Object> login(@Valid @RequestBody Login login) {
		log.info("Start Method :login");
		Map<String, Object> result = new HashMap<>();

		if (login.getUserType().equalsIgnoreCase("Doctor")) {
			Doctor doctor = doctorRepo.findByUsername(login.getUsername());
			if (doctor == null) {
				result.put("errorCode", 401);
				result.put("errorMessage", "incorrect username");
				log.info("End Method :login");
				return result;
			}
			if (!login.getPassword().equals(doctor.getPassword())) {
				result.put("errorCode", 401);
				result.put("errorMessage", "incorrect password");
				log.info("End Method :login");
				return result;

			}
			result.put("token", jwt.generateToken(doctor.getUsername()));
			log.info("End Method :login");
			return result;

		} else if (login.getUserType().equalsIgnoreCase("Patient")) {
			Patient patient = patientRepo.findByUsername(login.getUsername());
			if (patient == null) {
				result.put("errorCode", 401);
				result.put("errorMessage", "incorrect username");
				log.info("End Method :login");
				return result;
			}
			if (!login.getPassword().equals(patient.getPassword())) {
				result.put("errorCode", 401);
				result.put("errorMessage", "incorrect password");
				log.info("End Method :login");
				return result;

			}
			result.put("token", jwt.generateToken(patient.getUsername()));
			log.info("End Method :login");
			return result;
		} else {
			result.put("errorCode", 401);
			result.put("errorMessage", "incorrect userType");
			log.info("End Method :login");
			return result;
		}

	}
	//Link :http://localhost:8080/assignment/user/addNewDoctor

	@PostMapping("/addNewDoctor")
	Map<String, Object> addNewDoctor(@Valid @RequestBody Doctor newDoctor) {
		log.info("Start Method :addNewDoctor");
		Doctor doctor = doctorRepo.findByUsername(newDoctor.getUsername());
		Map<String, Object> result = new HashMap<>();

		if (doctor != null) {
			result.put("Add Status", "Failed");
			result.put("errorCode", 401);
			result.put("errorMessage", "username already used");
			log.info("End Method :addNewDoctor");

			return result;
		}
		doctorRepo.save(newDoctor);
		result.put("Add Status", "Success");
		log.info("End Method :addNewDoctor");
		return result;
	}

	//Link :http://localhost:8080/assignment/user/addNewPatient
	@PostMapping("/addNewPatient")
	Map<String, Object> addNewPatient(@Valid @RequestBody Patient newPatient) {
		log.info("Start Method :addNewPatient");

		Patient doctor = patientRepo.findByUsername(newPatient.getUsername());
		Map<String, Object> result = new HashMap<>();

		if (doctor != null) {
			result.put("Add Status", "Failed");
			result.put("errorCode", 401);
			result.put("errorMessage", "username already used");
			log.info("End Method :addNewPatient");

			return result;
		}
		patientRepo.save(newPatient);
		result.put("Add Status", "Success");
		log.info("End Method :addNewPatient");

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
