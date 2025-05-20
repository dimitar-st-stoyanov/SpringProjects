package com.ecomm.project.controller;

import com.ecomm.project.model.Address;
import com.ecomm.project.model.User;
import com.ecomm.project.payload.AddressDTO;
import com.ecomm.project.service.AddressServiceImpl;
import com.ecomm.project.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    public AddressServiceImpl addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){

        User user =    authUtil.loggedInUser();
        AddressDTO newAddress = addressService.createAddress(addressDTO, user);

        return new ResponseEntity<AddressDTO>(newAddress, HttpStatus.CREATED);
    }

    @GetMapping ("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> allAddresses = addressService.getAllAddresses();
        return new ResponseEntity<>(allAddresses, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO searchedAddress = addressService.findSpecificAddress(addressId);

        return new ResponseEntity<AddressDTO>(searchedAddress, HttpStatus.FOUND);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddressesAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> allAddresses = addressService.getUserAddresses(user);
        return new ResponseEntity<>(allAddresses, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                     @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);

        return new ResponseEntity<AddressDTO>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
