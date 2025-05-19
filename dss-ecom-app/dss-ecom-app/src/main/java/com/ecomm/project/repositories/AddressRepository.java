package com.ecomm.project.repositories;

import com.ecomm.project.model.Address;
import com.ecomm.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
