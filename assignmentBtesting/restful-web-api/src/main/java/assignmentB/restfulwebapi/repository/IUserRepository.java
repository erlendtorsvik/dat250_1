package assignmentB.restfulwebapi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import assignmentB.restfulwebapi.entity.User;

public interface IUserRepository extends PagingAndSortingRepository<User, String> {

	User findByFname(@Param("Fname") String Fname);

}
