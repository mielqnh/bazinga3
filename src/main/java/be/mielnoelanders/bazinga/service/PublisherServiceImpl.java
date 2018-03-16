package be.mielnoelanders.bazinga.service;

import be.mielnoelanders.bazinga.domain.Publisher;
import be.mielnoelanders.bazinga.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    // FIELDS
    private final PublisherRepository repo;

    // CONSTRUCTORS
    @Autowired
    public PublisherServiceImpl(PublisherRepository repo) {
        this.repo = repo;
    }

    // METHODS
    // --> init
    @PostConstruct
    public void init() {

        Publisher publisher1 = new Publisher();
        publisher1.setName("What The What");
        publisher1.setWebsite("www.whatthewhat.com");
        Publisher publisher2 = new Publisher();
        publisher2.setName("Who The Who");
        publisher2.setWebsite("www.whothewho.com");
        Publisher publisher3 = new Publisher();
        publisher3.setName("Where The Where");
        publisher3.setWebsite("www.wherethewhere.com");

        this.repo.saveAll(Arrays.asList(publisher1, publisher2, publisher3));
    }

    // --> create
    @Override
    public Publisher addOne(Publisher publisher) {
        return repo.save(publisher);
    }

    // --> read
    @Override
    public Iterable<Publisher> findAll() {
        return this.repo.findAll();
    }

    @Override
    public Publisher findOneById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // --> update
    @Override
    public Publisher updateOneById(Long Id, Publisher publisher) {
        Publisher result = (Publisher) repo.findByName(publisher.getName());

        if (result != null) {
            result.setName(publisher.getName());
            result.setWebsite(publisher.getWebsite());

            return this.repo.save(result);
        }
        return null;
    }

    // --> delete
    @Override
    public Publisher deleteOneById(Long id) {
        Publisher deletedPublisher = findOneById(id);
        if (deletedPublisher != null) {
            this.repo.deleteById(id);
            return deletedPublisher;
        }
        return null;
    }

    // --> others
    @Override
    public Publisher findByName(String name) {
        return repo.findByName(name);
    }
}


//    @Override
//    public Publisher updatePublisherByName(Long id, Publisher publisher) {
//        Publisher publisherToChange = getOneById(id);
//        if(publisherToChange == null){
//            return null;
//        }else{
//            publisherToChange.setName(publisher.getName());
//            return repo.save(publisherToChange);
//        }
//    }

//    @Override
//    public boolean updatePublisherByName(Publisher publisher) {
//        Publisher result = (Publisher) repo.findByName(publisher.getName());
//
//        if (result != null) {
//            result.setName(publisher.getName());
//            result.setWebsite(publisher.getWebsite());
//
//            this.repo.save(result);
//            return true;
//        }
//        return false;
//    }