package vn.chiendt.skilio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.chiendt.skilio.entity.Installment;

public interface InstallmentRepository extends JpaRepository<Installment, String> {
}
