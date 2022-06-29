package assignment.rest.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.rest.entity.Patient;

@Repository
public interface PatientRepo extends JpaRepository<Patient, UUID> {

	Patient findByUsername(String username);

}
