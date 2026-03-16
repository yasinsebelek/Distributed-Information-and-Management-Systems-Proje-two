package sau.com.schoolmanagement1.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sau.com.schoolmanagement1.dto.CourseDTO;
import sau.com.schoolmanagement1.exception.ErrorMessages;
import sau.com.schoolmanagement1.exception.ResourceAlreadyException;
import sau.com.schoolmanagement1.exception.ResourceNotFoundException;
import sau.com.schoolmanagement1.mapper.CourseMapper;
import sau.com.schoolmanagement1.model.Course;
import sau.com.schoolmanagement1.repository.CourseRepository;
import sau.com.schoolmanagement1.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log =
            LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {

        log.info("COURSE CREATE START | courseCode={}", courseDTO.getCourseCode());

        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            log.warn("COURSE CREATE CONFLICT | courseCode={}", courseDTO.getCourseCode());

            throw new ResourceAlreadyException(
                    ErrorMessages.courseAlreadyExists(courseDTO.getCourseCode())
            );
        }

        Course course = CourseMapper.toEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);

        log.info("COURSE CREATE SUCCESS | id={} | courseCode={}",
                savedCourse.getId(),
                savedCourse.getCourseCode());

        return CourseMapper.toDTO(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {

        log.info("COURSE GET BY ID START | id={}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("COURSE GET BY ID NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.courseNotFound(id)
                    );
                });

        log.info("COURSE GET BY ID SUCCESS | id={} | courseCode={}",
                course.getId(),
                course.getCourseCode());

        return CourseMapper.toDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {

        log.info("COURSE GET ALL START");

        List<Course> courses = courseRepository.findAll();

        log.info("COURSE GET ALL SUCCESS | count={}", courses.size());

        return CourseMapper.toDTOList(courses);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {

        log.info("COURSE UPDATE START | id={}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("COURSE UPDATE NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.courseNotFound(id)
                    );
                });

        if (!course.getCourseCode().equals(courseDTO.getCourseCode())) {
            if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
                log.warn("COURSE UPDATE CONFLICT | id={} | courseCode={}",
                        id,
                        courseDTO.getCourseCode());

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

        log.info("COURSE UPDATE SUCCESS | id={} | courseCode={}",
                updatedCourse.getId(),
                updatedCourse.getCourseCode());

        return CourseMapper.toDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        log.info("COURSE DELETE START | id={}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("COURSE DELETE NOT FOUND | id={}", id);
                    return new ResourceNotFoundException(
                            ErrorMessages.courseNotFound(id)
                    );
                });

        courseRepository.delete(course);

        log.info("COURSE DELETE SUCCESS | id={} | courseCode={}",
                course.getId(),
                course.getCourseCode());
    }
}