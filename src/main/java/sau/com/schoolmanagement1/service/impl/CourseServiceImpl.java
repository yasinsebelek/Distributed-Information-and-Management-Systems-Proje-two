package sau.com.schoolmanagement1.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sau.com.schoolmanagement1.dto.CourseDTO;
import sau.com.schoolmanagement1.exception.ResourceAlreadyException;
import sau.com.schoolmanagement1.exception.ResourceNotFoundException;
import sau.com.schoolmanagement1.mapper.CourseMapper;
import sau.com.schoolmanagement1.model.Course;
import sau.com.schoolmanagement1.repository.CourseRepository;
import sau.com.schoolmanagement1.service.CourseService;
import sau.com.schoolmanagement1.exception.ErrorMessages;


import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {

        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())){
            throw new ResourceAlreadyException(
                    ErrorMessages.courseAlreadyExists(courseDTO.getCourseCode())
                    );
        }

        Course course = CourseMapper.toEntity(courseDTO);
        Course saveCourse = courseRepository.save(course);

        return CourseMapper.toDTO(saveCourse);

    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.courseNotFound(id)
                        ));

        return CourseMapper.toDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllStudents() {

        List<Course> course = courseRepository.findAll();
        return CourseMapper.toDTOList(course);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorMessages.courseNotFound(id)
                        )
                );

        if(!course.getCourseCode().equals(courseDTO.getCourseCode())) {
            if(courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
                throw new ResourceAlreadyException(
                        ErrorMessages.courseAlreadyExists(courseDTO.getCourseCode())
                );
            }
        }

        course.setCourseCode(courseDTO.getCourseCode());
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setSemester(courseDTO.getSemester());

        Course updatedCourse = courseRepository.save(course);

        return CourseMapper.toDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow( () ->
                        new ResourceNotFoundException(
                                ErrorMessages.courseNotFound(id)
                        )
                );

        courseRepository.delete(course);
    }

}
