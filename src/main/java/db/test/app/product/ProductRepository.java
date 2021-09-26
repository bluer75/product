package db.test.app.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for products.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Collects all non-deleted entities. 
     */
    @Query("select e from #{#entityName} e where e.deleted=false")
    public List<Product> findAllNonDeleted();

    /**
     * Collects all deleted entities. 
     */
    @Query("select e from #{#entityName} e where e.deleted=true")
    public List<Product> findAllDeleted();

    /**
     * Retrieves non-deleted entity with given id. 
     */
    @Query("select e from #{#entityName} e where e.id=?1 and e.deleted=false")
    public Optional<Product> findNonDeletedById(Integer id);

    /**
     * Soft delete entity with given id. 
     */
    @Query("update #{#entityName} e set e.deleted=true where e.id=?1")
    @Modifying(clearAutomatically = true)
    public void softDelete(Integer id);
}