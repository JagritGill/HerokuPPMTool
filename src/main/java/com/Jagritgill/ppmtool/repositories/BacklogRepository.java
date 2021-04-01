package com.Jagritgill.ppmtool.repositories;

import com.Jagritgill.ppmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

    //JPA which allows us custom Quersa

    Backlog findByProjectIdentifier(String Identifier);

}
