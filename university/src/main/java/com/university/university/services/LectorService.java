package com.university.university.services;

import com.university.university.DTO.LectorDto;
import com.university.university.entities.Degree;
import com.university.university.entities.Lector;
import com.university.university.exceptions.ResourceNotFoundException;
import com.university.university.mappers.GenericMapper;
import com.university.university.repositories.LectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectorService {
    private final LectorRepository lectorRepository;

    public List<LectorDto> getAll() {
        return GenericMapper.mapList(lectorRepository.findAll(), LectorDto.class);
    }

    public LectorDto getById(Long id) {
        return GenericMapper.map(findByIdOrThrow(id), LectorDto.class);
    }

    @Transactional
    public LectorDto create(String firstname, String lastname, String degree, Double salary) {
        Lector lector = new Lector(firstname, lastname, Degree.valueOf(degree), salary);
        return GenericMapper.map(lectorRepository.save(lector), LectorDto.class);
    }

    @Transactional
    public LectorDto update(Long id, Lector updatedLector) {
        Lector existing = findByIdOrThrow(id);
        GenericMapper.updateMap(updatedLector, existing);
        return GenericMapper.map(lectorRepository.save(existing), LectorDto.class);
    }

    @Transactional
    public void deleteById(Long id) {
        lectorRepository.delete(findByIdOrThrow(id));
    }

    public List<LectorDto> globalSearch(String template) {
        List<Lector> lectors = lectorRepository.searchByTemplate(template);
        return GenericMapper.mapList(lectors, LectorDto.class);
    }

    private Lector findByIdOrThrow(Long id) {
        return lectorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lector is not found"));
    }
}
