package be.mielnoelanders.bazinga.repository;

import be.mielnoelanders.bazinga.domain.Parameter;
import be.mielnoelanders.bazinga.domain.ParameterEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter,Long> {

}