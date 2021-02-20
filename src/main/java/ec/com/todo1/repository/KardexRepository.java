package ec.com.todo1.repository;


import ec.com.todo1.domain.Kardex;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Kardex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KardexRepository extends JpaRepository<Kardex, Long>, JpaSpecificationExecutor<Kardex> {
    @Query(value="SELECT top 1 FROM kardex WHERE product_id = ?1 order by id desc", nativeQuery = true)
    Kardex findLastByProduct(Long idProduct);
}
