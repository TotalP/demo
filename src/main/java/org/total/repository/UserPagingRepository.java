package org.total.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.total.model.User;

/**
 * @author Pavlo.Fandych
 */

public interface UserPagingRepository extends PagingAndSortingRepository<User, Long> {
}