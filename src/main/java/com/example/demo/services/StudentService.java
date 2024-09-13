package com.example.demo.services;

import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.StudentImageDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Ranks;
import com.example.demo.models.Student;
import com.example.demo.models.StudentImage;
import com.example.demo.respository.StudentImageRepository;
import com.example.demo.respository.StudentRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService{
    private final StudentRespository studentRespository;
    private final StudentImageRepository studentImageRepository;

    @Override
    public List<Student> showAll() {
        return studentRespository.findAll();
    }

    @Override
    public Student findById(long Id) {
        return studentRespository.findById(Id).orElseThrow(() -> new ResourceNotFoundException("Student not found with ID " + Id));
    }

    @Override
    public Student update(long Id,Student student) {
        Student temp = findById(Id);
        if (temp != null){
            temp.setName(student.getName());
            temp.setCity(student.getCity());
            temp.setDob(student.getDob());
            temp.setStudentRank(student.getStudentRank());
        }
        assert temp != null;
        return studentRespository.save(temp);
    }

    @Override
    public String delete(long Id) {
        Student student = findById(Id);
        studentRespository.deleteById(Id);
        return "Đã xóa thành công";
    }

    @Override
    public Page<Student> getPagination(PageRequest request) {
        return studentRespository.findAll(request);
    }

    @Override
    public Student add(StudentDTO studentDTO) {
        return studentRespository.save(StudentDTO.mapToStudent(studentDTO));
    }

    @Override
    public Page<Student> findByNameContaining(String name, PageRequest request) {
        return studentRespository.findByNameContaining(name, request);
    }

    @Override
    public List<Student> findByName(String name) {
        return studentRespository.findByName(name);
    }


    @Override
    public Page<Student> findByNameContainingIgnoreCaseOrderBy(String name, String orderBy, PageRequest request) {
        if (Objects.equals(orderBy, "asc")){
            return studentRespository.findByNameContainingIgnoreCaseOrderByCreateAtAsc(name,request);
        }else {
            return studentRespository.findByNameContainingIgnoreCaseOrderByCreateAtDesc(name,request);
        }
    }

    @Override
    public Page<Student> findByNameContainingIgnoreCaseDobBetweenOrderByCreateAtAsc(String name, int startDate, int endDate, PageRequest request) {
        return studentRespository.findByNameContainingIgnoreCaseDobBetweenOrderByCreateAtAsc(name,startDate,endDate,request);
    }

    @Override
    public Page<Student> findByRankOrderByCreateAtAsc(Ranks studentRank, PageRequest request) {
        return studentRespository.findByStudentRank(studentRank, request);
    }

    @Override
    public Page<Student> findByStudentRankAndNameContainingIgnoreCaseOrderByCreateAtAsc(Ranks studentRank, String name, PageRequest request){
        return studentRespository.findByStudentRankAndNameContainingIgnoreCaseOrderByCreateAtAsc(studentRank,name,request);
    }

    @Override
    public Page<Student> searchThanThanh(String name, Ranks studentRank, int startYear, int endYear, PageRequest request) {
        return studentRespository.searchThanThanh(name, studentRank, startYear, endYear, request);
    }

    @Override
    public List<StudentImage> findByStudentId(Long id) {
        return studentImageRepository.findByStudentId(id);
    }

    @Override
    public StudentImage updateImage(Long id, StudentImageDTO studentImageDTO) {
        Student student = findById(id);
        if (findByStudentId(id).size() >=4){
            throw new InvalidParameterException("Mỗi sinh viên chỉ được tối đa 4 hình");
        }
        StudentImage studentImage = StudentImage.builder()
                .student(student)
                .imageUrl(studentImageDTO.getImageUrl())
                .build();
        return studentImageRepository.save(studentImage);
    }

}
