package com.bank.pay_pilot_kyc.repository;

import com.bank.pay_pilot_kyc.entity.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRepository extends JpaRepository<KycDetails, String> {
}
