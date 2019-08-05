package org.total.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.total.dto.UserDTO;
import org.total.model.User;

/**
 * @author Pavlo.Fandych
 */

public class UserConverter {

    private UserConverter() {
    }

    public static User convertToUser(final UserDTO userDTO) {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {
                map().setName(source.getName());
                map().setAge(source.getAge());
                map().setCityAddress(source.getAddress().getCity());
                map().setHomeNumberAddress(source.getAddress().getHomeNumber());
                map().setStreetAddress(source.getAddress().getStreet());
            }
        });

        return modelMapper.map(userDTO, User.class);
    }
}
