package com.Jagritgill.ppmtool.repositories;

import com.Jagritgill.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project , Long> {

    Project findByProjectIdentifier(String projectId);

    @Override
    Iterable<Project> findAll();

    Iterable<Project> findAllByProjectLeader(String username);
    //      was not there when guy was wiring new code
//    @Override
//   Iterable<Project> findAllById(Iterable<Long> iterable);


}
