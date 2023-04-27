package com.raithanna.dairy.RaithannaDairy.repositories;
import com.raithanna.dairy.RaithannaDairy.models.supplier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SupplierRepository extends CrudRepository<supplier,Integer> {
   List<supplier> findByOrderByIdDesc();


}
