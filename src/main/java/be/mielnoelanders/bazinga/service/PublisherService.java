package be.mielnoelanders.bazinga.service;

import be.mielnoelanders.bazinga.domain.Publisher;

public interface PublisherService {

    // --> create
    Publisher addOne(Publisher publisher);

    // --> read
    Iterable<Publisher> findAll();

    Publisher findOneById(Long id);

    // --> update
    Publisher updateOneById(Long id, Publisher publisher);

    // --> delete
    Publisher deleteOneById(Long id);

    // --> others

    Publisher findByName(String name);
}

