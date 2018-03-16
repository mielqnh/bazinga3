package be.mielnoelanders.bazinga.service;

import be.mielnoelanders.bazinga.domain.Parameter;
import be.mielnoelanders.bazinga.domain.ParameterEnum;
import be.mielnoelanders.bazinga.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    // FIELDS
    private final ParameterRepository repo;

    // CONSTRUCTORS
    @Autowired
    public ParameterServiceImpl(ParameterRepository repo) {
        this.repo = repo;
    }

    // METHODS
    // --> init
    @PostConstruct
    public void init() {

        Parameter parm1 = new Parameter();
        parm1.setType(ParameterEnum.PROFITMARGIN);
        parm1.setPercentage(30);
        Parameter parm2 = new Parameter();
        parm2.setType(ParameterEnum.PREMIUMCUSTOMER);
        parm2.setPercentage(10);
        Parameter parm3 = new Parameter();
        parm3.setType(ParameterEnum.DAMAGEDISCOUNT);
        parm3.setPercentage(20);

        this.repo.saveAll(Arrays.asList(parm1, parm2, parm3));
    }

    // --> create
    @Override
    public Parameter addOne(Parameter parameter) {
        return repo.save(parameter);
    }

    // --> read
    @Override
    public Iterable<Parameter> findAll() {
        return this.repo.findAll();
    }

    @Override
    public Parameter findOneById(long id) {
        return repo.findById(id).orElse(null);
    }

    // --> update
    @Override
    public Parameter updateOneById(Long id, Parameter parameter) {
        Parameter parmToChange = findOneById(id);
        if (parmToChange == null) {
            return null;
        } else {
            return repo.save(parameter);
        }
    }

    // --> delete
    @Override
    public boolean deleteOneById(Long id) {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
            return true;
        }
        return false;
    }
}