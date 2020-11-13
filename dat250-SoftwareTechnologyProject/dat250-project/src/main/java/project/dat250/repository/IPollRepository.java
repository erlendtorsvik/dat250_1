package project.dat250.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import project.dat250.entity.Poll;
import project.dat250.entity.User;

public interface IPollRepository extends PagingAndSortingRepository<Poll, Integer> {

	List<Poll> findByNameContainingIgnoreCase(@Param("name") String name);
	List<Poll> findByUser(@Param("user") User user);
	List<Poll> findByIsPublic(@Param("isPublic") boolean isPublic);
	List<Poll> findByStatus(@Param("status") String status);

}
