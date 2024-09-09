package com.example.demo.services;

import com.example.demo.dtos.StudentDTO;
import com.example.demo.models.Student;
import com.example.demo.respository.StudentRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService{
    private final StudentRespository studentRespository;

    @Override
    public List<Student> showAll() {
        return studentRespository.findAll();
    }

    @Override
    public Student findById(long Id) {
        return studentRespository.findById(Id).orElse(null);
    }

    @Override
    public Student update(long Id,Student student) {
        Student temp = findById(Id);
        if (temp != null){
            temp.setName(student.getName());
            temp.setCity(student.getCity());
            temp.setDob(student.getDob());
            temp.setRank(student.getRank());
        }
        assert temp != null;
        return studentRespository.save(temp);
    }

    @Override
    public String delete(long Id) {
        try{
            Optional<Student> student = Optional.ofNullable(findById(Id));
            if (student.isPresent()){
                studentRespository.deleteById(Id);
                return "Đã xóa thành công";
            }
        }catch (Exception e){
            log.error("Xóa không thành công");
            return "";
        }
        return "";
    }

    @Override
    public Page<Student> getPagination(PageRequest request) {
        return studentRespository.findAll(request);
    }

    @Override
    public Student add(StudentDTO studentDTO) {
        Student student = Student.builder()
                .name(studentDTO.getName())
                .city(studentDTO.getCity())
                .dob(studentDTO.getDob())
                .rank(studentDTO.getRank())
                .build();
        return studentRespository.save(student);
    }
}
