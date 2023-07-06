package com.honorato.crudspring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import com.honorato.crudspring.dto.CourseDTO;
import com.honorato.crudspring.dto.mapper.CourseMapper;
import com.honorato.crudspring.exception.RecordNotFoundExeption;
import com.honorato.crudspring.model.Course;
import com.honorato.crudspring.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public  List<CourseDTO> list() {
        return courseRepository.findAll()
        .stream()
        .map(courseMapper::toDTO)
        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
     
    }


    public CourseDTO findById(@PathVariable @NotNull @Positive Long id){
        return courseRepository.findById(id).map(courseMapper::toDTO)
        .orElseThrow(() -> new RecordNotFoundExeption(id));
      
    }
    public CourseDTO create( @Valid Course course){
        return courseMapper.toDTO(courseRepository.save(course)) ;
          
   }
   public CourseDTO update( @NotNull @Positive Long id,  Course course) {
    return courseRepository
    .findById(id)
    .map(recordFound -> {
        recordFound.setName(course.getName());
        recordFound.setCategory(course.getCategory());
        return courseMapper.toDTO(courseRepository.save(recordFound)) ;
        
    }).orElseThrow(() -> new RecordNotFoundExeption(id));
    
 }
 public void delete(@PathVariable @NotNull @Positive Long id) {

    courseRepository.delete(courseRepository.findById(id).orElseThrow(() -> new RecordNotFoundExeption(id)));

    /* courseRepository.findById(id).map(recordFound -> {
       courseRepository.deleteById(id);
        return true;
    })
    .orElseThrow(() -> new RecordNotFoundExeption(id));*/
 }
}
