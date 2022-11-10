package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //Some comment(can add for people to know what this () is doing, for what created)
    public <T> T convert(Object objectToBeConverted, T convertedObject){//Obj - can convert dto or entity, or entity to dto
        return modelMapper.map(objectToBeConverted, (Type) convertedObject.getClass());//add (Type)() expecting generic type, not obj//casting

    }
//
//both doing same
//    public <T> T convert(Object objectToBeConverted, Class<T> convertedObject){//Class<T> -
//        return modelMapper.map(objectToBeConverted, convertedObject);
//}
}