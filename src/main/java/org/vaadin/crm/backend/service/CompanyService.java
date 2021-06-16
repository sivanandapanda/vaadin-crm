package org.vaadin.crm.backend.service;

import org.springframework.stereotype.Service;
import org.vaadin.crm.backend.entity.Company;
import org.vaadin.crm.backend.repository.CompanyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

  private CompanyRepository companyRepository;

  public CompanyService(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  public List<Company> findAll() {
    return companyRepository.findAll();
  }

  public Map<String, Integer> getStats() {
    Map<String, Integer> stats = new HashMap<>();
    findAll().forEach(company -> stats.put(company.getName(), company.getEmployees().size()));
    return stats;
  }
}