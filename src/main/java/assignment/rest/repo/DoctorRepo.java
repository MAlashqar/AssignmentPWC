package assignment.rest.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.rest.entity.Doctor;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, UUID> {

	Doctor findByUsername(String username);

}
