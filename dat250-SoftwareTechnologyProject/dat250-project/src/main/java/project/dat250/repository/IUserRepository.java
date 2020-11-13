package project.dat250.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import project.dat250.entity.User;

public interface IUserRepository extends PagingAndSortingRepository<User, String> {

	User findByFname(@Param("fname") String fname);

}
