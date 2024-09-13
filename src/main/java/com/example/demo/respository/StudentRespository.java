package com.example.demo.respository;

import com.example.demo.models.Ranks;
import com.example.demo.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRespository extends JpaRepository<Student, Long> {

//    @Query("SELECT s from Student s where s.name like ?1%")
    List<Student> findByName(String name);

    Page<Student> findByNameContaining(String name, PageRequest request);

    Page<Student> findByNameContainingIgnoreCaseOrderByCreateAtAsc(String name, PageRequest request);

    Page<Student> findByNameContainingIgnoreCaseOrderByCreateAtDesc(String name, PageRequest request);

    @Query("SELECT s from Student s where s.name like %?1% and year(dob) between ?2 and ?3 order by createAt asc")
    Page<Student> findByNameContainingIgnoreCaseDobBetweenOrderByCreateAtAsc(String name, int startDate, int endDate, PageRequest request);


    Page<Student> findByStudentRank(Ranks studentRank, PageRequest request);

    Page<Student> findByStudentRankAndNameContainingIgnoreCaseOrderByCreateAtAsc(Ranks studentRank, String name, PageRequest request);


    @Query("SELECT s from Student s " +
            "where (:name is null or s.name like :name) " +
            "and (:studentRank is null or s.studentRank like :studentRank) " +
            "and year(s.dob) between :startYear and :endYear")
    Page<Student> searchThanThanh(@Param("name") String name,
                                  @Param("studentRank") Ranks studentRank,
                                  @Param("startYear") int startYear,
                                  @Param("endYear") int endYear,
                                  PageRequest request);
}
