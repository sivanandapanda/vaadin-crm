package org.vaadin.crm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.crm.backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}