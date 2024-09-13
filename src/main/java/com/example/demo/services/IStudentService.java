package com.example.demo.services;

import com.example.demo.dtos.StudentDTO;
import com.example.demo.dtos.StudentImageDTO;
import com.example.demo.models.Ranks;
import com.example.demo.models.Student;
import com.example.demo.models.StudentImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IStudentService {
    public List<Student> findByName(String name);

    public List<Student> showAll();

    public Student findById(long Id);

    public Student update(long Id,Student student);

    public String delete(long Id);

    public Page<Student> getPagination(PageRequest request);

    public Student add(StudentDTO studentDTO);

    public Page<Student> findByNameContaining(String name, PageRequest request);

    public Page<Student> findByNameContainingIgnoreCaseOrderBy(String name,String orderBy, PageRequest request);

    public Page<Student> findByNameContainingIgnoreCaseDobBetweenOrderByCreateAtAsc(String name, int startDate, int endDate, PageRequest request);

    public Page<Student> findByRankOrderByCreateAtAsc(Ranks studentRank, PageRequest request);

    public Page<Student> findByStudentRankAndNameContainingIgnoreCaseOrderByCreateAtAsc(Ranks studentRank, String name, PageRequest request);

    public Page<Student> searchThanThanh(String name, Ranks studentRank, int startDate, int endDate, PageRequest request);

    public List<StudentImage> findByStudentId(Long id);

    public StudentImage updateImage(Long id, StudentImageDTO studentImageDTO);
}
