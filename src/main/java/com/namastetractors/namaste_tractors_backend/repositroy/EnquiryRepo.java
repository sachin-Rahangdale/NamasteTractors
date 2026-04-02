package com.namastetractors.namaste_tractors_backend.repositroy;

import com.namastetractors.namaste_tractors_backend.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryRepo extends JpaRepository<Enquiry ,Long> {
}
