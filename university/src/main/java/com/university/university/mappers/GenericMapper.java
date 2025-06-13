package com.university.university.mappers;

import com.university.university.DTO.DepartmentDto;
import com.university.university.entities.Department;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GenericMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static final Logger logger = LoggerFactory.getLogger(GenericMapper.class);

    static {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);
    }

    public static <S, D> D map(S source, Class<D> destinationType) {
        try {
            return modelMapper.map(source, destinationType);
        } catch (MappingException e) {
            logger.error("Mapping error from {} to {}. Source: {}. Error: {}",
                    source.getClass().getSimpleName(),
                    destinationType.getSimpleName(),
                    source,
                    e.getMessage()
            );
            return null;
        }
    }

    public static <S, D> List<D> mapList(Collection<S> sourceList, Class<D> destinationType) {
        return sourceList.stream()
                .map(element -> map(element, destinationType))
                .collect(Collectors.toList());
    }

    public static <S, D> void updateMap(S source, D destination) {
        try {
            modelMapper.map(source, destination);
        } catch (MappingException e) {
            logger.error("Mapping error during in-place update. Source: {}. Destination: {}. Error: {}",
                    source, destination, e.getMessage());
        }
    }
}