package com.ecomm.project.service;

import com.ecomm.project.exceptions.ResourceNotFoundException;
import com.ecomm.project.model.Address;
import com.ecomm.project.model.User;
import com.ecomm.project.payload.AddressDTO;
import com.ecomm.project.repositories.AddressRepository;
import com.ecomm.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
       List<Address> allAddresses  =  addressRepository.findAll();
       List<AddressDTO> addressDTOS = allAddresses.stream()
               .map(p -> modelMapper.map(p, AddressDTO.class)).toList();

        return addressDTOS;
    }

    @Override
    public AddressDTO findSpecificAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "address_id", addressId));
        AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);

        return addressDTO;
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> userAddresses  =  addressRepository.findByUser(user);
        List<AddressDTO> addressDTOS = userAddresses.stream()
                .map(p -> modelMapper.map(p, AddressDTO.class)).toList();

        return addressDTOS;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "address_id", addressId));

        Address updatedAddress = modelMapper.map(addressDTO, Address.class);

        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setBuildingName(updatedAddress.getBuildingName());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setZipcode(updatedAddress.getZipcode());

        Address savedAddress = addressRepository.save(existingAddress);

        User user = existingAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(savedAddress);
        userRepository.save(user);

        AddressDTO addressDTOupdated = modelMapper.map(savedAddress, AddressDTO.class);

        return addressDTOupdated;
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                        .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = address.getUser();
        user.getAddresses().removeIf(adr -> adr.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(address);
        return "Address with id:" + addressId +" is deleted successful!";
    }
}
