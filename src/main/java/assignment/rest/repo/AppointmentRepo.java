package assignment.rest.repo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.rest.entity.Appointment;
import assignment.rest.entity.Doctor;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, UUID> {

	List<Appointment> findAllByDoctorAndDate(Doctor doc, LocalDate date);

	Appointment findByDoctorAndId(Doctor doc, UUID id);

	List<Appointment> findAllByDoctor(Doctor doc);

	List<Appointment> findAllByDoctorAndDateAndTime(Doctor doc, LocalDate date,LocalTime time);

}
