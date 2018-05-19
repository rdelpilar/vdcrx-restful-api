package com.vdcrx.rest.domain.interfaces;

import com.vdcrx.rest.domain.entities.*;

import java.util.Set;

public interface IPerson {
    Set<Role> getRoles();
    Set<Address> getAddresses();
    Set<Phone> getPhones();
}
