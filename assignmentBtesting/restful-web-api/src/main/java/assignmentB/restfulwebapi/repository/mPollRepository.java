package assignmentB.restfulwebapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import assignmentB.restfulwebapi.entity.Poll;

@RepositoryRestResource(collectionResourceRel = "polls", path = "polls")
public interface mPollRepository extends MongoRepository<Poll, Integer> {

    List<Poll> findByPollName(@Param("name") String name);

}
